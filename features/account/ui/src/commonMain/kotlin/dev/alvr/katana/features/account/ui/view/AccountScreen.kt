package dev.alvr.katana.features.account.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.components.home.KatanaHomeTopAppBar
import dev.alvr.katana.core.ui.navigation.Destination
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.account.ui.entities.UserInfoUi
import dev.alvr.katana.features.account.ui.navigation.AccountNavigator
import dev.alvr.katana.features.account.ui.strings.LocalAccountStrings
import dev.alvr.katana.features.account.ui.viewmodel.AccountViewModel
import org.koin.compose.koinInject

@Composable
@Destination
internal fun AccountScreen(
    navigator: AccountNavigator,
    vm: AccountViewModel = koinInject(),
) {
    val state by vm.collectAsState()

    AccountScreen(
        userInfo = state.userInfo,
        onLogoutClick = {
            vm.clearSession()
            navigator.logout()
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
                title = LocalAccountStrings.current.title,
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
