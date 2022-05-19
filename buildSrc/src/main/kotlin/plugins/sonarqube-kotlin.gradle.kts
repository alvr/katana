package plugins

import org.sonarqube.gradle.SonarQubeExtension
import utils.addIfExists

configure<SonarQubeExtension> {
    properties {
        addIfExists("sonar.sources", "$projectDir/src/main/kotlin")
        addIfExists("sonar.tests", "$projectDir/src/test/kotlin")
        addIfExists("sonar.java.binaries", "$buildDir/classes/kotlin")
        addIfExists("sonar.junit.reportPaths", "$buildDir/test-results/test")
        property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/kover/project-xml/report.xml")
    }
}
