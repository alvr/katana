package plugins

import KatanaConfiguration
import org.sonarqube.gradle.SonarQubeExtension
import org.sonarqube.gradle.SonarQubePlugin

apply<SonarQubePlugin>()

val codeExclusions = listOf(
    "**/R.*",
    "**/R$*.*",
    "**/BuildConfig.*",
)

val coverageExclusions = listOf(
    // App
    "**/KatanaApp.kt",
    "**/initializers/**",

    // Common Android
    "**Activity.kt",
    "**Fragment.kt",
    "**/BuildConfig.*",
    "**/base/**",
    "**/navigation/**",

    // Hilt
    "**/di/**",
    "**/*Hilt_*",
    "**/*HiltModules*",
    "*/*_Factory",

    // Ui
    "**/ui/**/view/**",

    // Utils
    "**/utils/tests/**",
)

configure<SonarQubeExtension> {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "alvr")
        property("sonar.projectKey", "alvr_katana")
        property("sonar.projectName", "Katana")
        property("sonar.projectVersion", "${KatanaConfiguration.VersionName}_(${KatanaConfiguration.VersionCode})")

        property("sonar.pullrequest.provider", "GitHub")
        property("sonar.pullrequest.github.repository", "alvr/katana")

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

    subprojects {
        androidVariant = "debug"
    }
}
