package dev.alvr.katana.ui.home.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.alvr.katana.ui.account.component.AccountComponent
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.explore.component.ExploreComponent
import dev.alvr.katana.ui.lists.component.anime.AnimeListComponent
import dev.alvr.katana.ui.lists.component.manga.MangaListComponent
import dev.alvr.katana.ui.social.component.SocialComponent

sealed interface HomeComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class AnimeListChild(val component: AnimeListComponent) : Child
        class MangaListChild(val component: MangaListComponent) : Child
        class ExploreChild(val component: ExploreComponent) : Child
        class SocialChild(val component: SocialComponent) : Child
        class AccountChild(val component: AccountComponent) : Child
    }
}

fun AppComponentContext.createHomeComponent(): HomeComponent = DefaultHomeComponent(
    componentContext = this,
)
