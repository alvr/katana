package dev.alvr.katana.core.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
)
fun KatanaPullRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val stateM2 = rememberPullRefreshState(refreshing, onRefresh)
    val stateM3 = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = modifier,
        state = stateM3,
        isRefreshing = refreshing,
        onRefresh = onRefresh,
        indicator = {
            PullRefreshIndicator(refreshing, stateM2, Modifier.align(Alignment.TopCenter))

            // TODO: Restore when stable version of M3 Expressive is released
            // LoadingIndicator(
            //     modifier = Modifier.align(Alignment.TopCenter),
            //     state = state,
            //     isRefreshing = refreshing,
            // )
        },
        content = content,
    )
}
