package dev.alvr.katana.buildlogic.multiplatform

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.fullPackageName
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue

internal class KatanaMultiplatformMobilePlugin : KatanaMultiplatformMobileBasePlugin(AndroidPlugin) {
    override fun ExtensionContainer.configureAndroid(project: Project) {
        configure<LibraryExtension> { configureAndroid(project.fullPackageName) }
    }

    private companion object {
        const val AndroidPlugin = "com.android.library"
    }
}
