package dev.alvr.katana.ui.account.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.account.component.AccountComponent
import dev.alvr.katana.ui.account.entities.UserInfoUi
import dev.alvr.katana.ui.account.strings.LocalAccountStrings
import dev.alvr.katana.ui.base.components.home.KatanaHomeTopAppBar

@Composable
fun AccountContent(
    component: AccountComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.collectAsState()

    AccountScreen(
        modifier = modifier,
        userInfo = state.userInfo,
        onLogoutClick = component::onLogoutClick,
    )
}

@Composable
private fun AccountScreen(
    userInfo: UserInfoUi,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
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
