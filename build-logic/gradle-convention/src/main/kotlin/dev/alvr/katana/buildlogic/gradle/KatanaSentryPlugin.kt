package dev.alvr.katana.buildlogic.gradle

import io.sentry.android.gradle.extensions.SentryPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

internal class KatanaSentryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "io.sentry.android.gradle")

        extensions.configure<SentryPluginExtension> {
            includeProguardMapping.set(true)
            autoUploadProguardMapping.set(true)
            dexguardEnabled.set(false)
            uploadNativeSymbols.set(false)
            includeNativeSources.set(false)
            tracingInstrumentation.enabled.set(false)
            autoInstallation.enabled.set(false)
            ignoredBuildTypes.set(setOf("debug"))
        }
    }
}
