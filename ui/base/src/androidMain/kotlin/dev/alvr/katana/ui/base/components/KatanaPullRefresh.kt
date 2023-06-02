package dev.alvr.katana.ui.base.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
@ExperimentalMaterialApi
fun KatanaPullRefresh(
    loading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val refreshState = rememberPullRefreshState(loading, onRefresh)

    Box(modifier.pullRefresh(refreshState)) {
        content()

        PullRefreshIndicator(loading, refreshState, Modifier.align(Alignment.TopCenter))
    }
}
