package dev.alvr.katana.features.home.data.mappers.requests

import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.core.remote.type.MediaType

internal fun MediaListType.toRemote() =
    when (this) {
        MediaListType.Anime -> MediaType.ANIME
        MediaListType.Manga -> MediaType.MANGA
    }
