package dev.alvr.katana.features.lists.domain.models.lists

import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry

data class MediaListGroup<out T : MediaEntry>(
    val name: String,
    val entries: List<MediaListEntry<T>>,
)
