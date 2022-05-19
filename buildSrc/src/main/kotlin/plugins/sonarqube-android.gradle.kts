package plugins

import org.sonarqube.gradle.SonarQubeExtension
import utils.addIfExists

configure<SonarQubeExtension> {
    properties {
        addIfExists("sonar.sources", "$projectDir/src/main/kotlin")
        addIfExists("sonar.tests", "$projectDir/src/test/kotlin")
        addIfExists("sonar.java.binaries", "$buildDir/tmp/kotlin-classes/debug")
        addIfExists("sonar.junit.reportPaths", "$buildDir/test-results/testDebugUnitTest")
        property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/kover/project-xml/report.xml")
        property("sonar.android.lint.report", "$buildDir/reports/lint-results-debug.xml")
    }
}
