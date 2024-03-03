package dev.alvr.katana.buildlogic.common

import dev.alvr.katana.buildlogic.ANDROID_APPLICATION_PLUGIN
import dev.alvr.katana.buildlogic.ANDROID_LIBRARY_PLUGIN
import dev.alvr.katana.buildlogic.isRelease
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.kover.gradle.plugin.dsl.KoverReportExtension
import kotlinx.kover.gradle.plugin.dsl.KoverReportFilters
import kotlinx.kover.gradle.plugin.dsl.MetricType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

internal class KatanaKoverPlugin : Plugin<Project> {

    // Keep in sync with codecov.yml
    private val classesExcludes = listOf(
        // Common
        "*.KatanaApp*",
        "*.KatanaBuildConfig",

        // Android
        "*.*Activity",
        "*.*Fragment",

        // Apollo
        "*.data.*Mutation*",
        "*.data.*Query*",

        // Compose
        "*.*ComposableSingletons*",

        // Serializers
        "*.*$\$serializer",

        // Sentry
        "*.SentryLogger",
    )
    private val packagesExcludes = listOf(
        // Core
        "*.core.*",

        // DI
        "*.di",

        // Remote
        "*.data.adapter",
        "*.data.fragment",
        "*.data.selections",
        "*.data.type",

        // UI
        "*.generated.resources",
        "*.shared.navigation",
        "*.shared.resources",
        "*.shared.strings",
        "*.shared.view",
        "*.ui.navigation",
        "*.ui.resources",
        "*.ui.strings",
        "*.ui.view",
    )

    private val containerModules = listOf(
        ":core",
        ":common",
        ":common:session",
        ":common:user",
        ":features",
        ":features:account",
        ":features:explore",
        ":features:lists",
        ":features:login",
        ":features:social",
    )

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlinx.kover")

        extensions.configure<KoverProjectExtension> { configure(rootProject) }
        subprojects.configureKover()
        extensions.configure<KoverReportExtension> { configureMergedReport() }
    }

    private fun Iterable<Project>.configureKover() {
        forEach { project ->
            with(project) {
                if (path !in containerModules) {
                    apply(plugin = "org.jetbrains.kotlinx.kover")
                    extensions.configure<KoverReportExtension> { configure(project) }
                    rootProject.dependencies.add("kover", this)
                }
            }
        }
    }

    private fun KoverProjectExtension.configure(project: Project) {
        project.allprojects {
            tasks.withType<Test> {
                if (isRelease) disable()
            }
        }

        excludeJavaCode()
    }

    private fun KoverReportExtension.configure(project: Project) {
        with(project) {
            pluginManager.withPlugin(ANDROID_APPLICATION_PLUGIN) { configureAndroidReport() }
            pluginManager.withPlugin(ANDROID_LIBRARY_PLUGIN) { configureAndroidReport() }
        }
    }

    private fun KoverReportExtension.configureMergedReport() {
        filters { configureFilters() }

        verify {
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
    }

    private fun KoverReportExtension.configureAndroidReport() {
        configureMergedReport()

        defaults {
            mergeWith(ANDROID_VARIANT)
            filters { configureFilters() }
        }
    }

    private fun KoverReportFilters.configureFilters() {
        excludes {
            annotatedBy(
                "androidx.compose.runtime.Composable",
                "androidx.compose.ui.tooling.preview.Preview",
            )
            classes(classesExcludes)
            packages(packagesExcludes)
        }
    }

    private companion object {
        const val ANDROID_VARIANT = "debug"
        const val MIN_COVERED_PERCENTAGE = 80
    }
}
