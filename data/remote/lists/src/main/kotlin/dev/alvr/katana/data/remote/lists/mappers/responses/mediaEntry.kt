package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.domain.lists.models.MediaEntry
import dev.alvr.katana.domain.lists.models.MediaStatus

internal fun MediaListCollectionQuery.Media?.mediaEntry() = let { entry ->
    MediaEntry(
        id = entry?.id ?: 0,
        title = entry?.title?.userPreferred.orEmpty(),
        status = MediaStatus.of(entry?.status?.rawValue),
        coverImage = entry?.coverImage?.large.orEmpty(),
        genres = entry?.genres?.filterNotNull().orEmpty(),
    )
}
