package dev.alvr.katana.buildlogic.gradle

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogVersion
import dev.alvr.katana.buildlogic.isRelease
import kotlinx.kover.api.CounterType
import kotlinx.kover.api.IntellijEngine
import kotlinx.kover.api.KoverMergedConfig
import kotlinx.kover.api.KoverMergedFilters
import kotlinx.kover.api.KoverProjectConfig
import kotlinx.kover.api.KoverTaskExtension
import kotlinx.kover.api.KoverVerifyConfig
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

internal class KoverConventionPlugin : ConventionPlugin {
    private val koverIncludes = listOf("dev.alvr.katana.*")
    private val koverExcludes = listOf(
        // App
        "*.KatanaApp",
        "*.initializers.*",

        // Apollo
        "*.remote.*.adapter.*",
        "*.remote.*.fragment.*",
        "*.remote.*.selections.*",
        "*.remote.*.type.*",
        "*.remote.*.*Mutation*",
        "*.remote.*.*Query*",

        // Common
        "*.common.*",

        // Common Android
        "*.BuildConfig",
        "*.*Activity",
        "*.*Fragment",
        "*.base.*",
        "*.navigation.*",

        // Compose
        "*.*ComposableSingletons*",

        // Koin
        "*.di.*",

        // Serializers
        "*$\$serializer",

        // Ui
        "*.ui.*.components.*",
        "*.ui.*.view.*",
    )

    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlinx.kover")
        val engineVersion = catalogVersion("coverage-engine")

        allprojects {
            apply(plugin = "kover")

            extensions.configure<KoverProjectConfig> {
                engine.set(IntellijEngine(engineVersion))
                filters {
                    classes {
                        excludes.addAll(koverExcludes)
                        includes.addAll(koverIncludes)
                    }
                }
            }

            tasks.withType<Test> {
                extensions.configure<KoverTaskExtension> {
                    isDisabled.set(isRelease)
                }
            }
        }

        extensions.configure<KoverMergedConfig> {
            enable()
            filters { commonFilters() }
            verify { rules() }
        }
    }

    private fun KoverMergedFilters.commonFilters() {
        classes {
            excludes.addAll(koverExcludes)
            includes.addAll(koverIncludes)
        }
    }

    private fun KoverVerifyConfig.rules() {
        rule {
            name = "Minimal instruction coverage rate in percent"
            bound {
                counter = CounterType.INSTRUCTION
                minValue = MIN_COVERED_PERCENTAGE
            }
        }
        rule {
            name = "Minimal line coverage rate in percent"
            bound {
                counter = CounterType.LINE
                minValue = MIN_COVERED_PERCENTAGE
            }
        }
    }

    companion object {
        private const val MIN_COVERED_PERCENTAGE = 80
    }
}
