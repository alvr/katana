package dev.alvr.katana.common.media.data.mappers.responses

import dev.alvr.katana.common.media.data.fragment.MediaEntry as MediaEntryFragment
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry

internal fun MediaEntryFragment.mangaEntry() =
    MediaEntry.Manga(entry = mediaEntry(), chapters = chapters, volumes = volumes)
