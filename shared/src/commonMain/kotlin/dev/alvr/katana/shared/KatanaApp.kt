package dev.alvr.katana.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import dev.alvr.katana.core.ui.strings.LocalBaseStrings
import dev.alvr.katana.core.ui.strings.rememberBaseStrings
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.features.account.ui.strings.LocalAccountStrings
import dev.alvr.katana.features.account.ui.strings.rememberAccountStrings
import dev.alvr.katana.features.explore.ui.strings.LocalExploreStrings
import dev.alvr.katana.features.explore.ui.strings.rememberExploreStrings
import dev.alvr.katana.features.lists.ui.strings.LocalListsStrings
import dev.alvr.katana.features.lists.ui.strings.rememberListsStrings
import dev.alvr.katana.features.login.ui.strings.LocalLoginStrings
import dev.alvr.katana.features.login.ui.strings.rememberLoginStrings
import dev.alvr.katana.features.social.ui.strings.LocalSocialStrings
import dev.alvr.katana.features.social.ui.strings.rememberSocialStrings
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
        KatanaStrings {
            KatanaContent()
        }
    }
}

@Composable
private fun KatanaStrings(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAccountStrings provides rememberAccountStrings(),
        LocalBaseStrings provides rememberBaseStrings(),
        LocalExploreStrings provides rememberExploreStrings(),
        LocalListsStrings provides rememberListsStrings(),
        LocalLoginStrings provides rememberLoginStrings(),
        LocalSocialStrings provides rememberSocialStrings(),
        content = content,
    )
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
