package dev.alvr.katana.core.ui.navigation

import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger

@Immutable
interface KatanaNavigator {
    fun navigateBack()

    fun <T> popBackStackWithResult(result: T)

    @Composable
    fun <T> getNavigationResult(onResult: (T) -> Unit): State<T?>
}

@Composable
fun <T : KatanaNavigator> rememberKatanaNavigator(factory: (NavHostController) -> T): T {
    val navigator = rememberNavController(
        rememberBottomSheetNavigator(),
    )
        .sentryObserver()
        .loggerObserver()

    return remember(navigator) { factory(navigator) }
}

@Composable
internal expect fun NavHostController.sentryObserver(): NavHostController

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
