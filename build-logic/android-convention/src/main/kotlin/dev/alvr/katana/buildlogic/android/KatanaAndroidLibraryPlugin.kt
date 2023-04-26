package dev.alvr.katana.buildlogic.android

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.desugaring
import dev.alvr.katana.buildlogic.implementation
import dev.alvr.katana.buildlogic.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal class KatanaAndroidLibraryPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.android")
        apply(plugin = "katana.sonar.android")

        with(extensions) {
            commonAndroidExtensions()
            getByType<LibraryExtension>().baseAndroidConfig()
        }

        tasks.commonTasks()

        dependencies {
            implementation(catalogBundle("mobile-common"))
            implementation(catalogLib("koin-android")) { excludeKoinDeps() }

            desugaring(catalogLib("desugaring"))

            testImplementation(catalogBundle("core-common-test"))
            testImplementation(catalogBundle("mobile-android-test"))
        }
    }
}
