package dev.alvr.katana.core.ui.components.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.ui.components.KatanaSearchTopAppBar
import dev.alvr.katana.core.ui.resources.Res
import dev.alvr.katana.core.ui.resources.toolbar_menu_filter
import dev.alvr.katana.core.ui.resources.toolbar_menu_search
import dev.alvr.katana.core.ui.resources.toolbar_search_clear
import dev.alvr.katana.core.ui.resources.toolbar_search_close
import dev.alvr.katana.core.ui.resources.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun KatanaHomeScaffold(
    title: String,
    searchPlaceholder: String,
    onSearch: (String) -> Unit,
    backContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    katanaScaffoldState: KatanaHomeScaffoldState = rememberKatanaHomeScaffoldState(),
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    subtitle: String? = null,
    searchContentDescription: String = Res.string.toolbar_menu_search.value,
    filterContentDescription: String = Res.string.toolbar_menu_filter.value,
    closeContentDescription: String = Res.string.toolbar_search_close.value,
    clearContentDescription: String = Res.string.toolbar_search_clear.value,
    fab: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    BackdropScaffold(
        modifier = modifier,
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
                searchContentDescription = searchContentDescription,
                filterContentDescription = filterContentDescription,
                closeContentDescription = closeContentDescription,
                clearContentDescription = clearContentDescription,
            )
        },
        backLayerBackgroundColor = MaterialTheme.colorScheme.primary,
        backLayerContentColor = contentColorFor(MaterialTheme.colorScheme.primary),
        backLayerContent = backContent,
        frontLayerShape = RectangleShape,
        frontLayerBackgroundColor = MaterialTheme.colorScheme.surface,
        frontLayerContentColor = contentColorFor(MaterialTheme.colorScheme.surface),
        frontLayerScrimColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.60f),
        frontLayerContent = {
            Scaffold(
                floatingActionButton = { fab?.invoke() },
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                content = content,
            )
        },
    )
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun KatanaTopAppBar(
    katanaScaffoldState: KatanaHomeScaffoldState,
    scaffoldState: BackdropScaffoldState,
    title: String,
    searchPlaceholder: String,
    searchContentDescription: String,
    filterContentDescription: String,
    closeContentDescription: String,
    clearContentDescription: String,
    onSearch: (String) -> Unit,
    subtitle: String? = null,
) {
    val coroutineScope = rememberCoroutineScope()

    Surface {
        AnimatedContent(
            label = "KatanaTopAppBar",
            targetState = katanaScaffoldState.topAppBarStyle,
            transitionSpec = {
                fadeIn(tween(ANIMATION_MILLIS, easing = EaseIn)) togetherWith
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
                    title = title,
                    subtitle = subtitle,
                    searchContentDescription = searchContentDescription,
                    filterContentDescription = filterContentDescription,
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
                    closeContentDescription = closeContentDescription,
                    clearContentDescription = clearContentDescription,
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

@OptIn(ExperimentalMaterialApi::class)
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
    Search,
}

private const val ANIMATION_MILLIS = 250
