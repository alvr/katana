package dev.alvr.katana

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.alvr.katana.shared.di.AndroidActivityComponent
import dev.alvr.katana.shared.di.AndroidApplicationComponent
import dev.alvr.katana.shared.di.create

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val applicationComponent = AndroidApplicationComponent.from(this)
        val activityComponent = AndroidActivityComponent.create(this, applicationComponent)

        setContent { activityComponent.app() }
    }

    private fun AndroidApplicationComponent.Companion.from(context: Context) =
        (context.applicationContext as KatanaApp).component
}
