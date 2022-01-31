package dev.alvr.katana

import android.app.Application
import dev.alvr.katana.logger.GlitchTipLogger
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class KatanaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        } else {
            Napier.base(GlitchTipLogger())
        }
    }
}
