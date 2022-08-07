package dev.alvr.katana.ui.base.components.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.ui.base.R

@Immutable
interface HomeTopAppBar {
    @get:StringRes
    val label: Int
}

val LocalHomeTopBarSubtitle = compositionLocalOf<String?> { String.empty }

@Composable
@ExperimentalPagerApi
internal fun HomeTopAppBar(
    tabs: Array<out HomeTopAppBar>,
    pagerState: PagerState,
    onTabClicked: (Int) -> Unit,
    onSearch: () -> Unit,
    onFilter: () -> Unit,
) {
    Column(modifier = Modifier.height(HEADER_SIZE.dp)) {
        TopAppBar(
            title = {
                Column {
                    Text(text = stringResource(tabs[pagerState.currentPage].label))
                    with(LocalHomeTopBarSubtitle.current) {
                        if (!isNullOrEmpty()) {
                            Text(text = this, style = MaterialTheme.typography.caption)
                        }
                    }
                }
            },
            actions = {
                IconButton(onClick = onSearch) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = stringResource(R.string.menu_search),
                    )
                }
                IconButton(onClick = onFilter) {
                    Icon(
                        imageVector = Icons.Outlined.FilterAlt,
                        contentDescription = stringResource(R.string.menu_filter),
                    )
                }
            },
            elevation = Int.zero.dp,
        )
        TabSelector(
            tabs = tabs,
            pagerState = pagerState,
            onTabClicked = onTabClicked,
        )
    }
}

@Composable
@ExperimentalPagerApi
private fun TabSelector(
    tabs: Array<out HomeTopAppBar>,
    pagerState: PagerState,
    onTabClicked: (Int) -> Unit,
) {
    val tabList = @Composable {
        tabs.forEachIndexed { index, tab ->
            Tab(
                text = { Text(text = stringResource(tab.label)) },
                modifier = Modifier.fillMaxHeight(),
                selected = index == pagerState.currentPage,
                onClick = { onTabClicked(index) },
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
                        topStart = IndicatorRadius,
                    ),
                ),
            height = 4.dp,
        )
    }

    if (tabs.size > MAX_FIXED_TABS) {
        ScrollableTabRow(
            modifier = tabRowModifier,
            selectedTabIndex = pagerState.currentPage,
            indicator = tabIndicator,
            edgePadding = Int.zero.dp,
            tabs = tabList,
        )
    } else {
        TabRow(
            modifier = tabRowModifier,
            selectedTabIndex = pagerState.currentPage,
            indicator = tabIndicator,
            tabs = tabList,
        )
    }
}

private const val MAX_FIXED_TABS = 4
private val IndicatorRadius = 24.dp
