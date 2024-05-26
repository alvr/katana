package dev.alvr.katana.common.user.data.mappers.responses

import dev.alvr.katana.common.user.data.UserIdQuery
import dev.alvr.katana.common.user.domain.models.UserId

internal operator fun UserIdQuery.Data.invoke(): UserId = UserId(
    id = viewer.id,
)
