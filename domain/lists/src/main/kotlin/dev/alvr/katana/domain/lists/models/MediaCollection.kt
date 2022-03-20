package dev.alvr.katana.domain.lists.models

data class MediaCollection<T : MediaEntry>(
    val entries: List<MediaList<T>>,
    val name: String,
    val isCustomList: Boolean,
    val isSplitCompletedList: Boolean,
)
