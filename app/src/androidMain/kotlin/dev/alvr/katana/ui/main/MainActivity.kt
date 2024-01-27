package dev.alvr.katana.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.defaultComponentContext
import dev.alvr.katana.ui.base.decompose.extensions.asAppComponent
import dev.alvr.katana.ui.main.component.DefaultKatanaComponent

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        val katanaComponent = DefaultKatanaComponent(
            componentContext = defaultComponentContext().asAppComponent(),
            token = intent.dataString,
        )
        setContent { KatanaApp(katanaComponent) }
    }
}
