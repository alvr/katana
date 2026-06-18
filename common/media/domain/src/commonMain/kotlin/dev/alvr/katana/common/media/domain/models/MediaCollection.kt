package dev.alvr.katana.common.media.domain.models

import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListGroup
import kotlin.jvm.JvmInline

@JvmInline value class MediaCollection<out T : MediaEntry>(val lists: List<MediaListGroup<T>>)
