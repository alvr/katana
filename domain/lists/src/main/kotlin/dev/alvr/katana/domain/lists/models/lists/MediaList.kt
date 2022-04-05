package dev.alvr.katana.domain.lists.models.lists

import dev.alvr.katana.domain.lists.models.entries.MediaEntry

data class MediaList<out T : MediaEntry>(
    val name: String,
    val listType: ListType,
    val entries: List<MediaListEntry<T>>,
) {
    enum class ListType(val listName: String) {
        WATCHING("Watching"),
        READING("Reading"),

        REWATCHING("Rewatching"),
        REREADING("Rereading"),

        COMPLETED_TV("Completed TV"),
        COMPLETED_MOVIE("Completed Movie"),
        COMPLETED_OVA("Completed OVA"),
        COMPLETED_ONA("Completed ONA"),
        COMPLETED_TV_SHORT("Completed TV Short"),
        COMPLETED_SPECIAL("Completed Special"),
        COMPLETED_MUSIC("Completed Mang"),
        COMPLETED_MANGA("Completed Manga"),
        COMPLETED_NOVEL("Completed Novel"),
        COMPLETED_ONE_SHOT("Completed One Shot"),

        PAUSED("Paused"),
        DROPPED("Dropped"),
        PLANNING("Planning"),

        CUSTOM("Custom");

        companion object {
            fun of(name: String?) = values().find {
                it.listName.lowercase() == name?.lowercase()
            } ?: CUSTOM
        }
    }
}
