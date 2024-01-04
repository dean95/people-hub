plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
}

dependencies {
    implementation(project(":annotation"))
    implementation(libs.ksp.processor.api)
    implementation(libs.kotlinpoet.ksp)
}