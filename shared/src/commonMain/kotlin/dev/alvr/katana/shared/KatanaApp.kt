package dev.alvr.katana.shared

import androidx.compose.runtime.Composable
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import dev.alvr.katana.core.ui.theme.KatanaTheme
import io.sentry.kotlin.multiplatform.PlatformOptionsConfiguration
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryLevel
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb

@Composable
internal expect fun KatanaContent()

internal expect fun sentryOptionsConfiguration(): PlatformOptionsConfiguration

@Composable
fun Katana() {
    initApp()

    KatanaTheme {
        KatanaContent()
    }
}

private fun initApp() {
    initSentry()
    initNapier()
}

private fun initNapier() {
    if (KatanaBuildConfig.DEBUG) {
        Logger.setLogWriters(platformLogWriter(DefaultFormatter))
    } else {
        Logger.setLogWriters(SentryLogger(Severity.Error))
    }
}

private fun initSentry() {
    Sentry.initWithPlatformOptions(sentryOptionsConfiguration())
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
