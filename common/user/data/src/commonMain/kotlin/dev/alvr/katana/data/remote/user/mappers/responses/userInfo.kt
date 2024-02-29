package dev.alvr.katana.data.remote.user.mappers.responses

import dev.alvr.katana.data.remote.user.UserInfoQuery
import dev.alvr.katana.domain.user.models.UserInfo

internal operator fun UserInfoQuery.Data?.invoke() = UserInfo(
    username = this?.user?.name.orEmpty(),
    avatar = this?.user?.avatar?.medium.orEmpty(),
    banner = this?.user?.bannerImage.orEmpty(),
)
