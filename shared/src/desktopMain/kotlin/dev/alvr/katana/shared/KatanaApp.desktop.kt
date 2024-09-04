package dev.alvr.katana.shared

import dev.alvr.katana.core.common.KatanaBuildConfig
import io.sentry.kotlin.multiplatform.PlatformOptionsConfiguration

internal actual fun sentryOptionsConfiguration(): PlatformOptionsConfiguration = { options ->
    options.dsn = KatanaBuildConfig.SENTRY_DSN

    options.enableTracing = !KatanaBuildConfig.DEBUG
    options.tracesSampleRate = 1.0

    options.profilesSampleRate = 1.0
}
