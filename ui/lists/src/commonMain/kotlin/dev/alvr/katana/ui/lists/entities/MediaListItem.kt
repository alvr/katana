package dev.alvr.katana.ui.lists.entities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.alvr.katana.ui.lists.lists.generated.resources.Res
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_movie
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_music
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_novel
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_ona
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_one_shot
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_ova
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_special
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_tv
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_tv_short
import dev.alvr.katana.ui.lists.lists.generated.resources.entry_format_unknown
import dev.alvr.katana.ui.lists.lists.generated.resources.manga_toolbar
import korlibs.time.Date
import korlibs.time.DateTimeTz
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

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

        @OptIn(ExperimentalResourceApi::class)
        val value
            @Composable get() = with(Res.string) {
                when (this@Format) {
                    Tv -> entry_format_tv
                    TvShort -> entry_format_tv_short
                    Movie -> entry_format_movie
                    Special -> entry_format_special
                    Ova -> entry_format_ova
                    Ona -> entry_format_ona
                    Music -> entry_format_music
                    Manga -> manga_toolbar
                    Novel -> entry_format_novel
                    OneShot -> entry_format_one_shot
                    Unknown -> entry_format_unknown
                }.let { res -> stringResource(res) }
            }
    }
}
