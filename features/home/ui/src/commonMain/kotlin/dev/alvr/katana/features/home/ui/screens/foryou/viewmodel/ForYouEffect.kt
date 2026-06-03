package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiEffect

internal sealed interface ForYouEffect : UiEffect {
    data object NavigateToAnimeLists : ForYouEffect

    data object NavigateToMangaLists : ForYouEffect

    data object NavigateToTrending : ForYouEffect

    data object NavigateToPopular : ForYouEffect

    data object NavigateToUpcoming : ForYouEffect
}
