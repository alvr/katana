package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.domain.lists.models.entries.MangaEntry
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal fun MediaEntryFragment?.mangaEntry() = let { entry ->
    MangaEntry(
        entry = mediaEntry(),
        chapters = entry?.chapters,
        volumes = entry?.volumes
    )
}
