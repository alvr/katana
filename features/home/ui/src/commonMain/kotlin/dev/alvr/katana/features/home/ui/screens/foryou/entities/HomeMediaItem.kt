package dev.alvr.katana.features.home.ui.screens.foryou.entities

import androidx.compose.runtime.Immutable
import dev.alvr.katana.common.media.domain.models.ItemMediaId
import dev.alvr.katana.common.media.domain.models.lists.MediaListType

@Immutable
internal data class HomeMediaItem(
    val id: ItemMediaId,
    val title: String,
    val cover: String,
    val type: MediaListType,
    val progress: Int? = null,
    val total: Int? = null,
)
