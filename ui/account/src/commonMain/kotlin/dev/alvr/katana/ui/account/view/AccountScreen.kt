package dev.alvr.katana.ui.account.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.account.entities.UserInfoUi
import dev.alvr.katana.ui.account.navigation.AccountNavigator
import dev.alvr.katana.ui.account.viewmodel.AccountViewModel
import dev.alvr.katana.ui.base.components.home.KatanaHomeTopAppBar
import dev.alvr.katana.ui.base.navigation.Destination
import dev.alvr.katana.ui.base.viewmodel.collectAsState
import katana.ui.account.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
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
@OptIn(ExperimentalResourceApi::class)
private fun AccountScreen(
    userInfo: UserInfoUi,
    onLogoutClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            KatanaHomeTopAppBar(
                title = stringResource(Res.string.title),
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
