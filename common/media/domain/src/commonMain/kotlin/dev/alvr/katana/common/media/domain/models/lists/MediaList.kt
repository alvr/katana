package dev.alvr.katana.common.media.domain.models.lists

import dev.alvr.katana.common.media.domain.models.ItemEntryId
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class MediaList(
    val id: ItemEntryId,
    val score: Float?,
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
