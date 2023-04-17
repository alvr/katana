package dev.alvr.katana.buildlogic.multiplatform

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformComposePlugin : ConventionPlugin {
    private val KotlinMultiplatformExtension.compose get() =
        (this as ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies

    override fun Project.configure() {
        apply(plugin = "katana.multiplatform.mobile")
        apply(plugin = "org.jetbrains.compose")

        extensions.configure<KotlinMultiplatformExtension> { configureSourceSets() }
    }

    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(compose.runtime)
                    implementation(compose.material)
                    implementation(compose.foundation)
                }
            }
            val androidMain by getting {
                dependencies {
                    implementation(catalogBundle("ui-compose"))
                }
            }
        }
    }
}
