@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.analysis

import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.detekt
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register

internal class KatanaDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "io.gitlab.arturbosch.detekt")

        tasks.register<Detekt>("detektAll") {
            description = "Run detekt in all modules"

            parallel = true
            ignoreFailures = false
            autoCorrect = true
            buildUponDefaultConfig = true
            jvmTarget = KatanaConfiguration.JvmTargetStr
            setSource(files(projectDir))
            config.setFrom(files("$rootDir/gradle/config/detekt.yml"))
            include("**/*.kt", "**/*.kts")
            exclude("**/resources/**", "**/build/**")

            reports {
                html.required = true
                sarif.required = true
                txt.required = false
                xml.required = true
            }
        }

        dependencies {
            detekt(catalogLib("detekt-compose"))
            detekt(catalogLib("detekt-compose2"))
            detekt(catalogLib("detekt-formatting"))
        }
    }
}
