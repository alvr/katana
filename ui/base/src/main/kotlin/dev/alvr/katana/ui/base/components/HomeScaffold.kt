package dev.alvr.katana.ui.base.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@ExperimentalPagerApi
@ExperimentalMaterialApi
fun <P : HomeTopAppBar> HomeScaffold(
    tabs: Array<out P>,
    backContent: @Composable () -> Unit,
    pageContent: @Composable (P) -> Unit,
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val pagerState = rememberPagerState()

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = false,
        persistentAppBar = true,
        stickyFrontLayer = false,
        appBar = {
            HomeTopAppBar(
                pagerState = pagerState,
                tabs = tabs
            ) { page ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page)
                }
            }
        },
        backLayerContent = backContent,
        frontLayerContent = {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                count = tabs.size,
                state = pagerState,
            ) { page -> pageContent(tabs[page]) }
        },
        frontLayerShape = RectangleShape
    )
}
