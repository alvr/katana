package dev.alvr.katana.features.home.ui.screens.foryou.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.theme.contentPaddingMedium
import dev.alvr.katana.core.ui.viewmodel.CollectEffect
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Popular
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Reading
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Trending
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Upcoming
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Watching
import dev.alvr.katana.features.home.ui.screens.foryou.sections.WelcomeCard
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouEffect
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouState
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
internal fun ForYouTabContent(
    navigator: HomeNavigator,
    sessionActive: Boolean,
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = metroViewModel(),
) {
    val uiState by viewModel.collectAsState()

    viewModel.CollectEffect { effect ->
        when (effect) {
            ForYouEffect.NavigateToAnimeLists -> navigator.navigateToAnimeLists()
            ForYouEffect.NavigateToMangaLists -> navigator.navigateToMangaLists()
            ForYouEffect.NavigateToTrending -> navigator.navigateToTrending()
            ForYouEffect.NavigateToPopular -> navigator.navigateToPopular()
            ForYouEffect.NavigateToUpcoming -> navigator.navigateToUpcoming()
        }
    }

    ForYouTab(
        uiState = uiState,
        sessionActive = sessionActive,
        onIntent = viewModel::intent,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun ForYouTab(
    uiState: ForYouState,
    sessionActive: Boolean,
    onIntent: (ForYouIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.contentPaddingMedium.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.itemSpacing),
    ) {
        if (!sessionActive && uiState.showWelcomeCard) {
            item(key = "welcome_card", contentType = ForYouItemContentType.WelcomeCard) {
                WelcomeCard(modifier = Modifier.animateItem(), onIntent = onIntent)
            }
        }

        if (sessionActive) {
            item(key = "watching", contentType = ForYouItemContentType.ListSection) {
                Watching(modifier = Modifier.animateItem(), onIntent = onIntent)
            }
            item(key = "reading", contentType = ForYouItemContentType.ListSection) {
                Reading(modifier = Modifier.animateItem(), onIntent = onIntent)
            }
        }

        item(key = "trending", contentType = ForYouItemContentType.ListSection) {
            Trending(modifier = Modifier.animateItem(), onIntent = onIntent)
        }
        item(key = "popular", contentType = ForYouItemContentType.ListSection) {
            Popular(modifier = Modifier.animateItem(), onIntent = onIntent)
        }
        item(key = "upcoming", contentType = ForYouItemContentType.ListSection) {
            Upcoming(modifier = Modifier.animateItem(), onIntent = onIntent)
        }
    }
}

private enum class ForYouItemContentType {
    WelcomeCard,
    ListSection,
}
