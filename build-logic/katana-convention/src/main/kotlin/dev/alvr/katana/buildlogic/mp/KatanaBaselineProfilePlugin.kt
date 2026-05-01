package dev.alvr.katana.buildlogic.mp

import com.android.build.api.dsl.TestExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

internal class KatanaBaselineProfilePlugin : Plugin<Project> {

    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "com.android.test")
            apply(plugin = "androidx.baselineprofile")

            with(extensions) {
                commonExtensions()
                configure<TestExtension> { configureAndroid() }
            }

            tasks.commonTasks()
        }

    private fun TestExtension.configureAndroid() {
        compileSdk = KatanaConfiguration.CompileSdk
        namespace = "${KatanaConfiguration.PackageName}.baselineprofile"
        targetProjectPath = ":app-android"

        defaultConfig {
            minSdk = KatanaConfiguration.MinSdk
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}
