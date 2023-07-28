package dev.alvr.katana.buildlogic.gradle

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.isRelease
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestReport
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

internal class KatanaCommonPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "com.louiscad.complete-kotlin")

        with(tasks) {
            register<Delete>("clean") {
                allprojects { delete(buildDir) }
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
    }
}
