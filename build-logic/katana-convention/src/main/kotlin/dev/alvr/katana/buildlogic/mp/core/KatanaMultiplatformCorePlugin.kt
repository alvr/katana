package dev.alvr.katana.buildlogic.mp.core

import dev.alvr.katana.buildlogic.mp.commonConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KatanaMultiplatformCorePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        commonConfiguration()
    }
}
