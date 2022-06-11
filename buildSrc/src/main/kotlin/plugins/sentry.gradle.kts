package plugins

import io.sentry.android.gradle.SentryPlugin
import io.sentry.android.gradle.extensions.SentryPluginExtension

apply<SentryPlugin>()

configure<SentryPluginExtension> {
    includeProguardMapping.set(true)
    autoUploadProguardMapping.set(true)
    experimentalGuardsquareSupport.set(false)
    uploadNativeSymbols.set(false)
    includeNativeSources.set(false)
    tracingInstrumentation.enabled.set(false)
    autoInstallation.enabled.set(false)
    ignoredBuildTypes.set(setOf("debug"))
}
