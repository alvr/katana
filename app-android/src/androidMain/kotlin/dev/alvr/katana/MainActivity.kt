package dev.alvr.katana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.defaultComponentContext
import dev.alvr.katana.core.ui.decompose.appComponentContext
import dev.alvr.katana.shared.Katana
import dev.alvr.katana.shared.component.KatanaComponent
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

internal class MainActivity : ComponentActivity() {
    private val katanaComponent by inject<KatanaComponent> {
        parametersOf(defaultComponentContext().appComponentContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent { Katana(katanaComponent) }
    }
}
