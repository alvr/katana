package dev.alvr.katana.ui.base.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
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
    val shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)

    TopAppBar {
        Box {
            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions).clip(shape),
                        height = 4.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        text = { Text(text = stringResource(id = tab.label)) },
                        modifier = Modifier.fillMaxHeight(),
                        selected = index == pagerState.currentPage,
                        onClick = { onTabClicked(index) }
                    )
                }
            }
        }
    }
}
