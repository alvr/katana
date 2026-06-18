package dev.alvr.katana.common.media.data.mappers.requests

import dev.alvr.katana.common.media.domain.models.lists.MediaListStatus
import dev.alvr.katana.core.remote.optional
import dev.alvr.katana.core.remote.type.MediaListStatus as RemoteMediaListStatus

internal operator fun MediaListStatus.invoke() =
    when (this) {
        MediaListStatus.Current -> RemoteMediaListStatus.CURRENT
        MediaListStatus.All -> null
    }.optional
