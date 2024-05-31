package dev.alvr.katana.features.account.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiState
import dev.alvr.katana.features.account.ui.entities.UserInfoUi

internal data class AccountState(
    val userInfo: UserInfoUi = UserInfoUi(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
) : UiState
