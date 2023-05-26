package dev.alvr.katana.domain.lists.models.lists

import dev.alvr.katana.domain.lists.models.entries.MediaEntry

data class MediaListGroup<out T : MediaEntry>(
    val name: String,
    val entries: List<MediaListEntry<T>>,
)