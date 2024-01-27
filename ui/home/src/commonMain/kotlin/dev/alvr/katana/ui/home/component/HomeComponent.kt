package dev.alvr.katana.ui.home.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.alvr.katana.ui.account.component.AccountComponent
import dev.alvr.katana.ui.base.components.navigation.KatanaNavigationBarItem
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.explore.component.ExploreComponent
import dev.alvr.katana.ui.lists.component.AnimeListComponent
import dev.alvr.katana.ui.lists.component.MangaListComponent
import dev.alvr.katana.ui.social.component.SocialComponent

sealed interface HomeComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onNavigationBarItemClicked(item: KatanaNavigationBarItem)

    sealed interface Child {
        class AnimeListChild(val component: AnimeListComponent) : Child
        class MangaListChild(val component: MangaListComponent) : Child
        class ExploreChild(val component: ExploreComponent) : Child
        class SocialChild(val component: SocialComponent) : Child
        class AccountChild(val component: AccountComponent) : Child
    }
}

fun AppComponentContext.createHomeComponent(
    navigateToLogin: () -> Unit,
): HomeComponent = DefaultHomeComponent(
    componentContext = this,
    navigateToLogin = navigateToLogin,
)
