package plugins

import org.sonarqube.gradle.SonarQubeExtension
import org.sonarqube.gradle.SonarQubePlugin

apply<SonarQubePlugin>()

val codeExclusions = listOf(
    "**/R.*",
    "**/R$*.*",
    "**/BuildConfig.*",
)

val coverageExclusions = listOf(
    "**/R.*",
    "**/R$*.*",
    "**/BuildConfig.*",
    "**/*Module_*Factory.*",
    "**Activity.kt",
    "**Fragment.kt",
    "/*Parcel.class",
    "**/*\$CREATOR.class",
    "android/**/*.*",
    "**/Lambda$*.class",
    "**/Lambda.class",
    "**/*Lambda.class",
    "**/*Lambda*.class",
    "**Module.kt",
)

configure<SonarQubeExtension> {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "alvr")
        property("sonar.projectKey", "alvr_katana")
        property("sonar.projectName", "Katana")

        property("sonar.pullrequest.provider", "GitHub")
        property("sonar.pullrequest.github.repository", "alvr/katana")

        property("sonar.coverage.exclusions", coverageExclusions.joinToString(separator = ","))
        property("sonar.exclusions", codeExclusions.joinToString(separator = ","))
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.language", "kotlin")
        property("sonar.log.level", "TRACE")
        property("sonar.qualitygate.wait", true)
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.tags", "android")
        property("sonar.verbose", true)
        property("sonar.kotlin.detekt.reportPaths", "${rootProject.buildDir}/reports/detekt/detekt.xml")
    }

    subprojects {
        androidVariant = "debug"
    }
}
