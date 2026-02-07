package dev.alvr.katana.features.account.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.components.KatanaScaffold
import dev.alvr.katana.core.ui.components.home.KatanaHomeTopAppBar
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.account.ui.entities.UserInfoUi
import dev.alvr.katana.features.account.ui.navigation.AccountNavigator
import dev.alvr.katana.features.account.ui.resources.Res
import dev.alvr.katana.features.account.ui.resources.title
import dev.alvr.katana.features.account.ui.screens.components.UserInfo
import dev.alvr.katana.features.account.ui.viewmodel.AccountIntent
import dev.alvr.katana.features.account.ui.viewmodel.AccountViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Suppress("UNUSED_PARAMETER")
internal fun AccountScreen(navigator: AccountNavigator, viewModel: AccountViewModel = koinViewModel()) {
    val state by viewModel.collectAsState()

    AccountScreen(userInfo = state.userInfo, onIntent = viewModel::intent)
}

@Composable
private fun AccountScreen(userInfo: UserInfoUi?, onIntent: (AccountIntent) -> Unit) {
    KatanaScaffold(topBar = { KatanaHomeTopAppBar(title = Res.string.title.value, subtitle = null) }) { paddingValues ->
        userInfo?.let { info ->
            UserInfo(
                userInfo = info,
                onLogoutClick = { onIntent(AccountIntent.Logout) },
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}
