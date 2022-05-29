package dev.alvr.katana.navigation

import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.alvr.katana.ui.home.view.destinations.HomeDestination
import dev.alvr.katana.ui.login.navigation.LoginNavigator
import dev.alvr.katana.ui.login.view.destinations.LoginDestination

internal class Navigator(
    private val navController: NavController,
) : LoginNavigator {
    override fun goBack() {
        navController.navigateUp()
    }

    override fun goToHomeFromLogin() {
        navController.navigate(HomeDestination) {
            popUpTo(LoginDestination) {
                inclusive = true
            }
        }
    }
}
