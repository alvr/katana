package dev.alvr.katana.features.lists.domain.models.entries

import kotlinx.datetime.LocalDateTime

sealed class MediaEntry(
    val id: Int,
    val title: String,
    val coverImage: String,
    val format: CommonMediaEntry.Format,
) {
    constructor(entry: CommonMediaEntry) : this(
        id = entry.id,
        title = entry.title,
        coverImage = entry.coverImage,
        format = entry.format,
    )

    data class Anime(
        val entry: CommonMediaEntry,
        val episodes: Int?,
        val nextEpisode: NextEpisode?,
    ) : MediaEntry(entry) {
        data class NextEpisode(
            val number: Int,
            val at: LocalDateTime,
        )
    }

    data class Manga(
        val entry: CommonMediaEntry,
        val chapters: Int?,
        val volumes: Int?,
    ) : MediaEntry(entry)
}
