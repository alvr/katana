package dev.alvr.katana.ui.base.components.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@ExperimentalPagerApi
@ExperimentalMaterialApi
fun <T : HomeTopAppBar> HomeScaffold(
    tabs: ImmutableList<T>,
    backContent: @Composable (T, BackLayerType) -> Unit,
    pageContent: @Composable (T) -> Unit,
    fab: (@Composable (T) -> Unit)? = null,
    onSelectedTab: (T) -> Unit = {},
) {
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    val (backLayerType, setBackLayerType) = remember { mutableStateOf(BackLayerType.NONE) }

    val changeBackLayerType = { newBackLayerType: BackLayerType ->
        scaffoldState.toggleBackdrop(
            scope = coroutineScope,
            current = backLayerType,
            new = newBackLayerType,
            update = setBackLayerType,
        )
    }

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = false,
        peekHeight = HEADER_SIZE.dp,
        appBar = {
            HomeTopAppBar(
                tabs = tabs,
                pagerState = pagerState,
                onSearch = { changeBackLayerType(BackLayerType.SEARCH) },
                onFilter = { changeBackLayerType(BackLayerType.FILTER) },
                onTabClicked = { page ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page)
                    }
                },
            )
        },
        backLayerContent = {
            LayerContent(
                tabsCount = tabs.size,
                pagerState = pagerState,
                scrollEnabled = false,
                content = { backContent(tabs[pagerState.currentPage], backLayerType) },
            )
        },
        frontLayerContent = {
            onSelectedTab(tabs[pagerState.currentPage])

            Scaffold(
                floatingActionButton = { fab?.invoke(tabs[pagerState.currentPage]) },
            ) {
                LayerContent(
                    modifier = Modifier.fillMaxSize().padding(it),
                    tabsCount = tabs.size,
                    pagerState = pagerState,
                    content = { page -> pageContent(tabs[page]) },
                )
            }
        },
        frontLayerShape = RectangleShape,
    )
}

@ExperimentalMaterialApi
private fun BackdropScaffoldState.toggleBackdrop(
    scope: CoroutineScope,
    current: BackLayerType,
    new: BackLayerType,
    update: (BackLayerType) -> Unit,
) {
    scope.launch {
        if (isConcealed) {
            reveal()
        } else if (current == new || new == BackLayerType.NONE) {
            conceal()
        }
    }

    update(new)
}
