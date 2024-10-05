package dev.alvr.katana.shared

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import dev.alvr.katana.core.common.KatanaBuildConfig
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.utils.noInsets
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.home.ui.screen.home
import dev.alvr.katana.features.login.ui.screen.login
import dev.alvr.katana.shared.navigation.RootNavigator
import dev.alvr.katana.shared.navigation.rememberKatanaRootNavigator
import dev.alvr.katana.shared.viewmodel.MainViewModel
import io.sentry.kotlin.multiplatform.PlatformOptionsConfiguration
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryLevel
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb
import org.koin.compose.viewmodel.koinNavViewModel
import org.koin.core.annotation.KoinExperimentalAPI

internal expect fun sentryOptionsConfiguration(): PlatformOptionsConfiguration

@Composable
fun Katana() {
    initApp()

    KatanaTheme {
        KatanaApp()
    }
}

@Composable
@OptIn(KoinExperimentalAPI::class)
private fun KatanaApp(
    modifier: Modifier = Modifier,
    navigator: RootNavigator = rememberKatanaRootNavigator(),
    vm: MainViewModel = koinNavViewModel()
) {
    val state by vm.collectAsState()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.noInsets,
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            navController = navigator.navController,
            startDestination = state.initialScreen,
        ) {
            login(loginNavigator = navigator)
            home(homeNavigator = navigator)
        }
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
