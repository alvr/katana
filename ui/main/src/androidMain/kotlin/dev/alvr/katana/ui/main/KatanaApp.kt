package dev.alvr.katana.ui.main

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import dev.alvr.katana.ui.base.design.KatanaTheme
import dev.alvr.katana.ui.main.di.katanaModule
import dev.alvr.katana.ui.main.di.platformModule
import dev.alvr.katana.ui.main.navigation.KatanaDestinations
import io.sentry.kotlin.multiplatform.Context
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryLevel
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.LocalKoinApplication

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun KatanaApp() {
    KatanaDI {
        initApp(LocalKoinApplication.current.get())

        KatanaTheme {
            KatanaDestinations(
                useNavRail = calculateWindowSizeClass().widthSizeClass > WindowWidthSizeClass.Medium,
                vm = koinViewModel(),
            )
        }
    }
}

@Composable
private fun KatanaDI(
    content: @Composable () -> Unit,
) {
    val platformModule = platformModule()

    KoinApplication(
        application = { modules(katanaModule, platformModule) },
        content = content,
    )
}

private fun initApp(context: Context) {
    initSentry(context)
    initNapier()
}

private fun initNapier() {
    if (KatanaBuildConfig.DEBUG) {
        Logger.setLogWriters(platformLogWriter(DefaultFormatter))
    } else {
        Logger.setLogWriters(SentryLogger(Severity.Error))
    }
}

private fun initSentry(context: Context) {
    Sentry.init(context) { options ->
        options.debug = KatanaBuildConfig.DEBUG
        options.dsn = KatanaBuildConfig.SENTRY_DSN
    }
}

private class SentryLogger(private val minSeverity: Severity) : LogWriter() {
    private val Severity.sentryLevel
        get() = when (this) {
            Severity.Verbose -> SentryLevel.DEBUG
            Severity.Debug -> SentryLevel.DEBUG
            Severity.Info -> SentryLevel.INFO
            Severity.Warn -> SentryLevel.WARNING
            Severity.Error -> SentryLevel.ERROR
            Severity.Assert -> SentryLevel.FATAL
        }

    override fun isLoggable(tag: String, severity: Severity) =
        !KatanaBuildConfig.DEBUG && severity >= minSeverity

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        if (throwable != null && severity >= minSeverity) {
            Sentry.addBreadcrumb(
                Breadcrumb(
                    level = severity.sentryLevel,
                    message = "$tag: $message",
                ),
            )
            Sentry.captureException(throwable)
        }
    }
}
