package dev.alvr.katana.common.media.domain.models.lists

import dev.alvr.katana.common.media.domain.models.entries.MediaEntry

data class MediaListGroup<out T : MediaEntry>(val name: String, val entries: List<MediaListEntry<T>>)
