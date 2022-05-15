package plugins

import org.sonarqube.gradle.SonarQubeExtension
import org.sonarqube.gradle.SonarQubePlugin

apply<SonarQubePlugin>()

configure<SonarQubeExtension> {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "alvr")
        property("sonar.projectKey", "alvr_katana")
        property("sonar.projectName", "Katana")
        property("sonar.sourceEncoding", "UTF-8")

        property("sonar.coverage.jacoco.xmlReportPaths", "${rootProject.buildDir}/reports/kover/report.xml")
        property("sonar.kotlin.detekt.reportPaths", "${rootProject.buildDir}/reports/detekt/detekt.xml")

        subprojects {
            val isAndroidModule = plugins.run {
                hasPlugin("com.android.library") || hasPlugin("com.android.application")
            }

            if (isAndroidModule) {
                property(
                    "$path.sonar.androidLint.reportPaths",
                    "$buildDir/reports/lint-results-debug.xml",
                )
                property(
                    "$path.sonar.sources",
                    "src/main/kotlin",
                )
                property(
                    "$path.sonar.tests",
                    "src/test/kotlin",
                )
            }
        }
    }
}
