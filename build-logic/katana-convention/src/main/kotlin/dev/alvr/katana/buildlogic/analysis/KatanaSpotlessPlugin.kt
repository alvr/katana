package dev.alvr.katana.buildlogic.analysis

import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.kotlin.KtfmtStep.TrailingCommaManagementStrategy.COMPLETE
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

internal class KatanaSpotlessPlugin : Plugin<Project> {

    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "com.diffplug.spotless")

            extensions.configure<SpotlessExtension> {
                kotlin {
                    target("**/*.kt")
                    targetExclude(layout.buildDirectory)

                    configure()
                }

                kotlinGradle {
                    target("*.gradle.kts", "**/*.gradle.kts")
                    targetExclude(layout.buildDirectory)

                    configure()
                }
            }
        }

    private fun BaseKotlinExtension.configure() {
        ktfmt().kotlinlangStyle().configure { options ->
            options.setMaxWidth(MaxWidth)
            options.setRemoveUnusedImports(true)
            options.setTrailingCommaManagementStrategy(COMPLETE)
        }
    }
}

private const val MaxWidth = 120
