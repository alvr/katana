package dev.alvr.katana.buildlogic.multiplatform

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ANDROID_LIBRARY_PLUGIN
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.fullPackageName
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.configure

internal class KatanaMultiplatformMobilePlugin : KatanaMultiplatformMobileBasePlugin(ANDROID_LIBRARY_PLUGIN) {
    override fun ExtensionContainer.configureAndroid(project: Project) {
        configure<LibraryExtension> { configureAndroid(project.fullPackageName) }
    }
}
