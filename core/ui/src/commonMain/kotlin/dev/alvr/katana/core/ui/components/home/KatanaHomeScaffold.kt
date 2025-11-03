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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.ui.components.KatanaScaffold
import dev.alvr.katana.core.ui.components.KatanaSearchTopAppBar
import dev.alvr.katana.core.ui.resources.Res
import dev.alvr.katana.core.ui.resources.toolbar_menu_filter
import dev.alvr.katana.core.ui.resources.toolbar_menu_search
import dev.alvr.katana.core.ui.resources.toolbar_search_clear
import dev.alvr.katana.core.ui.resources.toolbar_search_close
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.theme.noInsets

@Composable
fun KatanaHomeScaffold(
    title: String,
    searchPlaceholder: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    katanaScaffoldState: KatanaHomeScaffoldState = rememberKatanaHomeScaffoldState(),
    subtitle: String? = null,
    searchContentDescription: String = Res.string.toolbar_menu_search.value,
    filterContentDescription: String = Res.string.toolbar_menu_filter.value,
    closeContentDescription: String = Res.string.toolbar_search_close.value,
    clearContentDescription: String = Res.string.toolbar_search_clear.value,
    fab: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    KatanaScaffold(
        modifier = modifier,
        topBar = {
            KatanaTopAppBar(
                katanaScaffoldState = katanaScaffoldState,
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
        floatingActionButton = { fab?.invoke() },
        contentWindowInsets = WindowInsets.noInsets,
        content = content,
    )
}

@Composable
private fun KatanaTopAppBar(
    katanaScaffoldState: KatanaHomeScaffoldState,
    title: String,
    searchPlaceholder: String,
    searchContentDescription: String,
    filterContentDescription: String,
    closeContentDescription: String,
    clearContentDescription: String,
    onSearch: (String) -> Unit,
    subtitle: String? = null,
) {
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
                    onSearch = { katanaScaffoldState.searchToolbar() },
                    onFilter = null,
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
                )
            }
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
