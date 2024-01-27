package dev.alvr.katana.ui.lists.entities.item

import androidx.compose.runtime.Immutable
import com.arkivanov.essenty.parcelable.Parcelize
import korlibs.time.Date
import korlibs.time.DateTimeTz

@Immutable
@Parcelize
data class MangaListItem internal constructor(
    override val entryId: Int,
    override val mediaId: Int,
    override val title: String,
    override val score: Double,
    override val format: ItemMediaFormat,
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
    internal val volumesProgress: Int,
    internal val volumesTotal: Int?,
) : MediaListItem
