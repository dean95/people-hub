package validator

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

class SymbolValidator(private val logger: KSPLogger) {
    fun isValid(symbol: KSAnnotated): Boolean {
        return symbol is KSClassDeclaration && symbol.validate()
    }
}
