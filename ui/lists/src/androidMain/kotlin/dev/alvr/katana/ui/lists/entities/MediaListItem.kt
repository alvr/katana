package dev.alvr.katana.ui.lists.entities

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.alvr.katana.ui.lists.R
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
    enum class Format(@StringRes val value: Int) {
        Tv(R.string.lists_entry_format_tv),
        TvShort(R.string.lists_entry_format_tv_short),
        Movie(R.string.lists_entry_format_movie),
        Special(R.string.lists_entry_format_special),
        Ova(R.string.lists_entry_format_ova),
        Ona(R.string.lists_entry_format_ona),
        Music(R.string.lists_entry_format_music),
        Manga(R.string.lists_entry_format_manga),
        Novel(R.string.lists_entry_format_novel),
        OneShot(R.string.lists_entry_format_one_shot),
        Unknown(R.string.lists_entry_format_unknown),
    }
}
