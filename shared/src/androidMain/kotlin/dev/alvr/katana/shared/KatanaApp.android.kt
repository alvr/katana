package dev.alvr.katana.shared

import io.sentry.kotlin.multiplatform.PlatformOptionsConfiguration

internal actual fun sentryOptionsConfiguration(): PlatformOptionsConfiguration = { options ->
    options.isDebug = KatanaBuildConfig.DEBUG
    options.dsn = KatanaBuildConfig.SENTRY_DSN

    options.enableTracing = true
    options.tracesSampleRate = 1.0

    options.isEnableAppStartProfiling = true
    options.profilesSampleRate = 1.0
}
