package dev.alvr.katana.buildlogic.mp.mobile

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ANDROID_LIBRARY_PLUGIN
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.fullPackageName
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.configure

internal class KatanaMultiplatformMobilePlugin : KatanaMultiplatformMobileBasePlugin(ANDROID_LIBRARY_PLUGIN) {
    context(Project)
    override fun ExtensionContainer.configureAndroid() {
        configure<LibraryExtension> { configureAndroid(fullPackageName) }
    }
}
