package dev.alvr.katana.ui.lists.entities

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.alvr.katana.common.core.orZero
import dev.alvr.katana.ui.lists.R
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.parcelize.Parcelize

@Immutable
@Suppress("ComplexInterface")
sealed interface MediaListItem : Parcelable {
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
    val startDate: LocalDate
    val endDate: LocalDate?
    val startedAt: LocalDate?
    val completedAt: LocalDate?
    val updatedAt: LocalDateTime?

    fun edit(
        score: Double = this.score,
        progress: Int = this.progress,
        repeat: Int = this.repeat,
        private: Boolean = this.private,
        notes: String = this.notes,
        hiddenFromStatusLists: Boolean = this.hiddenFromStatusLists,
        startedAt: LocalDate? = this.startedAt,
        completedAt: LocalDate? = this.completedAt,
        volumesProgress: Int = (this as? MangaListItem)?.volumesProgress.orZero()
    ) = when (this) {
        is AnimeListItem -> copy(
            score = score,
            progress = progress,
            repeat = repeat,
            private = private,
            notes = notes,
            hiddenFromStatusLists = hiddenFromStatusLists,
            startedAt = startedAt,
            completedAt = completedAt,
        )
        is MangaListItem -> copy(
            score = score,
            progress = progress,
            repeat = repeat,
            private = private,
            notes = notes,
            hiddenFromStatusLists = hiddenFromStatusLists,
            startedAt = startedAt,
            completedAt = completedAt,
            volumesProgress = volumesProgress,
        )
    }

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
        override val startDate: LocalDate,
        override val endDate: LocalDate?,
        override val startedAt: LocalDate?,
        override val completedAt: LocalDate?,
        override val updatedAt: LocalDateTime?,
        val nextEpisode: NextEpisode?,
    ) : MediaListItem {
        @Stable
        @Parcelize
        data class NextEpisode(
            val number: Int,
            val date: LocalDateTime,
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
        override val startDate: LocalDate,
        override val endDate: LocalDate?,
        override val startedAt: LocalDate?,
        override val completedAt: LocalDate?,
        override val updatedAt: LocalDateTime?,
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
