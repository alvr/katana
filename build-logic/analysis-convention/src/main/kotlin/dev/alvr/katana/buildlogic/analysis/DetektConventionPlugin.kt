package dev.alvr.katana.buildlogic.analysis

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogLib
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register

class DetektConventionPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "io.gitlab.arturbosch.detekt")

        tasks.register<Detekt>("detektAll") {
            description = "Run detekt in all modules"

            parallel = true
            ignoreFailures = false
            autoCorrect = true
            buildUponDefaultConfig = true
            jvmTarget = KatanaConfiguration.JvmTarget
            setSource(files(projectDir))
            config.setFrom(files("$rootDir/config/detekt.yml"))
            include("**/*.kt", "**/*.kts")
            exclude("**/resources/**", "**/build/**")

            reports {
                html.required.set(true)
                sarif.required.set(true)
                txt.required.set(false)
                xml.required.set(true)
            }
        }

        dependencies {
            add("detektPlugins", catalogLib("detekt-formatting"))
        }
    }
}
