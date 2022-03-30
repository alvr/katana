package dev.alvr.katana.domain.lists.models.entries

import java.time.LocalDateTime

data class AnimeEntry(
    val entry: MediaEntry,
    val episodes: Int?,
    val nextEpisode: NextEpisode?
) : MediaEntry(entry) {
    data class NextEpisode(
        val number: Int,
        val at: LocalDateTime
    )
}
