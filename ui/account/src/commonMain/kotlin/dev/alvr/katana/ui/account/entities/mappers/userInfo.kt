package dev.alvr.katana.ui.account.entities.mappers

import dev.alvr.katana.domain.user.models.UserInfo
import dev.alvr.katana.ui.account.entities.UserInfoUi

internal fun UserInfo.toEntity() = UserInfoUi(
    username = username,
    avatar = avatar,
    banner = banner,
)
