package dev.alvr.katana.features.lists.ui.entities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.alvr.katana.features.lists.ui.strings.LocalListsStrings
import korlibs.time.Date
import korlibs.time.DateTimeTz

@Immutable
@Suppress("ComplexInterface")
internal sealed interface MediaListItem : Parcelable {
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
    val startedAt: Date?
    val completedAt: Date?
    val updatedAt: DateTimeTz?

    @Parcelize
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
        override val startedAt: Date?,
        override val completedAt: Date?,
        override val updatedAt: DateTimeTz?,
        val nextEpisode: NextEpisode?,
    ) : MediaListItem {
        @Stable
        @Parcelize
        data class NextEpisode(
            val number: Int,
            val date: DateTimeTz,
        ) : Parcelable
    }

    @Parcelize
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
        override val startedAt: Date?,
        override val completedAt: Date?,
        override val updatedAt: DateTimeTz?,
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
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.Tv -> entryFormatTv
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.TvShort -> entryFormatTvShort
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.Movie -> entryFormatMovie
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.Special -> entryFormatSpecial
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.Ova -> entryFormatOva
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.Ona -> entryFormatOna
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.Music -> entryFormatMusic
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.Manga -> entryFormatManga
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.Novel -> entryFormatNovel
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.OneShot -> entryFormatOneShot
                    dev.alvr.katana.features.lists.ui.entities.MediaListItem.Format.Unknown -> entryFormatUnknown
                }
            }
    }
}
