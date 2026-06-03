package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiIntent

internal sealed interface ForYouIntent : UiIntent {
    data object CloseWelcomeCard : ForYouIntent

    data object NavigateToAnimeLists : ForYouIntent

    data object NavigateToMangaLists : ForYouIntent

    data object NavigateToTrending : ForYouIntent

    data object NavigateToPopular : ForYouIntent

    data object NavigateToUpcoming : ForYouIntent
}
