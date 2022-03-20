package dev.alvr.katana.domain.lists.models

import java.time.LocalDate

data class MediaList<T : MediaEntry>(
    val id: Int,
    val score: Double,
    val progress: Int,
    val progressVolumes: Int?,
    val repeat: Int,
    val priority: Int,
    val private: Boolean,
    val notes: String,
    val hiddenFromStatusLists: Boolean,
    val startedAt: LocalDate?,
    val completedAt: LocalDate?,
    val media: T
)
