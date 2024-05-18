package dev.alvr.katana.shared

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.sentry.kotlin.multiplatform.PlatformOptionsConfiguration
import platform.Foundation.NSNumber

@Composable
internal actual fun KatanaContent() {
    Text(text = "KatanaApp for iOS")
}

internal actual fun sentryOptionsConfiguration(): PlatformOptionsConfiguration = { options ->
    options.debug = KatanaBuildConfig.DEBUG
    options.dsn = KatanaBuildConfig.SENTRY_DSN

    options.enableTracing = true
    options.tracesSampleRate = NSNumber(1.0)

    options.profilesSampleRate = NSNumber(1.0)
}
