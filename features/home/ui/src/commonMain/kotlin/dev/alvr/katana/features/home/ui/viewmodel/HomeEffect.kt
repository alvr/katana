package dev.alvr.katana.features.home.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiEffect

internal interface HomeEffect : UiEffect {
    data object SaveTokenFailure : HomeEffect
    data object SaveUserIdFailure : HomeEffect
    data object ObserveSessionFailure : HomeEffect

    sealed interface ForYouEffect : HomeEffect {
        data object NavigateToAnimeLists : ForYouEffect
        data object NavigateToMangaLists : ForYouEffect
        data object NavigateToTrending : ForYouEffect
        data object NavigateToPopular : ForYouEffect
        data object NavigateToUpcoming : ForYouEffect
    }

    sealed interface ActivityEffect : HomeEffect
}
