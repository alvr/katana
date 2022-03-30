package dev.alvr.katana.domain.lists.models

import dev.alvr.katana.domain.lists.models.entries.MediaEntry

@JvmInline
value class MediaCollection<T : MediaEntry>(
    val lists: List<MediaList<T>> = emptyList()
)
