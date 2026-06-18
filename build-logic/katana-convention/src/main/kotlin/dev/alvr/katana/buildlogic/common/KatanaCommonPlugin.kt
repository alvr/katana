package dev.alvr.katana.buildlogic.common

import dev.alvr.katana.buildlogic.isRelease
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestReport
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest

internal class KatanaCommonPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(tasks) {
                register<TestReport>("androidTests") {
                    group = "verification"
                    description = "Run the tests for all androidHostTest targets + common"

                    val testTasks = subprojects.map { p -> p.tasks.withType<Test>().matching { t -> !t.isRelease } }

                    mustRunAfter(testTasks)
                    destinationDirectory = file("${layout.buildDirectory.asFile.get()}/reports/android-tests")
                    testResults.setFrom(testTasks)
                }

                register<TestReport>("iosTests") {
                    group = "verification"
                    description = "Run the tests for all iosTest targets + common"

                    val testTasks = subprojects.map { p -> p.tasks.withType<KotlinNativeSimulatorTest>() }

                    mustRunAfter(testTasks)
                    destinationDirectory = file("${layout.buildDirectory.asFile.get()}/reports/ios-tests")
                    testResults.setFrom(testTasks)
                }
            }
        }
    }
}
