package dev.alvr.katana.features.lists.ui.entities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import dev.alvr.katana.common.media.domain.models.ItemEntryId
import dev.alvr.katana.common.media.domain.models.ItemMediaId
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.entry_format_manga
import dev.alvr.katana.features.lists.ui.resources.entry_format_movie
import dev.alvr.katana.features.lists.ui.resources.entry_format_music
import dev.alvr.katana.features.lists.ui.resources.entry_format_novel
import dev.alvr.katana.features.lists.ui.resources.entry_format_ona
import dev.alvr.katana.features.lists.ui.resources.entry_format_one_shot
import dev.alvr.katana.features.lists.ui.resources.entry_format_ova
import dev.alvr.katana.features.lists.ui.resources.entry_format_special
import dev.alvr.katana.features.lists.ui.resources.entry_format_tv
import dev.alvr.katana.features.lists.ui.resources.entry_format_tv_short
import dev.alvr.katana.features.lists.ui.resources.entry_format_unknown
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Immutable
@Suppress("ComplexInterface")
internal sealed interface MediaListItem {
    val entryId: ItemEntryId
    val mediaId: ItemMediaId
    val title: String
    val score: Float?
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
        override val entryId: ItemEntryId,
        override val mediaId: ItemMediaId,
        override val title: String,
        override val score: Float?,
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
        @Immutable data class NextEpisode(val number: Int, val date: LocalDateTime)
    }

    data class MangaListItem(
        override val entryId: ItemEntryId,
        override val mediaId: ItemMediaId,
        override val title: String,
        override val score: Float?,
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

        val text
            @Composable
            get() =
                when (this) {
                    Tv -> Res.string.entry_format_tv
                    TvShort -> Res.string.entry_format_tv_short
                    Movie -> Res.string.entry_format_movie
                    Special -> Res.string.entry_format_special
                    Ova -> Res.string.entry_format_ova
                    Ona -> Res.string.entry_format_ona
                    Music -> Res.string.entry_format_music
                    Manga -> Res.string.entry_format_manga
                    Novel -> Res.string.entry_format_novel
                    OneShot -> Res.string.entry_format_one_shot
                    Unknown -> Res.string.entry_format_unknown
                }.value
    }
}
