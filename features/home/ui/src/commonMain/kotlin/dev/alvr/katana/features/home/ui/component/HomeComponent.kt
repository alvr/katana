package dev.alvr.katana.features.home.ui.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBarItem
import dev.alvr.katana.core.ui.decompose.AppComponentContext

sealed interface HomeComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onNavigationBarItemClicked(item: KatanaNavigationBarItem)

    sealed interface Child {
        data object AnimeListChild : Child
        data object MangaListChild : Child
        data object ExploreChild : Child
        data object SocialChild : Child
        data object AccountChild : Child
    }

    fun interface Factory {
        fun create(
            componentContext: AppComponentContext,
            navigateToLogin: () -> Unit,
        ): HomeComponent
    }
}
