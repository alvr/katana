package dev.alvr.katana.buildlogic.kotlin

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.commonTasks
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

internal class KotlinTestLibraryConventionPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlin.jvm")

        tasks.commonTasks()
    }
}
