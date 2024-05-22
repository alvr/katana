package dev.alvr.katana.buildlogic.mp

import dev.alvr.katana.buildlogic.configureKotlinCompiler
import java.util.Locale
import kotlinx.kover.gradle.plugin.dsl.KoverVerifyReportConfig
import kotlinx.kover.gradle.plugin.dsl.MetricType
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSourceSetConvention
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

internal fun KoverVerifyReportConfig.configure() {
    onCheck = true

    rule("Minimal instruction coverage rate in percent") {
        bound {
            metric = MetricType.INSTRUCTION
            minValue = MIN_COVERED_PERCENTAGE
        }
    }
    rule("Minimal line coverage rate in percent") {
        bound {
            metric = MetricType.LINE
            minValue = MIN_COVERED_PERCENTAGE
        }
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

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal val NamedDomainObjectContainer<KotlinSourceSet>.androidUnitTest:
    NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention

internal fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

private const val MIN_COVERED_PERCENTAGE = 80
