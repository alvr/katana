package dev.alvr.katana.features.home.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiIntent

internal interface HomeIntent : UiIntent {
    data class SaveToken(val token: String) : HomeIntent

    sealed interface ForYouIntent : HomeIntent {
        data object CloseWelcomeCard : ForYouIntent

        data object NavigateToAnimeLists : ForYouIntent
        data object NavigateToMangaLists : ForYouIntent
        data object NavigateToTrending : ForYouIntent
        data object NavigateToPopular : ForYouIntent
        data object NavigateToUpcoming : ForYouIntent
    }

    sealed interface ActivityIntent : HomeIntent {
        companion object : ActivityIntent // TODO: remove when adding first intent
    }
}
