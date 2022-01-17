import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import utils.configureKotlin

plugins {
    plugins.dependencies
    plugins.detekt
}

// Version catalogs is not accessible from precompile scripts
// https://github.com/gradle/gradle/issues/15383
buildscript {
    extra.set("detektFormatting", libs.detekt.formatting)
    extra.set("composeCompiler", libs.versions.compose.compiler.get())
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.configureKotlin()
}
