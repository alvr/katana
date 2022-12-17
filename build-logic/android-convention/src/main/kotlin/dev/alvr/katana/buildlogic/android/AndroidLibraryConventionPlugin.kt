package dev.alvr.katana.buildlogic.android

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.desugaring
import dev.alvr.katana.buildlogic.implementation
import dev.alvr.katana.buildlogic.kapt
import dev.alvr.katana.buildlogic.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal class AndroidLibraryConventionPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.android")
        apply(plugin = "org.jetbrains.kotlin.kapt")
        apply(plugin = "com.google.dagger.hilt.android")
        apply(plugin = "katana.sonarqube.android")

        with(extensions) {
            commonAndroidExtensions()
            getByType<LibraryExtension>().baseAndroidConfig()
        }

        tasks.commonTasks()

        dependencies {
            implementation(catalogBundle("common-android"))

            desugaring(catalogLib("desugaring"))

            kapt(catalogBundle("kapt"))

            testImplementation(catalogBundle("test"))
            testImplementation(catalogBundle("test-android"))
        }
    }
}
