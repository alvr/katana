package dev.alvr.katana

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority

class KatanaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, LogPriority.VERBOSE)
    }
}
