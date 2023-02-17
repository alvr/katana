package dev.alvr.katana.buildlogic.analysis

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.KatanaConfiguration
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.sonarqube.gradle.SonarExtension

internal class SonarConventionPlugin : ConventionPlugin {
    private val codeExclusions = listOf(
        "**/R.*",
        "**/R$*.*",
        "**/BuildConfig.*",
    )

    private val coverageExclusions = listOf(
        // App
        "**/KatanaApp.kt",
        "**/initializers/**",

        // Common
        "**/common/**",

        // Common Android
        "**Activity.kt",
        "**Fragment.kt",
        "**/base/**",
        "**/navigation/**",

        // Koin
        "**/di/**",

        // Ui
        "**/ui/**/components/**",
        "**/ui/**/view/**",
    )

    override fun Project.configure() {
        apply(plugin = "org.sonarqube")

        extensions.configure<SonarExtension> {
            properties {
                property("sonar.host.url", "https://sonarcloud.io")
                property("sonar.organization", "alvr")
                property("sonar.projectKey", "alvr_katana")
                property("sonar.projectName", "Katana")
                property(
                    "sonar.projectVersion",
                    "${KatanaConfiguration.VersionName}_(${KatanaConfiguration.VersionCode})",
                )

                property("sonar.pullrequest.github.repository", "alvr/katana")
                property("sonar.pullrequest.provider", "GitHub")

                property("sonar.coverage.exclusions", coverageExclusions.joinToString(separator = ","))
                property("sonar.exclusions", codeExclusions.joinToString(separator = ","))
                property("sonar.java.coveragePlugin", "jacoco")
                property("sonar.kotlin.detekt.reportPaths", "${rootProject.buildDir}/reports/detekt/detekt.xml")
                property("sonar.language", "kotlin")
                property("sonar.log.level", "TRACE")
                property("sonar.qualitygate.wait", true)
                property("sonar.sourceEncoding", "UTF-8")
                property("sonar.tags", "android")
                property("sonar.verbose", true)
            }
        }
    }
}
