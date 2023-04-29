package dev.alvr.katana.initializers

import android.content.Context
import androidx.startup.Initializer
import dev.alvr.katana.R
import io.sentry.Sentry

internal class SentryInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val res = context.resources

        Sentry.init { options ->
            options.dsn = res.getString(R.string.sentry_dsn)
            options.tracesSampleRate = res.getInteger(R.integer.sentry_sample_rate).toDouble()
            options.profilesSampleRate = res.getInteger(R.integer.sentry_sample_rate).toDouble()
        }
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>>>()
}
