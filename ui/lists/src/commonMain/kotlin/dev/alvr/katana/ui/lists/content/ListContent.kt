package dev.alvr.katana.ui.lists.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.alvr.katana.ui.base.components.KatanaEmptyState
import dev.alvr.katana.ui.base.components.KatanaErrorState
import dev.alvr.katana.ui.base.components.home.KatanaHomeScaffold
import dev.alvr.katana.ui.base.components.home.rememberKatanaHomeScaffoldState
import dev.alvr.katana.ui.base.decompose.extensions.subscribeChildSlot
import dev.alvr.katana.ui.base.decompose.state.collectAsState
import dev.alvr.katana.ui.lists.component.AnimeListComponent
import dev.alvr.katana.ui.lists.component.BaseListComponent
import dev.alvr.katana.ui.lists.component.DefaultBaseListComponent.Config.BottomSheetListSelector
import dev.alvr.katana.ui.lists.component.MangaListComponent
import dev.alvr.katana.ui.lists.content.modules.ChangeListButton
import dev.alvr.katana.ui.lists.content.modules.ChangeListSheet
import dev.alvr.katana.ui.lists.content.modules.MediaList
import dev.alvr.katana.ui.lists.strings.LocalListsStrings
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
internal fun ListContent(
    component: BaseListComponent<*>,
    title: String,
    emptyStateRes: String,
    backContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by component.collectAsState()
    val bottomSheet by component.bottomSheet.subscribeAsState()
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState()

    coroutineScope.launch {
        if (bottomSheet.child?.instance == null) {
            modalBottomSheetState.hide()
        } else {
            modalBottomSheetState.show()
        }
    }

    component.bottomSheet.subscribeChildSlot { config ->
        when (config) {
            is BottomSheetListSelector -> ChangeListSheet(
                lists = config.lists,
                selectedList = config.current,
                onResult = component::selectList,
                onDismiss = component::dismissListSelector,
            )
        }
    }

    val katanaScaffoldState = rememberKatanaHomeScaffoldState()
    val lazyGridState = rememberLazyGridState()
    val strings = LocalListsStrings.current

    val searchPlaceholder = when (component) {
        is AnimeListComponent -> strings.animeToolbarSearchPlaceholder
        is MangaListComponent -> strings.mangaToolbarSearchPlaceholder
    }

    val buttonsVisible by remember(state.isError) {
        derivedStateOf(structuralEqualityPolicy()) { !state.isError }
    }
    katanaScaffoldState.showTopAppBarActions = buttonsVisible

    KatanaHomeScaffold(
        katanaScaffoldState = katanaScaffoldState,
        title = title,
        subtitle = state.name,
        searchPlaceholder = searchPlaceholder,
        onSearch = component::search,
        backContent = backContent,
        fab = {
            ChangeListButton(visible = buttonsVisible && component.userLists.isNotEmpty()) {
                component.showListSelectorBottomSheet(component.userLists, state.name.orEmpty())
            }
        },
    ) { paddingValues ->
        with(state) {
            when {
                isError -> KatanaErrorState(
                    modifier = modifier.padding(paddingValues),
                    text = strings.errorMessage,
                    onRetry = {
                        component.refreshList()
                        katanaScaffoldState.resetToolbar()
                    },
                    buttonText = strings.errorRetryButton,
                    loading = state.isLoading,
                )

                isEmpty && !isLoading -> KatanaEmptyState(
                    modifier = modifier.padding(paddingValues),
                    text = emptyStateRes,
                )

                else -> MediaList(
                    lazyGridState = lazyGridState,
                    modifier = modifier.padding(paddingValues),
                    listState = state,
                    onRefresh = component::refreshList,
                    onAddPlusOne = component::addPlusOne,
                    onEditEntry = { TODO() },
                    onEntryDetails = { TODO() },
                )
            }
        }
    }
}
