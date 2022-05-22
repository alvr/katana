package plugins

import org.sonarqube.gradle.SonarQubeExtension
import utils.addFilesIfExist

configure<SonarQubeExtension> {
    properties {
        addFilesIfExist("sonar.sources", "$projectDir/src/main/kotlin")
        addFilesIfExist("sonar.tests", "$projectDir/src/test/kotlin")
        addFilesIfExist("sonar.java.binaries", "$buildDir/tmp/kotlin-classes/debug")
        addFilesIfExist("sonar.junit.reportPaths", "$buildDir/test-results/testDebugUnitTest")
        addFilesIfExist("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/kover/project-xml/report.xml")
        addFilesIfExist("sonar.android.lint.report", "$buildDir/reports/lint-results-debug.xml")
    }
}
