package dev.alvr.katana.features.lists.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import dev.alvr.katana.features.lists.ui.screens.AnimeScreen
import dev.alvr.katana.features.lists.ui.screens.MangaScreen

fun NavGraphBuilder.lists(animeListsNavigator: AnimeListsNavigator, mangaListsNavigator: MangaListsNavigator) {
    animeLists(animeListsNavigator)
    mangaLists(mangaListsNavigator)
}

private fun NavGraphBuilder.animeLists(animeListsNavigator: AnimeListsNavigator) {
    navigation<AnimeListsDestination.Root>(startDestination = AnimeListsDestination.Lists) {
        composable<AnimeListsDestination.Lists> { AnimeScreen(animeListsNavigator) }
    }
}

private fun NavGraphBuilder.mangaLists(mangaListsNavigator: MangaListsNavigator) {
    navigation<MangaListsDestination.Root>(startDestination = MangaListsDestination.Lists) {
        composable<MangaListsDestination.Lists> { MangaScreen(mangaListsNavigator) }
    }
}
