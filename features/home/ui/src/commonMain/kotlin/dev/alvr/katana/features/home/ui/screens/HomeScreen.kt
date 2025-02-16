package dev.alvr.katana.features.home.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import dev.alvr.katana.core.ui.components.KatanaScaffold
import dev.alvr.katana.core.ui.components.showSnackbar
import dev.alvr.katana.core.ui.resources.asPainter
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.utils.contentPadding
import dev.alvr.katana.core.ui.utils.noInsets
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.core.ui.viewmodel.collectEffect
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
import dev.alvr.katana.features.home.ui.viewmodel.HomeViewModel
import kotlinx.collections.immutable.toImmutableList

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(
    homeNavigator: HomeNavigator,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val tabs = remember { HomeTab.entries.toImmutableList() }
    val pagerState = rememberPagerState { HomeTab.entries.size }
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val currentTab by remember { derivedStateOf { pagerState.currentPage } }

    val uiState by viewModel.collectAsState()
    viewModel.collectEffect { effect ->
        when (effect) {
            HomeEffect.SaveTokenFailure -> snackbarHostState.showSnackbar(Res.string.error_save_token)
            HomeEffect.SaveUserIdFailure -> snackbarHostState.showSnackbar(Res.string.error_fetch_user_id)
            HomeEffect.ObserveSessionFailure -> snackbarHostState.showSnackbar(Res.string.error_observe_session)
            HomeEffect.ForYouEffect.NavigateToAnimeLists -> homeNavigator.navigateToAnimeLists()
            HomeEffect.ForYouEffect.NavigateToMangaLists -> homeNavigator.navigateToMangaLists()
            HomeEffect.ForYouEffect.NavigateToTrending -> homeNavigator.navigateToTrending()
            HomeEffect.ForYouEffect.NavigateToPopular -> homeNavigator.navigateToPopular()
            HomeEffect.ForYouEffect.NavigateToUpcoming -> homeNavigator.navigateToUpcoming()
            else -> effect.handleHomeEffect(homeNavigator)
        }
    }

    KatanaScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHostState = snackbarHostState,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Image(
                            modifier = Modifier.padding(vertical = 12.dp),
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
                            onClick = { pagerState.requestScrollToPage(tab.ordinal) },
                            text = { Text(text = tab.title.value) },
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets.noInsets,
    ) { paddingValues ->
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = pagerState,
            verticalAlignment = Alignment.Top,
            contentPadding = WindowInsets.contentPadding.asPaddingValues(),
            pageSpacing = 12.dp,
        ) { page ->
            when (page) {
                HomeTab.ForYou.ordinal -> ForYouTabContent(
                    sessionActive = uiState.sessionActive,
                    onIntent = viewModel::intent,
                    uiState = uiState.forYouTab,
                )
                HomeTab.Activity.ordinal -> ActivityTabContent(
                    sessionActive = uiState.sessionActive,
                    onEvent = viewModel::intent,
                    uiState = uiState.activityTab,
                )
            }
        }
    }
}

internal expect fun HomeEffect.handleHomeEffect(homeNavigator: HomeNavigator)
