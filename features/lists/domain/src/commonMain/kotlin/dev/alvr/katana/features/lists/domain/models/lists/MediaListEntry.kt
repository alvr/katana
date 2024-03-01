package dev.alvr.katana.features.lists.domain.models.lists

import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry

data class MediaListEntry<out T : MediaEntry>(
    val list: MediaList,
    val entry: T,
)
