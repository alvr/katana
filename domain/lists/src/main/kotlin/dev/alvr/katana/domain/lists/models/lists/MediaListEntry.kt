package dev.alvr.katana.domain.lists.models.lists

import dev.alvr.katana.domain.lists.models.entries.MediaEntry

data class MediaListEntry<out T : MediaEntry>(
    val list: MediaList,
    val entry: T,
)
