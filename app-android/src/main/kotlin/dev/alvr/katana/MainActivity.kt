package dev.alvr.katana

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

@Inject
@ActivityKey
@ContributesIntoMap(AppScope::class, binding<Activity>())
internal class MainActivity(viewModelFactory: MetroViewModelFactory) : ComponentActivity() {
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory = viewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val app = (application as KatanaApp).appComponentProviders.app

        setContent { app() }
    }
}
