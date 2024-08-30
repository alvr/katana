package dev.alvr.katana.buildlogic.mp

import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.configureKotlinCompiler
import java.util.Locale
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

internal fun KotlinMultiplatformExtension.configureIos() {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { ios ->
        ios.binaries.framework {
            baseName = project.frameworkIdentifier
            isStatic = true
        }
    }
}

internal fun KotlinMultiplatformExtension.configureKotlin() {
    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.configure {
                configureKotlinCompiler()
            }
        }
    }
}

internal fun KotlinSourceSet.configureCommonLanguageSettings() {
    languageSettings {
        apiVersion = KatanaConfiguration.KotlinVersion.version
        languageVersion = KatanaConfiguration.KotlinVersion.version
        progressiveMode = true
    }
}

private fun KotlinCompilationTask<*>.configureKotlinCompiler() {
    compilerOptions.configureKotlinCompiler()
}

private val Project.frameworkIdentifier
    get() = path.split(':').identifier

internal val List<String>.identifier
    get() = filter { it.isNotEmpty() }
        .reduceRight { acc, s -> "$acc${s.capitalize()}" }

internal fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
