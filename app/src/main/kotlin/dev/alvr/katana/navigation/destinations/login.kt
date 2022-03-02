package dev.alvr.katana.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import dev.alvr.katana.navigation.screens.Screen
import dev.alvr.katana.navigation.screens.ScreensNavigator
import dev.alvr.katana.ui.login.LOGIN_DEEP_LINK
import dev.alvr.katana.ui.login.Login

@ExperimentalAnimationApi
internal fun NavGraphBuilder.login(navigator: ScreensNavigator) {
    composable(
        route = Screen.Login.route,
        deepLinks = listOf(NavDeepLink(LOGIN_DEEP_LINK))
    ) {
        Login(navigator)
    }
}
