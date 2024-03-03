package dev.alvr.katana.features.lists.domain.models

import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import kotlin.jvm.JvmInline

@JvmInline
value class MediaCollection<out T : MediaEntry>(val lists: List<MediaListGroup<T>>)
