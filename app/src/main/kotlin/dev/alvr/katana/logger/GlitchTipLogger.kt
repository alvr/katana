package dev.alvr.katana.logger

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel
import io.sentry.Sentry

class GlitchTipLogger : Antilog() {
    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        if (priority < LogLevel.ERROR) return

        throwable?.let { error ->
            message?.let { msg -> Sentry.addBreadcrumb(msg) }
            Sentry.captureException(error)
        }
    }
}
