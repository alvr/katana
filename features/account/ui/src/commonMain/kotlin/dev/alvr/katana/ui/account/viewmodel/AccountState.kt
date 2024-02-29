package dev.alvr.katana.ui.account.viewmodel

import dev.alvr.katana.ui.account.entities.UserInfoUi

internal data class AccountState(
    val userInfo: UserInfoUi = UserInfoUi(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
)
