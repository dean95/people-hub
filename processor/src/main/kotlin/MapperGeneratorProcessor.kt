import annotation.GenerateMapper
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import java.util.Locale
import validator.SymbolValidator

class MapperGeneratorProcessor(
    private val codeGenerator: CodeGenerator,
    logger: KSPLogger
) : SymbolProcessor {

    private val symbolValidator = SymbolValidator(logger)

    // Process the symbols with the GenerateMapper annotation
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotationName = GenerateMapper::class.qualifiedName ?: return emptyList()
        val resolvedSymbols = resolver.getSymbolsWithAnnotation(annotationName).toList()
        val validatedSymbols = resolvedSymbols.filter(symbolValidator::isValid).toList()
        val classDeclarations = validatedSymbols.filterIsInstance<KSClassDeclaration>()

        val mapperInterfaces = mutableMapOf<String, TypeSpec.Builder>()
        val mapperImplementations = mutableMapOf<String, TypeSpec.Builder>()

        // Generate mappers for each class declaration
        classDeclarations.forEach { classDeclaration ->
            generateMappersForClass(resolver, classDeclaration, annotationName, mapperInterfaces, mapperImplementations)
        }

        // Write the generated mapper interfaces and implementations to files
        writeGeneratedMappersToFile(mapperInterfaces, mapperImplementations)

        // Return the symbols that could not be resolved
        return resolvedSymbols - validatedSymbols
    }

    // Generate mappers for a single class declaration
    private fun generateMappersForClass(
        resolver: Resolver,
        classDeclaration: KSClassDeclaration,
        annotationName: String,
        mapperInterfaces: MutableMap<String, TypeSpec.Builder>,
        mapperImplementations: MutableMap<String, TypeSpec.Builder>
    ) {
        val mapperName = extractMapperName(classDeclaration, annotationName)
        val mapperInterfaceBuilder = mapperInterfaces.getOrPut(mapperName) {
            TypeSpec.interfaceBuilder(mapperName)
        }
        val implClassName = "${mapperName}Impl"
        val mapperImplementationBuilder = mapperImplementations.getOrPut(implClassName) {
            TypeSpec.classBuilder(implClassName)
                .addSuperinterface(ClassName("", mapperName))
                .addModifiers(KModifier.INTERNAL)
        }
        generateMapperMethods(resolver, classDeclaration, mapperInterfaceBuilder, mapperImplementationBuilder)
    }

    // Extract the mapper name from class annotations
    private fun extractMapperName(classDeclaration: KSClassDeclaration, annotationName: String) = classDeclaration
        .annotations
        .find {
            it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationName
        }?.arguments?.find { it.name?.asString() == "name" }?.value.toString()

    // Write generated mapper interfaces and implementations to files
    private fun writeGeneratedMappersToFile(
        mapperInterfaces: Map<String, TypeSpec.Builder>,
        mapperImplementations: Map<String, TypeSpec.Builder>
    ) {
        mapperInterfaces.forEach { (name, builder) ->
            val file = FileSpec.builder("", name)
                .addType(builder.build())
                .build()
            file.writeTo(codeGenerator, false)
        }

        mapperImplementations.forEach { (name, builder) ->
            val file = FileSpec.builder("", name)
                .addType(builder.build())
                .build()
            file.writeTo(codeGenerator, false)
        }
    }

    private fun generateMapperMethods(
        resolver: Resolver,
        classDeclaration: KSClassDeclaration,
        mapperInterfaceBuilder: TypeSpec.Builder,
        mapperImplementationBuilder: TypeSpec.Builder
    ) {
        val entityClassName = ClassName(classDeclaration.packageName.asString(), classDeclaration.simpleName.asString())
        val domainClassName = ClassName.bestGuess(
            "peoplehub.domain.model.${entityClassName.simpleName.removeSuffix("Entity")}"
        )
        val domainClassDeclaration = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(domainClassName.canonicalName)
        ) ?: error("Cannot get class declaration")

        val entityProperties = classDeclaration.getDeclaredProperties().toSet()
        val entityPropertiesNames = entityProperties.map { it.simpleName.asString() }.toSet()
        val domainProperties = domainClassDeclaration.getDeclaredProperties().toSet()
        val domainPropertiesNames = domainProperties.map { it.simpleName.asString() }.toSet()
        val commonPropertiesNames = entityPropertiesNames.intersect(domainPropertiesNames)

        val commonProperties = (entityProperties + domainProperties)
            .filter { it.simpleName.asString() in commonPropertiesNames }
            .distinctBy { it.simpleName.asString() }

        // Generate methods for the interface
        val mapToDomainInterfaceFunction = FunSpec.builder(mapperFunctionName(domainClassName))
            .addModifiers(KModifier.ABSTRACT)
            .addParameter(
                entityClassName.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
                entityClassName
            )
            .returns(domainClassName)
            .build()

        val mapToEntityInterfaceFunction = FunSpec.builder(mapperFunctionName(entityClassName))
            .addModifiers(KModifier.ABSTRACT)
            .addParameter(
                domainClassName.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
                domainClassName
            )
            .returns(entityClassName)
            .build()

        mapperInterfaceBuilder
            .addFunction(mapToDomainInterfaceFunction)
            .addFunction(mapToEntityInterfaceFunction)

        // Generate methods for the implementation
        val mapToDomainImplFunction = FunSpec.builder(mapperFunctionName(domainClassName))
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(
                entityClassName.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
                entityClassName
            )
            .returns(domainClassName)
            .addCode(
                generateMappingCode(
                    commonProperties,
                    entityClassName.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
                    domainClassName
                )
            )
            .build()

        val mapToEntityImplFunction = FunSpec.builder(mapperFunctionName(entityClassName))
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(
                domainClassName.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
                domainClassName
            )
            .returns(entityClassName)
            .addCode(
                generateMappingCode(
                    commonProperties,
                    domainClassName.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
                    entityClassName
                )
            )
            .build()

        mapperImplementationBuilder
            .addFunction(mapToDomainImplFunction)
            .addFunction(mapToEntityImplFunction)
    }

    private fun generateMappingCode(
        properties: List<KSPropertyDeclaration>,
        sourceObjectName: String,
        targetObjectName: ClassName
    ): CodeBlock {
        return CodeBlock.builder()
            .add("return %T(\n", targetObjectName)
            .add(properties.joinToString("\n") { property ->
                val propertyString = property.simpleName.asString()
                val propertyTypeString = property.type.resolve().declaration.simpleName.asString()
                if (propertyTypeString in primitiveTypes) {
                    "    $propertyString = $sourceObjectName.$propertyString,"
                } else {
                    val mapperFunctionName = if (targetObjectName.simpleName.endsWith("Entity")) {
                        "mapTo${propertyTypeString}"
                    } else {
                        "mapTo${propertyTypeString.removeSuffix("Entity")}"
                    }
                    "    $property = $sourceObjectName.$property?.let { $mapperFunctionName(it) },"
                }
            })
            .add("\n)\n")
            .build()
    }

    private fun mapperFunctionName(className: ClassName) = "mapTo${className.simpleName}"

    private val primitiveTypes = setOf("Byte", "Short", "Int", "Long", "Float", "Double", "Char", "Boolean", "String")
}
