package dev.alvr.katana.ui.account.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.account.navigation.AccountNavigator
import dev.alvr.katana.ui.account.viewmodel.AccountState
import dev.alvr.katana.ui.account.viewmodel.AccountViewModel
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
        state = state,
        onLogoutClick = {
            vm.clearSession()
            navigator.logout()
        },
    )
}

@Composable
private fun AccountScreen(
    state: AccountState,
    onLogoutClick: () -> Unit,
) {
    Scaffold { paddingValues ->
        Header(
            modifier = Modifier.padding(paddingValues),
            username = state.username,
            avatar = state.avatar,
            onLogoutClick = onLogoutClick,
        )
    }
}
