package dev.alvr.katana.shared.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import dev.alvr.katana.core.ui.navigation.rememberKatanaNavigator
import dev.alvr.katana.core.ui.screens.KatanaScreen
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
        navController.navigate(KatanaScreen.Home.name) {
            popUpTo(KatanaScreen.Auth.name) {
                inclusive = true
            }
        }
    }

    override fun navigateToLogin() {
        navController.navigate(KatanaScreen.Auth.name) {
            popUpTo(KatanaScreen.Home.name) {
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
