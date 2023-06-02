package dev.alvr.katana.domain.lists.models.lists

import korlibs.time.Date
import korlibs.time.DateTimeTz

data class MediaList(
    val id: Int,
    val score: Double,
    val progress: Int,
    val progressVolumes: Int?,
    val repeat: Int,
    val private: Boolean,
    val notes: String,
    val hiddenFromStatusLists: Boolean,
    val startedAt: Date?,
    val completedAt: Date?,
    val updatedAt: DateTimeTz?,
)
