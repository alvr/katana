package dev.alvr.katana.common.user.data.mappers.responses

import dev.alvr.katana.common.user.data.UserInfoQuery
import dev.alvr.katana.common.user.domain.models.UserInfo

internal operator fun UserInfoQuery.Data.invoke() = UserInfo(
    username = viewer.name,
    avatar = viewer.avatar.large,
    banner = viewer.bannerImage,
)
