package dev.alvr.katana.common.media.domain.models.entries

import dev.alvr.katana.common.media.domain.models.ItemMediaId

data class CommonMediaEntry(val id: ItemMediaId, val title: String, val coverImage: String, val format: Format) {
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
