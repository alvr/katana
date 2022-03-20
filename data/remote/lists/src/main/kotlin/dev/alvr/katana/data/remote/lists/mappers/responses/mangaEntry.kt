package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.domain.lists.models.MangaEntry

internal fun MediaListCollectionQuery.Media?.mangaEntry() = let { entry ->
    MangaEntry(
        entry = mediaEntry(),
        chapters = entry?.chapters,
        volumes = entry?.volumes
    )
}
