package dev.alvr.katana.ui.lists.entities

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import dev.alvr.katana.ui.lists.R
import java.time.LocalDateTime

@Immutable
internal sealed interface MediaListItem {
    val entryId: Int
    val mediaId: Int
    val title: String
    val score: Double
    val format: Format
    val cover: String
    val progress: Int
    val total: Int?
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
        override val updatedAt: LocalDateTime?,
        val volumesProgress: Int,
        val volumesTotal: Int?,
    ) : MediaListItem

    @Immutable
    enum class Format(@StringRes val value: Int) {
        Tv(R.string.entry_format_tv),
        TvShort(R.string.entry_format_tv_short),
        Movie(R.string.entry_format_movie),
        Special(R.string.entry_format_special),
        Ova(R.string.entry_format_ova),
        Ona(R.string.entry_format_ona),
        Music(R.string.entry_format_music),
        Manga(R.string.entry_format_manga),
        Novel(R.string.entry_format_novel),
        OneShot(R.string.entry_format_one_shot),
        Unknown(R.string.entry_format_unknown),
    }
}
