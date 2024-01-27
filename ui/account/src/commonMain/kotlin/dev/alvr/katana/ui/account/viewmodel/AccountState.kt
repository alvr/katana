package dev.alvr.katana.ui.account.viewmodel

import dev.alvr.katana.ui.account.entities.UserInfoUi
import dev.alvr.katana.ui.base.decompose.state.UiState

data class AccountState internal constructor(
    internal val userInfo: UserInfoUi = UserInfoUi(),
    internal val isLoading: Boolean = true,
    internal val isError: Boolean = false,
) : UiState
