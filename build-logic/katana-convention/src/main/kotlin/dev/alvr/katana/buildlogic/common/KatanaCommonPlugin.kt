package dev.alvr.katana.buildlogic.common

import dev.alvr.katana.buildlogic.catalogVersion
import dev.alvr.katana.buildlogic.isRelease
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestReport
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

internal class KatanaCommonPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.louiscad.complete-kotlin")

            // TODO remove when Sentry Multiplatform includes Sentry v7.0.0
            allprojects {
                configurations.configureEach {
                    resolutionStrategy.eachDependency {
                        if (requested.group == "io.sentry" && !requested.name.contains("multiplatform")) {
                            useVersion(catalogVersion("sentry"))
                        }
                    }
                }
            }

            with(tasks) {
                register<Delete>("clean") {
                    allprojects { delete(layout.buildDirectory.asFile.get()) }
                }

                register<TestReport>("unitTests") {
                    val testTasks = subprojects.map { p ->
                        p.tasks.withType<Test>().matching { t -> !t.isRelease }
                    }

                    mustRunAfter(testTasks)
                    destinationDirectory.set(file("${layout.buildDirectory.asFile.get()}/reports/allTests"))
                    testResults.setFrom(testTasks)
                }
            }
        }
    }
}
