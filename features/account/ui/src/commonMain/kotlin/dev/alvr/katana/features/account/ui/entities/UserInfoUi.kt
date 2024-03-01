package dev.alvr.katana.features.account.ui.entities

import dev.alvr.katana.core.common.empty

internal data class UserInfoUi(
    val username: String = String.empty,
    val avatar: String = String.empty,
    val banner: String = String.empty,
)
