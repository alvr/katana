package dev.alvr.katana.ui.base.components.home

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.ui.base.R
import dev.alvr.katana.ui.base.components.KatanaSearchTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun KatanaHomeScaffold(
    @StringRes title: Int,
    subtitle: String? = null,
    search: String,
    onSearch: (String) -> Unit,
    backContent: @Composable () -> Unit,
    fab: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = false,
        appBar = {
            TopAppBar(
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
                title = title,
                subtitle = subtitle,
                search = search,
                onSearch = onSearch,
            )
        },
        backLayerContent = backContent,
        frontLayerShape = RectangleShape,
        frontLayerContent = {
            Scaffold(
                floatingActionButton = fab,
                content = content,
            )
        },
    )
}

@Composable
@ExperimentalMaterialApi
@ExperimentalAnimationApi
private fun TopAppBar(
    scaffoldState: BackdropScaffoldState,
    coroutineScope: CoroutineScope,
    @StringRes title: Int,
    subtitle: String? = null,
    search: String,
    onSearch: (String) -> Unit,
) {
    var topAppBarStyle by rememberSaveable { mutableStateOf(TopAppBarStyle.Normal) }

    Surface {
        AnimatedContent(
            targetState = topAppBarStyle,
            transitionSpec = {
                fadeIn(tween(ANIMATION_MILLIS, easing = EaseIn)) with
                    fadeOut(tween(ANIMATION_MILLIS, easing = EaseOut)) using
                    SizeTransform(
                        clip = false,
                        sizeAnimationSpec = { _, _ ->
                            tween(ANIMATION_MILLIS, easing = EaseInOut)
                        },
                    )
            },
        ) { targetState ->
            when (targetState) {
                TopAppBarStyle.Normal -> KatanaHomeTopAppBar(
                    title = stringResource(title),
                    subtitle = subtitle,
                    onSearch = { topAppBarStyle = TopAppBarStyle.Search },
                    onFilter = { scaffoldState.toggleBackdrop(coroutineScope) },
                )
                TopAppBarStyle.Search -> KatanaSearchTopAppBar(
                    search = search,
                    onValueChange = onSearch,
                    placeholder = R.string.toolbar_search_clear,
                    onBack = {
                        topAppBarStyle = TopAppBarStyle.Normal
                        onSearch(String.empty)
                    },
                    onClear = { onSearch(String.empty) },
                ).also {
                    coroutineScope.launch { scaffoldState.conceal() }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
private fun BackdropScaffoldState.toggleBackdrop(scope: CoroutineScope) {
    scope.launch {
        if (isConcealed) {
            reveal()
        } else {
            conceal()
        }
    }
}

private enum class TopAppBarStyle {
    Normal,
    Search
}

private const val ANIMATION_MILLIS = 250
