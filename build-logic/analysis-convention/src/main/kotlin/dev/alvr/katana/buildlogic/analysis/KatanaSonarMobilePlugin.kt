package dev.alvr.katana.buildlogic.analysis

import dev.alvr.katana.buildlogic.ConventionPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.sonarqube.gradle.SonarQubeExtension

internal class KatanaSonarMobilePlugin : ConventionPlugin {
    override fun Project.configure() {
        extensions.configure<SonarQubeExtension> {
            properties {
                addFilesIfExist(
                    "sonar.android.lint.report",
                    "$buildDir/reports/lint-results-debug.xml",
                )
                addFilesIfExist(
                    "sonar.coverage.jacoco.xmlReportPaths",
                    "$buildDir/reports/kover/report.xml",
                )
                addFilesIfExist("sonar.java.binaries", "$buildDir/tmp/kotlin-classes/debug")
                addFilesIfExist(
                    "sonar.junit.reportPaths",
                    "$buildDir/test-results/testDebugUnitTest",
                )
                addFilesIfExist(
                    "sonar.sources",
                    "$projectDir/src/commonMain/kotlin",
                    "$projectDir/src/androidMain/kotlin",
                    "$projectDir/src/iosMain/kotlin",
                )
                addFilesIfExist(
                    "sonar.tests",
                    "$projectDir/src/commonTest/kotlin",
                    "$projectDir/src/androidUnitTest/kotlin",
                    "$projectDir/src/iosTest/kotlin",
                )
            }
        }
    }
}
