package dev.alvr.katana.ui.main

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import dev.alvr.katana.ui.base.design.KatanaTheme
import dev.alvr.katana.ui.main.di.katanaModule
import dev.alvr.katana.ui.main.di.platformModule
import dev.alvr.katana.ui.main.navigation.KatanaDestinations
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier
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
    if (BuildConfig.DEBUG) {
        Napier.base(DebugAntilog())
    } else {
        Napier.base(SentryLogger(LogLevel.ERROR))
    }
}

private fun initSentry(context: Context) {
    Sentry.init(context) { options ->
        options.debug = BuildConfig.DEBUG
        options.dsn = "" // TODO Add Sentry DSN
        options.attachScreenshot = true
    }
}

private class SentryLogger(private val minSeverity: LogLevel) : Antilog() {
    private val LogLevel.sentryLevel
        get() = when (this) {
            LogLevel.VERBOSE -> SentryLevel.DEBUG
            LogLevel.DEBUG -> SentryLevel.DEBUG
            LogLevel.INFO -> SentryLevel.INFO
            LogLevel.WARNING -> SentryLevel.WARNING
            LogLevel.ERROR -> SentryLevel.ERROR
            LogLevel.ASSERT -> SentryLevel.FATAL
        }

    override fun isEnable(priority: LogLevel, tag: String?) =
        !BuildConfig.DEBUG && priority >= minSeverity

    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?,
    ) {
        if (throwable != null && priority >= minSeverity) {
            Sentry.addBreadcrumb(
                Breadcrumb(
                    level = priority.sentryLevel,
                    message = "$tag: $message",
                ),
            )
            Sentry.captureException(throwable)
        }
    }
}
