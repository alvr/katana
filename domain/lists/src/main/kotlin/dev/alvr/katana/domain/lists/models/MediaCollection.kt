package dev.alvr.katana.domain.lists.models

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList

@JvmInline
value class MediaCollection<out T : MediaEntry>(val lists: List<MediaList<T>>)
