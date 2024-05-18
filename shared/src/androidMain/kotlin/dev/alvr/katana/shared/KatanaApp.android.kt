package dev.alvr.katana.shared

import androidx.compose.runtime.Composable
import dev.alvr.katana.core.ui.utils.isLandscape
import dev.alvr.katana.shared.navigation.KatanaDestinations
import io.sentry.kotlin.multiplatform.PlatformOptionsConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
internal actual fun KatanaContent() {
    KatanaDestinations(
        useNavRail = isLandscape(),
        vm = koinViewModel(),
    )
}

internal actual fun sentryOptionsConfiguration(): PlatformOptionsConfiguration = { options ->
    options.isDebug = KatanaBuildConfig.DEBUG
    options.dsn = KatanaBuildConfig.SENTRY_DSN

    options.enableTracing = true
    options.tracesSampleRate = 1.0

    options.isEnableAppStartProfiling = true
    options.profilesSampleRate = 1.0
}
