package dev.alvr.katana.ui.base.components.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

@Composable
@ExperimentalPagerApi
internal fun LayerContent(
    tabsCount: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    scrollEnabled: Boolean = true,
    content: @Composable (Int) -> Unit,
) {
    HorizontalPager(
        modifier = modifier,
        count = tabsCount,
        state = pagerState,
        userScrollEnabled = scrollEnabled,
        content = { page -> content(page) },
    )
}
