package dev.alvr.katana.navigation.screens

import androidx.navigation.NavController
import dev.alvr.katana.ui.login.LoginNavigator

internal class ScreensNavigator(private val navController: NavController) : LoginNavigator {
    override fun goToHome() {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
    }
}
