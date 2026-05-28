package dev.alvr.katana.features.home.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreProvider
import dev.alvr.katana.core.ui.components.KatanaScaffold
import dev.alvr.katana.core.ui.components.snackbar.LocalSnackbarController
import dev.alvr.katana.core.ui.resources.asPainter
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.theme.noInsets
import dev.alvr.katana.core.ui.viewmodel.CollectEffect
import dev.alvr.katana.core.ui.viewmodel.collectUiStateWithLifecycle
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.error_fetch_user_id
import dev.alvr.katana.features.home.ui.resources.error_observe_session
import dev.alvr.katana.features.home.ui.resources.error_save_token
import dev.alvr.katana.features.home.ui.resources.katana_logo
import dev.alvr.katana.features.home.ui.resources.katana_logo_a11y
import dev.alvr.katana.features.home.ui.screens.activity.screens.ActivityTabContent
import dev.alvr.katana.features.home.ui.screens.foryou.screens.ForYouTabContent
import dev.alvr.katana.features.home.ui.viewmodel.HomeEffect
import dev.alvr.katana.features.home.ui.viewmodel.HomeViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Host composable for the Home screen that sets up tabs, pager state, collapsing top app bar,
 * collects UI state and side effects, and renders per-tab content.
 *
 * @param homeNavigator Navigator used for navigation actions originating from Home tab content.
 * @param viewModel Source of UI state and effects for the Home screen.
 * @param modifier Optional [Modifier] applied to the scaffold root.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(homeNavigator: HomeNavigator, viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    val snackbarController = LocalSnackbarController.current
    val tabs = remember { HomeTab.entries.toImmutableList() }
    val pagerState = rememberPagerState { HomeTab.entries.size }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val uiState by viewModel.collectUiStateWithLifecycle()

    viewModel.CollectEffect { effect ->
        when (effect) {
            HomeEffect.SaveTokenFailure -> snackbarController.showMessage(Res.string.error_save_token)
            HomeEffect.SaveUserIdFailure -> snackbarController.showMessage(Res.string.error_fetch_user_id)
            HomeEffect.ObserveSessionFailure -> snackbarController.showMessage(Res.string.error_observe_session)
        }
    }

    KatanaScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                scrollBehavior = scrollBehavior,
                currentTab = pagerState.currentPage,
                tabs = tabs,
                onTabClick = { tab -> pagerState.requestScrollToPage(tab.ordinal) },
            )
        },
        contentWindowInsets = WindowInsets.noInsets,
    ) { paddingValues ->
        PagerContent(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            navigator = homeNavigator,
            pagerState = pagerState,
            sessionActive = uiState.sessionActive,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    currentTab: Int,
    tabs: ImmutableList<HomeTab>,
    onTabClick: (HomeTab) -> Unit,
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Image(
                    modifier = Modifier.padding(vertical = KatanaTheme.dimensions.spacing3),
                    painter = Res.drawable.katana_logo.asPainter,
                    contentDescription = Res.string.katana_logo_a11y.value,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            scrollBehavior = scrollBehavior,
        )

        PrimaryTabRow(selectedTabIndex = currentTab) {
            tabs.fastForEach { tab ->
                Tab(
                    selected = currentTab == tab.ordinal,
                    onClick = { onTabClick(tab) },
                    text = { Text(text = tab.title.value) },
                )
            }
        }
    }
}

/**
 * Displays horizontally swipeable home pages and scopes a distinct ViewModel store to each page.
 *
 * Each page is rendered using the provided `navigator` and `sessionActive` flag; a separate
 * ViewModelStoreOwner is created per page so page-scoped ViewModels are retained independently.
 *
 * @param navigator Navigator used by page content to perform navigation from within tabs.
 * @param sessionActive `true` if a user session is active; forwarded to page content for conditional UI.
 * @param pagerState State object that controls and observes the pager's current page and animations.
 * @param modifier Optional layout modifiers applied to the pager container.
 */
@Composable
private fun PagerContent(
    navigator: HomeNavigator,
    sessionActive: Boolean,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val storeProvider = rememberViewModelStoreProvider()

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        verticalAlignment = Alignment.Top,
        pageSpacing = KatanaTheme.dimensions.pageSpacing,
    ) { page ->
        val owner = rememberViewModelStoreOwner(key = page, provider = storeProvider)

        CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
            when (page) {
                HomeTab.ForYou.ordinal -> ForYouTabContent(navigator = navigator, sessionActive = sessionActive)
                HomeTab.Activity.ordinal -> ActivityTabContent(navigator = navigator, sessionActive = sessionActive)
            }
        }
    }
}
