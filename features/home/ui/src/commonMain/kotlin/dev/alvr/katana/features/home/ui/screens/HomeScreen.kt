package dev.alvr.katana.features.home.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.util.fastForEach
import dev.alvr.katana.core.ui.components.KatanaScaffold
import dev.alvr.katana.core.ui.components.snackbar.LocalSnackbarController
import dev.alvr.katana.core.ui.resources.asPainter
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.theme.noInsets
import dev.alvr.katana.core.ui.viewmodel.CollectEffect
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.error_fetch_user_id
import dev.alvr.katana.features.home.ui.resources.error_observe_session
import dev.alvr.katana.features.home.ui.resources.error_save_token
import dev.alvr.katana.features.home.ui.resources.katana_logo
import dev.alvr.katana.features.home.ui.resources.katana_logo_a11y
import dev.alvr.katana.features.home.ui.screens.activity.ActivityTabContent
import dev.alvr.katana.features.home.ui.screens.foryou.ForYouTabContent
import dev.alvr.katana.features.home.ui.viewmodel.HomeEffect
import dev.alvr.katana.features.home.ui.viewmodel.HomeIntent
import dev.alvr.katana.features.home.ui.viewmodel.HomeState
import dev.alvr.katana.features.home.ui.viewmodel.HomeViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(homeNavigator: HomeNavigator, viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    val snackbarController = LocalSnackbarController.current
    val tabs = remember { HomeTab.entries.toImmutableList() }
    val pagerState = rememberPagerState { HomeTab.entries.size }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val uiState by viewModel.collectAsState()

    viewModel.CollectEffect { effect ->
        when (effect) {
            HomeEffect.SaveTokenFailure -> snackbarController.showMessage(Res.string.error_save_token)
            HomeEffect.SaveUserIdFailure -> snackbarController.showMessage(Res.string.error_fetch_user_id)
            HomeEffect.ObserveSessionFailure -> snackbarController.showMessage(Res.string.error_observe_session)
            HomeEffect.ForYouEffect.NavigateToAnimeLists -> homeNavigator.navigateToAnimeLists()
            HomeEffect.ForYouEffect.NavigateToMangaLists -> homeNavigator.navigateToMangaLists()
            HomeEffect.ForYouEffect.NavigateToTrending -> homeNavigator.navigateToTrending()
            HomeEffect.ForYouEffect.NavigateToPopular -> homeNavigator.navigateToPopular()
            HomeEffect.ForYouEffect.NavigateToUpcoming -> homeNavigator.navigateToUpcoming()
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
            paddingValues = paddingValues,
            pagerState = pagerState,
            sessionActive = uiState.sessionActive,
            forYouState = uiState.forYouTab,
            activityState = uiState.activityTab,
            onIntent = viewModel::intent,
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

@Composable
private fun PagerContent(
    paddingValues: PaddingValues,
    pagerState: PagerState,
    sessionActive: Boolean,
    forYouState: HomeState.ForYouTabState,
    activityState: HomeState.ActivityTabState,
    onIntent: (HomeIntent) -> Unit,
) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        state = pagerState,
        verticalAlignment = Alignment.Top,
        pageSpacing = KatanaTheme.dimensions.pageSpacing,
    ) { page ->
        when (page) {
            HomeTab.ForYou.ordinal ->
                ForYouTabContent(sessionActive = sessionActive, onIntent = onIntent, uiState = forYouState)

            HomeTab.Activity.ordinal ->
                ActivityTabContent(sessionActive = sessionActive, onIntent = onIntent, uiState = activityState)
        }
    }
}
