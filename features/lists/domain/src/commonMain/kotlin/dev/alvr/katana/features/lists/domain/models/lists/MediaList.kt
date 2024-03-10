package dev.alvr.katana.features.lists.domain.models.lists

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class MediaList(
    val id: Int,
    val score: Double,
    val progress: Int,
    val progressVolumes: Int?,
    val repeat: Int,
    val private: Boolean,
    val notes: String,
    val hiddenFromStatusLists: Boolean,
    val startedAt: LocalDate?,
    val completedAt: LocalDate?,
    val updatedAt: LocalDateTime?,
)
