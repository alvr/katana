package dev.alvr.katana.buildlogic.feature.ui

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

internal class KatanaUiPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.feature")
        apply(plugin = "katana.feature.ui.compose")
    }
}
