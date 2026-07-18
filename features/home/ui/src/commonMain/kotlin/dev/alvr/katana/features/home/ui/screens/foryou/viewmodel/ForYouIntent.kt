package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.core.ui.viewmodel.UiIntent

internal sealed interface ForYouIntent : UiIntent {
    data object CloseWelcomeCard : ForYouIntent

    data class SessionChanged(val active: Boolean) : ForYouIntent

    data class SelectTrendingType(val type: MediaListType) : ForYouIntent

    data class SelectPopularType(val type: MediaListType) : ForYouIntent

    data object RetryWatching : ForYouIntent

    data object RetryReading : ForYouIntent

    data object RetryTrending : ForYouIntent

    data object RetryPopular : ForYouIntent

    data object RetryUpcoming : ForYouIntent

    data object NavigateToAnimeLists : ForYouIntent

    data object NavigateToMangaLists : ForYouIntent

    data object NavigateToTrending : ForYouIntent

    data object NavigateToPopular : ForYouIntent

    data object NavigateToUpcoming : ForYouIntent
}
