package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.data.fragment.MediaEntry as MediaEntryFragment

internal fun MediaEntryFragment.mangaEntry() = MediaEntry.Manga(
    entry = mediaEntry(),
    chapters = chapters,
    volumes = volumes,
)
