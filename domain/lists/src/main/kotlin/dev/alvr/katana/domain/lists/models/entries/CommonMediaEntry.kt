package dev.alvr.katana.domain.lists.models.entries

import java.time.LocalDate

data class CommonMediaEntry(
    val id: Int,
    val title: String,
    val coverImage: String,
    val format: Format,
    val startDate: LocalDate,
    val endDate: LocalDate?,
) {
    enum class Format {
        TV,
        TV_SHORT,
        MOVIE,
        SPECIAL,
        OVA,
        ONA,
        MUSIC,
        MANGA,
        NOVEL,
        ONE_SHOT,
        UNKNOWN,
    }
}
