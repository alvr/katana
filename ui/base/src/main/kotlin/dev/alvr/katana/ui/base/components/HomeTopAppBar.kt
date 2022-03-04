package dev.alvr.katana.ui.base.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset

@Stable
@Immutable
interface HomeTopAppBar {
    @get:StringRes
    val label: Int
}

@Composable
@ExperimentalPagerApi
internal fun HomeTopAppBar(
    tabs: Array<out HomeTopAppBar>,
    pagerState: PagerState,
    onTabClicked: (Int) -> Unit
) {
    TopAppBar {
        TabSelector(
            tabs = tabs,
            pagerState = pagerState,
            onTabClicked = onTabClicked
        )
    }
}

@Composable
@ExperimentalPagerApi
private fun TabSelector(
    tabs: Array<out HomeTopAppBar>,
    pagerState: PagerState,
    onTabClicked: (Int) -> Unit
) {
    val tabList = @Composable {
        tabs.forEachIndexed { index, tab ->
            Tab(
                text = { Text(text = stringResource(id = tab.label)) },
                modifier = Modifier.fillMaxHeight(),
                selected = index == pagerState.currentPage,
                onClick = { onTabClicked(index) }
            )
        }
    }

    val tabRowModifier = Modifier.fillMaxWidth()
    val tabIndicator = @Composable { tabPositions: List<TabPosition> ->
        TabRowDefaults.Indicator(
            modifier = Modifier
                .pagerTabIndicatorOffset(pagerState, tabPositions)
                .clip(
                    RoundedCornerShape(
                        topEnd = IndicatorRadius,
                        topStart = IndicatorRadius
                    )
                ),
            height = 4.dp
        )
    }

    if (tabs.size > MAX_FIXED_TABS) {
        ScrollableTabRow(
            modifier = tabRowModifier,
            selectedTabIndex = pagerState.currentPage,
            indicator = tabIndicator,
            edgePadding = 0.dp,
            tabs = tabList
        )
    } else {
        TabRow(
            modifier = tabRowModifier,
            selectedTabIndex = pagerState.currentPage,
            indicator = tabIndicator,
            tabs = tabList
        )
    }
}

private const val MAX_FIXED_TABS = 4
private val IndicatorRadius = 24.dp
