package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import androidx.compose.runtime.Immutable
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.core.ui.viewmodel.SectionStatus
import dev.alvr.katana.core.ui.viewmodel.UiState
import dev.alvr.katana.features.home.ui.screens.foryou.entities.HomeMediaItem
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class ForYouState(
    val showWelcomeCard: Boolean = false,
    val sessionActive: Boolean = false,
    val watching: SectionStatus<ImmutableList<HomeMediaItem>> = SectionStatus.Uninitialized,
    val reading: SectionStatus<ImmutableList<HomeMediaItem>> = SectionStatus.Uninitialized,
    val trending: SelectableMediaSectionState = SelectableMediaSectionState(),
    val popular: SelectableMediaSectionState = SelectableMediaSectionState(),
    val upcoming: SectionStatus<ImmutableList<HomeMediaItem>> = SectionStatus.Uninitialized,
) : UiState

@Immutable
internal data class SelectableMediaSectionState(
    val selectedType: MediaListType = MediaListType.Anime,
    val status: SectionStatus<ImmutableList<HomeMediaItem>> = SectionStatus.Uninitialized,
)
