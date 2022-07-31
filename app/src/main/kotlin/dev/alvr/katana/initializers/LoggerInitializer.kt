package dev.alvr.katana.initializers

import android.content.Context
import androidx.startup.Initializer
import dev.alvr.katana.BuildConfig
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier
import io.sentry.Sentry
import io.sentry.SentryLevel

internal class LoggerInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        } else {
            Napier.base(SentryLogger(LogLevel.ERROR))
        }
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>>>()
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
            Sentry.addBreadcrumb("$tag: $message")
            Sentry.setLevel(priority.sentryLevel)
            Sentry.captureException(throwable)
        }
    }
}
