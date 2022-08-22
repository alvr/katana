package plugins

import org.sonarqube.gradle.SonarQubeExtension
import utils.addFilesIfExist

configure<SonarQubeExtension> {
    properties {
        addFilesIfExist("sonar.sources", "$projectDir/src/main/kotlin")
        addFilesIfExist("sonar.tests", "$projectDir/src/test/kotlin")
        addFilesIfExist("sonar.java.binaries", "$buildDir/classes/kotlin")
        addFilesIfExist("sonar.junit.reportPaths", "$buildDir/test-results/test")
        addFilesIfExist("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/kover/xml/report.xml")
    }
}
