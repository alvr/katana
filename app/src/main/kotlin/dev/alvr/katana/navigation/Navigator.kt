package dev.alvr.katana.navigation

import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.alvr.katana.ui.login.navigation.LoginNavigator
import dev.alvr.katana.ui.login.view.destinations.LoginDestination

internal class Navigator(
    private val navigator: DestinationsNavigator,
) : LoginNavigator {
    override fun goBack() {
        navigator.navigateUp()
    }

    override fun toHome() {
        navigator.navigate(NavGraphs.home, onlyIfResumed = true) {
            popUpTo(LoginDestination) {
                inclusive = true
            }
        }
    }
}
