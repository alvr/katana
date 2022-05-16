package plugins

import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformAndroidPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
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

        property("sonar.kotlin.detekt.reportPaths", "${rootProject.buildDir}/reports/detekt/detekt.xml")

        subprojects.filter {
            it.plugins.hasPlugin(KotlinPlatformJvmPlugin::class) ||
                it.plugins.hasPlugin(KotlinPlatformAndroidPlugin::class)
        }.forEach { p ->
            val isAndroidModule = p.plugins.run {
                hasPlugin("com.android.library") || hasPlugin("com.android.application")
            }

            val sonarTestSources = mutableListOf("src/test/**").apply {
                if (isAndroidModule) add("src/androidTest/**")
            }

            property("sonar.projectName", p.displayName)
            property("sonar.sources", "src/main/**")
            property("sonar.tests", sonarTestSources)

            property("sonar.coverage.jacoco.xmlReportPaths", "${p.buildDir}/reports/kover/project-xml/report.xml")

            if (isAndroidModule) {
                property(
                    "sonar.androidLint.reportPaths",
                    "${p.buildDir}/reports/lint-results-debug.xml",
                )
            }
        }
    }
}
