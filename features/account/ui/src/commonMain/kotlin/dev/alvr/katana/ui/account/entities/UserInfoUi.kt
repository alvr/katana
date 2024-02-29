package dev.alvr.katana.ui.account.entities

import dev.alvr.katana.common.core.empty

internal data class UserInfoUi(
    val username: String = String.empty,
    val avatar: String = String.empty,
    val banner: String = String.empty,
)
