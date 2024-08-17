@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.common

import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.kover.gradle.plugin.dsl.KoverReportFiltersConfig
import kotlinx.kover.gradle.plugin.dsl.KoverReportSetConfig
import kotlinx.kover.gradle.plugin.dsl.KoverVerificationRulesConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

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

        // DI
        "*.Module_*Kt",

        // Serializers
        "*.*$\$serializer",

        // Sentry
        "*.SentryLogger",
    )
    private val packagesExcludes = listOf(
        // Core
        "*.core.*",

        // Remote
        "*.data.adapter",
        "*.data.fragment",
        "*.data.selections",
        "*.data.type",

        // UI
        "*.navigation",
        "*.resources",
        "*.screen",
    )

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlinx.kover")

        extensions.configure<KoverProjectExtension> { configureRoot(project) }
    }

    private fun KoverProjectExtension.configureRoot(project: Project) {
        project.subprojects
            .filterNot { it.childProjects.isEmpty() }
            .forEach { configureSubproject(it) }

        configureCommon()
    }

    private fun KoverProjectExtension.configureSubproject(project: Project) {
        project.apply(plugin = "org.jetbrains.kotlinx.kover")
        project.rootProject.dependencies { add("kover", project) }

        configureCommon()
    }

    private fun KoverProjectExtension.configureCommon() {
        reports {
            filters.configure()
            total.configure()
            verify.configure()
        }
    }

    private fun KoverReportSetConfig.configure() {
        filters.configure()

        verify {
            rule("Minimal instruction coverage rate in percent") {
                bound {
                    coverageUnits = CoverageUnit.INSTRUCTION
                    minValue = MIN_COVERED_PERCENTAGE
                }
            }
            rule("Minimal line coverage rate in percent") {
                bound {
                    coverageUnits = CoverageUnit.LINE
                    minValue = MIN_COVERED_PERCENTAGE
                }
            }
        }
    }

    private fun KoverReportFiltersConfig.configure() {
        excludes {
            annotatedBy(
                "androidx.compose.runtime.Composable",
                "androidx.compose.ui.tooling.preview.Preview",
            )
            classes(classesExcludes)
            packages(packagesExcludes)
        }
    }

    private fun KoverVerificationRulesConfig.configure() {
        rule("Minimal instruction coverage rate in percent") {
            bound {
                coverageUnits = CoverageUnit.INSTRUCTION
                minValue = MIN_COVERED_PERCENTAGE
            }
        }
        rule("Minimal line coverage rate in percent") {
            bound {
                coverageUnits = CoverageUnit.LINE
                minValue = MIN_COVERED_PERCENTAGE
            }
        }
    }

    private companion object {
        const val MIN_COVERED_PERCENTAGE = 80
    }
}
