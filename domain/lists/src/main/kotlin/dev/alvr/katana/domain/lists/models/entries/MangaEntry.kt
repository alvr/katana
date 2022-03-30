package dev.alvr.katana.domain.lists.models.entries

data class MangaEntry(
    val entry: MediaEntry,
    val chapters: Int?,
    val volumes: Int?,
) : MediaEntry(entry)
