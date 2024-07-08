package dev.alvr.katana.shared.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import dev.alvr.katana.core.ui.navigation.rememberKatanaNavigator
import dev.alvr.katana.core.ui.screens.RootScreen
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.navigation.rememberKatanaHomeNavigator
import dev.alvr.katana.features.login.ui.navigation.LoginNavigator

internal sealed interface KatanaRootNavigator : LoginNavigator, HomeNavigator {
    val navController: NavHostController
}

private class DefaultKatanaRootNavigator(
    override val navController: NavHostController,
    homeNavigator: HomeNavigator,
) : KatanaRootNavigator,
    HomeNavigator by homeNavigator {

    override fun navigateToHome() {
        navController.navigate(RootScreen.Home) {
            popUpTo(RootScreen.Auth) {
                inclusive = true
            }
        }
    }

    override fun navigateToLogin() {
        navController.navigate(RootScreen.Auth) {
            popUpTo(RootScreen.Home) {
                inclusive = true
            }
        }
    }

    override fun navigateBack() {
        navController.navigateUp()
    }
}

@Composable
internal fun rememberKatanaRootNavigator(): KatanaRootNavigator {
    val homeNavigator = rememberKatanaHomeNavigator()

    return rememberKatanaNavigator { navController ->
        DefaultKatanaRootNavigator(
            navController = navController,
            homeNavigator = homeNavigator,
        )
    }
}
