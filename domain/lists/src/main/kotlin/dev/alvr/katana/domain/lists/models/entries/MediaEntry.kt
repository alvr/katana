package dev.alvr.katana.domain.lists.models.entries

open class MediaEntry(
    val id: Int,
    val title: String,
    val coverImage: String,
    val format: Format,
    val genres: List<String>,
) {
    constructor(entry: MediaEntry) : this(
        id = entry.id,
        title = entry.title,
        coverImage = entry.coverImage,
        format = entry.format,
        genres = entry.genres
    )

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
