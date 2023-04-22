package dev.alvr.katana.data.remote.user.mappers.responses

import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.domain.user.models.UserId

internal operator fun UserIdQuery.Data?.invoke(): UserId = UserId(
    id = checkNotNull(this?.viewer?.id) { "ViewerId is required." },
)
