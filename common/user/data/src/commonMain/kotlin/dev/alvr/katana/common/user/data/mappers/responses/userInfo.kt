package dev.alvr.katana.common.user.data.mappers.responses

import dev.alvr.katana.common.user.data.UserInfoQuery
import dev.alvr.katana.common.user.domain.models.UserInfo

internal operator fun UserInfoQuery.Data?.invoke() = UserInfo(
    username = this?.user?.name.orEmpty(),
    avatar = this?.user?.avatar?.medium.orEmpty(),
    banner = this?.user?.bannerImage.orEmpty(),
)
