package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger

@Composable
internal expect fun NavHostController.sentryObserver(): NavHostController

@Immutable
interface KatanaNavigator {
    fun navigateBack()

    fun <T> popBackStackWithResult(result: T)

    fun <T> getNavigationResult(onResult: (T) -> Unit)
}

@Composable
fun <T : KatanaNavigator> rememberKatanaNavigator(factory: (NavHostController) -> T): T {
    val navigator = rememberNavController().sentryObserver().loggerObserver()
    return remember(navigator) { factory(navigator) }
}

@Composable
private fun NavHostController.loggerObserver() = apply {
    DisposableEffect(this, LocalLifecycleOwner.current.lifecycle) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            Logger.d { "Navigating to route ${destination.route}" }
        }

        addOnDestinationChangedListener(listener)
        onDispose { removeOnDestinationChangedListener(listener) }
    }
}
