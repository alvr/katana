package dev.alvr.katana.ui.home.component

import com.arkivanov.decompose.router.stack.StackNavigation
import dev.alvr.katana.ui.account.component.createAccountComponent
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.base.decompose.appChildStack
import dev.alvr.katana.ui.explore.component.createExploreComponent
import dev.alvr.katana.ui.home.component.HomeComponent.Child.AccountChild
import dev.alvr.katana.ui.home.component.HomeComponent.Child.AnimeListChild
import dev.alvr.katana.ui.home.component.HomeComponent.Child.ExploreChild
import dev.alvr.katana.ui.home.component.HomeComponent.Child.MangaListChild
import dev.alvr.katana.ui.home.component.HomeComponent.Child.SocialChild
import dev.alvr.katana.ui.lists.component.anime.createAnimeListComponent
import dev.alvr.katana.ui.lists.component.manga.createMangaListComponent
import dev.alvr.katana.ui.social.component.createSocialComponent
import kotlinx.serialization.Serializable

internal class DefaultHomeComponent(
    componentContext: AppComponentContext,
) : HomeComponent, AppComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack = appChildStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.AnimeList,
        childFactory = ::childFactory,
    )

    private fun childFactory(
        config: Config,
        componentContext: AppComponentContext,
    ) = when (config) {
        Config.AnimeList -> componentContext.animeListChildFactory()
        Config.MangaList -> componentContext.mangaListChildFactory()
        Config.Explore -> componentContext.exploreChildFactory()
        Config.Social -> componentContext.socialChildFactory()
        Config.Account -> componentContext.accountChildFactory()
    }

    private fun AppComponentContext.animeListChildFactory() =
        AnimeListChild(createAnimeListComponent())

    private fun AppComponentContext.mangaListChildFactory() =
        MangaListChild(createMangaListComponent())

    private fun AppComponentContext.exploreChildFactory() =
        ExploreChild(createExploreComponent())

    private fun AppComponentContext.socialChildFactory() =
        SocialChild(createSocialComponent())

    private fun AppComponentContext.accountChildFactory() =
        AccountChild(createAccountComponent())

    @Serializable
    internal sealed interface Config {
        @Serializable
        data object AnimeList : Config

        @Serializable
        data object MangaList : Config

        @Serializable
        data object Explore : Config

        @Serializable
        data object Social : Config

        @Serializable
        data object Account : Config
    }
}
