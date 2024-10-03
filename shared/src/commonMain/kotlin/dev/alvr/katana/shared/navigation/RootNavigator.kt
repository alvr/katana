package dev.alvr.katana.shared.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import dev.alvr.katana.core.ui.destinations.RootDestination
import dev.alvr.katana.core.ui.navigation.rememberKatanaNavigator
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.navigation.rememberKatanaHomeNavigator
import dev.alvr.katana.features.login.ui.navigation.LoginNavigator

internal sealed interface RootNavigator : LoginNavigator, HomeNavigator {
    val navController: NavHostController
}

private class KatanaRootNavigator(
    override val navController: NavHostController,
    homeNavigator: HomeNavigator,
) : RootNavigator,
    HomeNavigator by homeNavigator {

    override fun navigateToHome() {
        navController.navigate(RootDestination.Home) {
            popUpTo(RootDestination.Auth) {
                inclusive = true
            }
        }
    }

    override fun navigateToLogin() {
        navController.navigate(RootDestination.Auth) {
            popUpTo(RootDestination.Home) {
                inclusive = true
            }
        }
    }

    override fun navigateBack() {
        navController.navigateUp()
    }
}

@Composable
internal fun rememberKatanaRootNavigator(): RootNavigator {
    val homeNavigator = rememberKatanaHomeNavigator()

    return rememberKatanaNavigator { navController ->
        KatanaRootNavigator(
            navController = navController,
            homeNavigator = homeNavigator,
        )
    }
}
