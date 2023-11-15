package dev.alvr.katana.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import dev.alvr.katana.KatanaBuildConfig
import dev.alvr.katana.ui.account.strings.LocalAccountStrings
import dev.alvr.katana.ui.account.strings.rememberAccountStrings
import dev.alvr.katana.ui.explore.strings.LocalExploreStrings
import dev.alvr.katana.ui.explore.strings.rememberExploreStrings
import dev.alvr.katana.ui.lists.strings.LocalListsStrings
import dev.alvr.katana.ui.lists.strings.rememberListsStrings
import dev.alvr.katana.ui.login.strings.LocalLoginStrings
import dev.alvr.katana.ui.login.strings.rememberLoginStrings
import dev.alvr.katana.ui.social.strings.LocalSocialStrings
import dev.alvr.katana.ui.social.strings.rememberSocialStrings
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryLevel
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb

@Composable
internal fun KatanaStrings(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAccountStrings provides rememberAccountStrings(),
        LocalExploreStrings provides rememberExploreStrings(),
        LocalListsStrings provides rememberListsStrings(),
        LocalLoginStrings provides rememberLoginStrings(),
        LocalSocialStrings provides rememberSocialStrings(),
        content = content,
    )
}

internal fun initApp() {
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
    Sentry.init { options ->
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
