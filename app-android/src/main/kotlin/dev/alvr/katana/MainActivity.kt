package dev.alvr.katana

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import dev.alvr.katana.di.AppGraph
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

    private val appGraph by lazy { (application as KatanaApp).appComponentProviders }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent { appGraph.app() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            intent.dataString?.let { url -> appGraph.deepLinkDispatcher.dispatch(url) }
        }
    }
}
