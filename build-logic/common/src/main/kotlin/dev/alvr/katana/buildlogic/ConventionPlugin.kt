package dev.alvr.katana.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

interface ConventionPlugin : Plugin<Project> {
    fun Project.configure()

    override fun apply(target: Project) {
        target.configure()
    }
}
