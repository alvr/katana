package dev.alvr.katana.features.lists.ui.entities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import dev.alvr.katana.features.lists.ui.strings.LocalListsStrings
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Immutable
@Suppress("ComplexInterface")
internal sealed interface MediaListItem {
    val entryId: Int
    val mediaId: Int
    val title: String
    val score: Double
    val format: Format
    val cover: String
    val progress: Int
    val total: Int?
    val repeat: Int
    val private: Boolean
    val notes: String
    val hiddenFromStatusLists: Boolean
    val startedAt: LocalDate?
    val completedAt: LocalDate?
    val updatedAt: LocalDateTime?

    data class AnimeListItem(
        override val entryId: Int,
        override val mediaId: Int,
        override val title: String,
        override val score: Double,
        override val format: Format,
        override val cover: String,
        override val progress: Int,
        override val total: Int?,
        override val repeat: Int,
        override val private: Boolean,
        override val notes: String,
        override val hiddenFromStatusLists: Boolean,
        override val startedAt: LocalDate?,
        override val completedAt: LocalDate?,
        override val updatedAt: LocalDateTime?,
        val nextEpisode: NextEpisode?,
    ) : MediaListItem {
        data class NextEpisode(
            val number: Int,
            val date: LocalDateTime,
        )
    }

    data class MangaListItem(
        override val entryId: Int,
        override val mediaId: Int,
        override val title: String,
        override val score: Double,
        override val format: Format,
        override val cover: String,
        override val progress: Int,
        override val total: Int?,
        override val repeat: Int,
        override val private: Boolean,
        override val notes: String,
        override val hiddenFromStatusLists: Boolean,
        override val startedAt: LocalDate?,
        override val completedAt: LocalDate?,
        override val updatedAt: LocalDateTime?,
        val volumesProgress: Int,
        val volumesTotal: Int?,
    ) : MediaListItem

    @Immutable
    enum class Format {
        Tv,
        TvShort,
        Movie,
        Special,
        Ova,
        Ona,
        Music,
        Manga,
        Novel,
        OneShot,
        Unknown;

        val value
            @Composable get() = with(LocalListsStrings.current) {
                when (this@Format) {
                    Tv -> entryFormatTv
                    TvShort -> entryFormatTvShort
                    Movie -> entryFormatMovie
                    Special -> entryFormatSpecial
                    Ova -> entryFormatOva
                    Ona -> entryFormatOna
                    Music -> entryFormatMusic
                    Manga -> entryFormatManga
                    Novel -> entryFormatNovel
                    OneShot -> entryFormatOneShot
                    Unknown -> entryFormatUnknown
                }
            }
    }
}
