import kotlinx.kover.api.CoverageEngine
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import utils.KOVER_MIN_COVERED_LINES
import utils.configureKotlin
import utils.koverExcludes
import utils.koverIncludes

plugins {
    plugins.dependencies
    plugins.detekt
    alias(libs.plugins.kover)
}

// Version catalogs is not accessible from precompile scripts
// https://github.com/gradle/gradle/issues/15383
buildscript {
    extra.set("composeCompiler", libs.versions.compose.get())
    extra.set("detektFormatting", libs.detekt.formatting)
}

tasks {
    koverMergedHtmlReport {
        includes = koverIncludes
        excludes = koverExcludes
    }

    koverMergedVerify {
        includes = koverIncludes
        excludes = koverExcludes

        rule {
            name = "Minimal line coverage rate in percent"
            bound {
                minValue = KOVER_MIN_COVERED_LINES
            }
        }
    }

    register<Delete>("clean") {
        delete(buildDir)
    }

    withType<KotlinCompile> {
        kotlinOptions.configureKotlin()
    }

    val unitTests by registering {
        val androidUnitTest = "testDebugUnitTest"
        val kotlinUnitTest = "test"

        subprojects.forEach { p ->
            if (p.tasks.findByName(androidUnitTest) != null) {
                dependsOn("${p.path}:$androidUnitTest")
            } else if (p.tasks.findByName(kotlinUnitTest) != null) {
                dependsOn("${p.path}:$kotlinUnitTest")
            }
        }
    }

    register("allTests") {
        val instrumentedTests = "connectedDebugAndroidTest"

        dependsOn(unitTests)
        subprojects.forEach { p ->
            if (p.tasks.findByName(instrumentedTests) != null) {
                dependsOn("${p.path}:$instrumentedTests")
            }
        }
    }
}

kover {
    coverageEngine.set(CoverageEngine.INTELLIJ)
    intellijEngineVersion.set(libs.versions.intellij.get())
    instrumentAndroidPackage = true
}
