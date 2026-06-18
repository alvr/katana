package dev.alvr.katana.features.lists.ui.navigation

import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import dev.alvr.katana.common.media.domain.models.ItemEntryId
import dev.alvr.katana.core.ui.navigation.KatanaNavigator
import dev.alvr.katana.core.ui.navigation.overridden

interface MangaListsNavigator : KatanaNavigator {
    fun mangaEntryDetails(id: ItemEntryId)

    fun editMangaEntry(id: ItemEntryId)
}

private class KatanaMangaListsNavigator(override val navController: NavHostController) : MangaListsNavigator {
    override fun navigateBack() {
        overridden()
    }

    override fun mangaEntryDetails(id: ItemEntryId) {
        Logger.d(tag = LogTag) { "Entry details ${id.value}" }
    }

    override fun editMangaEntry(id: ItemEntryId) {
        Logger.d(tag = LogTag) { "Edit entry ${id.value}" }
    }
}

fun katanaMangaListsNavigator(navController: NavHostController): MangaListsNavigator =
    KatanaMangaListsNavigator(navController)

private const val LogTag = "MangaListsNavigator"
