package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.features.lists.data.fragment.MediaEntry as MediaEntryFragment
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry

internal fun MediaEntryFragment.mangaEntry() =
    MediaEntry.Manga(entry = mediaEntry(), chapters = chapters, volumes = volumes)
