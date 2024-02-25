package dev.alvr.katana.buildlogic.feature

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.utils.ANDROID_LIBRARY_PLUGIN
import dev.alvr.katana.buildlogic.utils.configureAndroid
import dev.alvr.katana.buildlogic.utils.fullPackageName
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.configure

internal class KatanaFeaturePlugin : KatanaFeatureBasePlugin(ANDROID_LIBRARY_PLUGIN) {
    context(Project)
    override fun ExtensionContainer.configureAndroid() {
        configure<LibraryExtension> { configureAndroid(fullPackageName) }
    }
}
