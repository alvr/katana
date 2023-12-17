package dev.alvr.katana.buildlogic.mp.mobile.ui

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

internal class KatanaMultiplatformUiPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.multiplatform.mobile")
        apply(plugin = "katana.multiplatform.compose")
    }
}
