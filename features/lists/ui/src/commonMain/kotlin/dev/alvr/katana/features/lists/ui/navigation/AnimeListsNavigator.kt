package dev.alvr.katana.features.lists.ui.navigation

import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import dev.alvr.katana.core.ui.navigation.KatanaNavigator
import dev.alvr.katana.core.ui.navigation.overridden
import dev.alvr.katana.features.lists.domain.models.ItemEntryId

interface AnimeListsNavigator : KatanaNavigator {
    fun animeEntryDetails(id: ItemEntryId)

    fun editAnimeEntry(id: ItemEntryId)
}

private class KatanaAnimeListsNavigator(override val navController: NavHostController) : AnimeListsNavigator {
    override fun navigateBack() {
        overridden()
    }

    override fun animeEntryDetails(id: ItemEntryId) {
        Logger.d(LogTag) { "Entry details ${id.value}" }
    }

    override fun editAnimeEntry(id: ItemEntryId) {
        Logger.d(LogTag) { "Edit entry ${id.value}" }
    }
}

fun katanaAnimeListsNavigator(navController: NavHostController): AnimeListsNavigator =
    KatanaAnimeListsNavigator(navController)

private const val LogTag = "AnimeListsNavigator"
