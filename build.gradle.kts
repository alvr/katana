
import kotlinx.kover.api.DefaultIntellijEngine
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
    tasks.withType<KotlinCompile> {
        kotlinOptions.configureKotlin()
    }
}

subprojects {
    apply(plugin = "kover")

    configure<KoverProjectConfig> {
        engine.set(DefaultIntellijEngine)
        filters {
            classes {
                excludes.addAll(koverExcludes)
                includes.addAll(koverIncludes)
            }
        }
    }

    tasks.withType<Test> {
        extensions.configure<KoverTaskExtension> {
            isDisabled.set(name == "testReleaseUnitTest")
        }
    }
}

tasks {
    register<Delete>("clean") {
        delete(buildDir)
    }

    register("unitTests") {
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

    register<TestReport>("testMergedReport") {
        destinationDir = file("$buildDir/reports/allTests")
        reportOn(
            subprojects.map { p ->
                p.tasks.withType<Test>().matching { t ->
                    !t.name.contains("release", ignoreCase = true)
                }
            },
        )
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
            excludes.addAll(listOf(":common:core", ":common:tests", ":common:tests-android"))
        }
    }
    verify {
        rule {
            name = "Minimal line coverage rate in percent"
            bound {
                minValue = KOVER_MIN_COVERED_PERCENTAGE
            }
        }
    }
}
