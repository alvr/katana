import kotlinx.kover.api.CounterType
import kotlinx.kover.api.IntellijEngine
import kotlinx.kover.api.KoverMergedConfig
import kotlinx.kover.api.KoverProjectConfig
import kotlinx.kover.api.KoverTaskExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import utils.KOVER_MIN_COVERED_PERCENTAGE
import utils.configureKotlin
import utils.koverExcludes
import utils.koverIncludes

plugins {
    alias(libs.plugins.kover)
    plugins.dependencies
    plugins.detekt
    plugins.sonarqube
}

// Version catalogs is not accessible from precompile scripts
// https://github.com/gradle/gradle/issues/15383
buildscript {
    extra.set("composeCompiler", libs.versions.compose.compiler.get())
    extra.set("detektFormatting", libs.detekt.formatting)
}

allprojects {
    apply(plugin = "kover")

    configure<KoverProjectConfig> {
        engine.set(IntellijEngine(coverageEngineVersion))
        filters {
            classes {
                excludes.addAll(koverExcludes)
                includes.addAll(koverIncludes)
            }
        }
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.configureKotlin()
        }

        withType<Test> {
            extensions.configure<KoverTaskExtension> {
                isDisabled.set(isRelease)
            }
        }
    }
}

tasks {
    register<Delete>("clean") {
        delete(buildDir)
    }

    register<TestReport>("unitTests") {
        val testTasks = subprojects.map { p ->
            p.tasks.withType<Test>().matching { t -> !t.isRelease }
        }

        mustRunAfter(testTasks)
        destinationDirectory.set(file("$buildDir/reports/allTests"))
        testResults.setFrom(testTasks)
    }
}

configure<KoverMergedConfig> {
    enable()
    filters {
        classes {
            excludes.addAll(koverExcludes)
            includes.addAll(koverIncludes)
        }
        projects {
            excludes.addAll(
                listOf(
                    ":",
                    ":common",
                    ":common:core",
                    ":common:tests",
                    ":common:tests-android",
                    ":data",
                    ":data:preferences",
                    ":data:remote",
                    ":domain",
                    ":ui",
                ),
            )
        }
    }
    verify {
        rule {
            name = "Minimal line coverage rate in percent"
            bound {
                counter = CounterType.LINE
                minValue = KOVER_MIN_COVERED_PERCENTAGE
            }
        }
    }
}

val coverageEngineVersion: String get() = libs.versions.coverage.engine.get()
val Test.isRelease get() = name.contains("release", ignoreCase = true)
