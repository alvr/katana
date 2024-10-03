package dev.alvr.katana.features.account.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.alvr.katana.core.ui.components.home.KatanaHomeTopAppBar
import dev.alvr.katana.core.ui.destinations.HomeDestination
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.account.ui.entities.UserInfoUi
import dev.alvr.katana.features.account.ui.navigation.AccountNavigator
import dev.alvr.katana.features.account.ui.resources.Res
import dev.alvr.katana.features.account.ui.resources.title
import dev.alvr.katana.features.account.ui.screens.components.UserInfo
import dev.alvr.katana.features.account.ui.viewmodel.AccountViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun AccountScreen(
    navigator: AccountNavigator,
    viewModel: AccountViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()

    AccountScreen(
        userInfo = state.userInfo,
        onLogoutClick = {
            viewModel.clearSession()
            navigator.navigateToLogin()
        },
    )
}

@Composable
private fun AccountScreen(
    userInfo: UserInfoUi,
    onLogoutClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            KatanaHomeTopAppBar(
                title = Res.string.title.value,
                subtitle = null,
            )
        },
    ) { paddingValues ->
        UserInfo(
            modifier = Modifier.padding(paddingValues),
            userInfo = userInfo,
            onLogoutClick = onLogoutClick,
        )
    }
}
