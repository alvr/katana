package dev.alvr.katana.buildlogic.gradle

import dev.alvr.katana.buildlogic.isRelease
import dev.alvr.katana.buildlogic.kover
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.kover.gradle.plugin.dsl.KoverReportExtension
import kotlinx.kover.gradle.plugin.dsl.KoverVerifyReportConfig
import kotlinx.kover.gradle.plugin.dsl.MetricType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

internal class KatanaKoverPlugin : Plugin<Project> {

    private val classesExcludes = listOf(
        // App
        "*.KatanaApp",
        "*.*Initializer",

        // Apollo
        "*.remote.*.*Mutation*",
        "*.remote.*.*Query*",

        // Common Android
        "*.BuildConfig",
        "*.*Activity",
        "*.*Fragment",

        // Compose
        "*.*ComposableSingletons*",

        // Serializers
        "*.*$\$serializer",
    )
    private val packagesIncludes = listOf("dev.alvr.katana")
    private val packagesExcludes = listOf(
        // Common
        "*.common.tests",

        // DI
        "*.di",

        // Remote
        "*.remote.*.adapter",
        "*.remote.*.fragment",
        "*.remote.*.selections",
        "*.remote.*.type",
    )

    private val containerModules = listOf(
        ":common",
        ":data",
        ":data:preferences",
        ":data:remote",
        ":domain",
        ":ui",
    )

    override fun apply(target: Project) = with(target) {
        allprojects {
            if (path !in containerModules) {
                apply(plugin = "org.jetbrains.kotlinx.kover")
            }
        }

        with(extensions) {
            configure<KoverProjectExtension> { configure(project) }
            configure<KoverReportExtension> { configure() }
        }

        dependencies {
            subprojects
                .filterNot { it.path in containerModules }
                .forEach { kover(it.path) }
        }
    }

    private fun KoverProjectExtension.configure(project: Project) {
        project.tasks.withType<Test> {
            if (isRelease) disable()
        }
    }

    private fun KoverReportExtension.configure() {
        filters {
            includes { packages(packagesIncludes) }
            excludes {
                annotatedBy(
                    "androidx.compose.runtime.Composable",
                    "androidx.compose.ui.tooling.preview.Preview",
                )
                classes(classesExcludes)
                packages(packagesExcludes)
            }
        }

//        defaults {
//            mergeWith(ANDROID_VARIANT)
//            verify { configure() }
//        }

//        androidReports(ANDROID_VARIANT) {
//            verify { configure() }
//        }
    }

    private fun KoverVerifyReportConfig.configure() {
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

    private companion object {
        const val ANDROID_VARIANT = "debug"
        const val MIN_COVERED_PERCENTAGE = 80
    }
}
