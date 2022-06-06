package dev.alvr.katana.initializers

import android.content.Context
import androidx.startup.Initializer
import dev.alvr.katana.BuildConfig
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

internal class LoggerInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>>>()
}
