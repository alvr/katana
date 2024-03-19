package dev.alvr.katana.features.home.ui.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBarItem
import dev.alvr.katana.core.ui.decompose.AppComponentContext
import dev.alvr.katana.features.home.ui.navigation.HomeNavigationBar
import kotlinx.serialization.Serializable

internal class DefaultHomeComponent private constructor(
    componentContext: AppComponentContext,
    private val navigateToLogin: () -> Unit,
) : HomeComponent, AppComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.AnimeList,
        childFactory = ::childFactory,
    )

    override fun onNavigationBarItemClicked(item: KatanaNavigationBarItem) {
        when (item) {
            HomeNavigationBar.AnimeList -> onAnimeListItemClicked()
            HomeNavigationBar.MangaList -> onMangaListItemClicked()
            HomeNavigationBar.Explore -> onExploreItemClicked()
            HomeNavigationBar.Social -> onSocialItemClicked()
            HomeNavigationBar.Account -> onAccountItemClicked()
        }
    }

    private fun childFactory(
        config: Config,
        componentContext: AppComponentContext,
    ) = when (config) {
        Config.AnimeList -> HomeComponent.Child.AnimeListChild
        Config.MangaList -> HomeComponent.Child.MangaListChild
        Config.Explore -> HomeComponent.Child.ExploreChild
        Config.Social -> HomeComponent.Child.SocialChild
        Config.Account -> HomeComponent.Child.AccountChild
    }

    private fun onAnimeListItemClicked() {
        navigation.bringToFront(Config.AnimeList)
    }

    private fun onMangaListItemClicked() {
        navigation.bringToFront(Config.MangaList)
    }

    private fun onExploreItemClicked() {
        navigation.bringToFront(Config.Explore)
    }

    private fun onSocialItemClicked() {
        navigation.bringToFront(Config.Social)
    }

    private fun onAccountItemClicked() {
        navigation.bringToFront(Config.Account)
    }

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

    internal class Factory: HomeComponent.Factory {
        override fun invoke(
            componentContext: AppComponentContext,
            navigateToLogin: () -> Unit
        ) = DefaultHomeComponent(componentContext, navigateToLogin)
    }
}
