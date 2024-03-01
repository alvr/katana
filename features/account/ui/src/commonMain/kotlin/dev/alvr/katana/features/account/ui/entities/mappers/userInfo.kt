package dev.alvr.katana.features.account.ui.entities.mappers

import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.features.account.ui.entities.UserInfoUi

internal fun UserInfo.toEntity() = UserInfoUi(
    username = username,
    avatar = avatar,
    banner = banner,
)
