package dev.alvr.katana.domain.lists.models.lists

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import java.time.LocalDate
import java.time.LocalDateTime

data class MediaListEntry<out T : MediaEntry>(
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
    val updatedAt: LocalDateTime?,
    val media: T,
)
