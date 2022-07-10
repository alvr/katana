package dev.alvr.katana.domain.lists.models.entries

data class CommonMediaEntry(
    val id: Int,
    val title: String,
    val coverImage: String,
    val format: Format,
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
