package dev.alvr.katana.ui.account.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.account.entities.UserInfoUi
import dev.alvr.katana.ui.account.navigation.AccountNavigator
import dev.alvr.katana.ui.account.strings.LocalAccountStrings
import dev.alvr.katana.ui.account.viewmodel.AccountViewModel
import dev.alvr.katana.ui.base.components.home.KatanaHomeTopAppBar
import dev.alvr.katana.ui.base.navigation.Destination
import dev.alvr.katana.ui.base.viewmodel.collectAsState
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
