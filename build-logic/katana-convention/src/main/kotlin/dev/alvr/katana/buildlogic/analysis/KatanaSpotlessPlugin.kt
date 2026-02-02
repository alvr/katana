package dev.alvr.katana.buildlogic.analysis

import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

internal class KatanaSpotlessPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "com.diffplug.spotless")

        extensions.configure<SpotlessExtension> {
            kotlin {
                target("**/*.kt")
                targetExclude(layout.buildDirectory)

                configure(
                    config = mapOf(
                        "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                    ),
                )
            }

            kotlinGradle {
                target("*.gradle.kts", "**/*.gradle.kts")
                targetExclude(layout.buildDirectory)

                configure()
            }
        }
    }

    context(project: Project)
    private fun BaseKotlinExtension.configure(config: Map<String, Any> = emptyMap()) {
        ktlint()
            .setEditorConfigPath(project.rootProject.file(".editorconfig"))
            .editorConfigOverride(
                mapOf(
                    "ktlint_code_style" to "android_studio",
                    "ktlint_standard_filename" to "disabled",
                    "ktlint_standard_property-naming" to "pascal_case",
                ) + config,
            )
    }
}
