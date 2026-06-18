package dev.alvr.katana.common.media.domain.models.lists

import dev.alvr.katana.common.media.domain.models.entries.MediaEntry

data class MediaListEntry<out T : MediaEntry>(val list: MediaList, val entry: T)
