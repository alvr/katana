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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.ui.base.components.KatanaSearchTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun KatanaHomeScaffold(
    @StringRes title: Int,
    searchPlaceholder: String,
    onSearch: (String) -> Unit,
    backContent: @Composable () -> Unit,
    katanaScaffoldState: KatanaHomeScaffoldState = rememberKatanaHomeScaffoldState(),
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    subtitle: String? = null,
    fab: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = false,
        appBar = {
            KatanaTopAppBar(
                katanaScaffoldState = katanaScaffoldState,
                scaffoldState = scaffoldState,
                title = title,
                subtitle = subtitle,
                onSearch = onSearch,
                searchPlaceholder = searchPlaceholder,
            )
        },
        backLayerContent = backContent,
        frontLayerShape = RectangleShape,
        frontLayerContent = {
            Scaffold(
                floatingActionButton = { fab?.invoke() },
                content = content,
            )
        },
    )
}

@Composable
@ExperimentalMaterialApi
@ExperimentalAnimationApi
private fun KatanaTopAppBar(
    katanaScaffoldState: KatanaHomeScaffoldState,
    scaffoldState: BackdropScaffoldState,
    @StringRes title: Int,
    searchPlaceholder: String,
    subtitle: String? = null,
    onSearch: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    Surface {
        AnimatedContent(
            targetState = katanaScaffoldState.topAppBarStyle,
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
                    onSearch = { katanaScaffoldState.searchToolbar() }.takeIf {
                        katanaScaffoldState.showTopAppBarActions
                    },
                    onFilter = { scaffoldState.toggleBackdrop(coroutineScope) }.takeIf {
                        katanaScaffoldState.showTopAppBarActions
                    },
                )
                TopAppBarStyle.Search -> KatanaSearchTopAppBar(
                    onValueChange = onSearch,
                    searchPlaceholder = searchPlaceholder,
                    onBack = {
                        katanaScaffoldState.resetToolbar()
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

@Stable
class KatanaHomeScaffoldState {
    internal var topAppBarStyle by mutableStateOf(TopAppBarStyle.Normal)
        private set

    var showTopAppBarActions by mutableStateOf(true)

    internal fun searchToolbar() {
        topAppBarStyle = TopAppBarStyle.Search
    }

    fun resetToolbar() {
        topAppBarStyle = TopAppBarStyle.Normal
    }

    companion object {
        internal val saver: Saver<KatanaHomeScaffoldState, *> = listSaver(
            save = { listOf<Any>(it.topAppBarStyle, it.showTopAppBarActions) },
            restore = {
                KatanaHomeScaffoldState().apply {
                    topAppBarStyle = it.first() as TopAppBarStyle
                    showTopAppBarActions = it.last() as Boolean
                }
            },
        )
    }
}

@Composable
fun rememberKatanaHomeScaffoldState() = rememberSaveable(
    saver = KatanaHomeScaffoldState.saver,
) { KatanaHomeScaffoldState() }

internal enum class TopAppBarStyle {
    Normal,
    Search
}

private const val ANIMATION_MILLIS = 250
