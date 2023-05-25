package dev.alvr.katana.buildlogic.analysis

import dev.alvr.katana.buildlogic.ConventionPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.sonarqube.gradle.SonarQubeExtension

internal class KatanaSonarKotlinPlugin : ConventionPlugin {
    override fun Project.configure() {
        extensions.configure<SonarQubeExtension> {
            properties {
                addFilesIfExist(
                    "sonar.coverage.jacoco.xmlReportPaths",
                    "$buildDir/reports/kover/report.xml",
                )
                addFilesIfExist("sonar.java.binaries", "$buildDir/classes/kotlin")
                addFilesIfExist("sonar.junit.reportPaths", "$buildDir/test-results/test")
                addFilesIfExist(
                    "sonar.sources",
                    "$projectDir/src/commonMain/kotlin",
                    "$projectDir/src/jvmMain/kotlin",
                    "$projectDir/src/iosMain/kotlin",
                )
                addFilesIfExist(
                    "sonar.tests",
                    "$projectDir/src/commonTest/kotlin",
                    "$projectDir/src/jvmTest/kotlin",
                    "$projectDir/src/iosTest/kotlin",
                )
            }
        }
    }
}
