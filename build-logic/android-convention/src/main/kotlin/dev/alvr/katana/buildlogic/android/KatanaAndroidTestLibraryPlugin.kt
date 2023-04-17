package dev.alvr.katana.buildlogic.android

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.commonTasks
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

internal class KatanaAndroidTestLibraryPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.android")

        extensions.getByType<LibraryExtension>().baseAndroidConfig()
        tasks.commonTasks()
    }
}
