package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger

@Stable
interface KatanaNavigator {
    val navController: NavHostController

    fun navigateBack()
}

@Suppress("UnusedReceiverParameter")
fun KatanaNavigator.overridden(navigator: String = "KatanaRootNavigator") {
    Logger.i(LogTag) { "Implementation overridden in $navigator" }
}

val KatanaNavigator.viewModelStoreOwner: ViewModelStoreOwner
    @Composable
    get() =
        navController.previousBackStackEntry
            ?: LocalViewModelStoreOwner.current
            ?: error("ViewModelStoreOwner not found")

private const val LogTag = "KatanaNavigator"
