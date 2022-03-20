package dev.alvr.katana.domain.lists.models

open class MediaEntry(
    val id: Int,
    val title: String,
    val status: MediaStatus,
    val coverImage: String,
    val genres: List<String>,
) {
    constructor(entry: MediaEntry) : this(
        id = entry.id,
        title = entry.title,
        status = entry.status,
        coverImage = entry.coverImage,
        genres = entry.genres
    )
}
