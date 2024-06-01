package dev.alvr.katana.core.ui.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavDeepLink

val WindowInsets.Companion.noInsets: WindowInsets
    get() = WindowInsets(0)

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun isLandscape() = calculateWindowSizeClass().widthSizeClass > WindowWidthSizeClass.Medium

@Composable
fun doNavigation(onNavigation: () -> Unit) = dropUnlessResumed(LocalLifecycleOwner.current, onNavigation)

fun navDeepLink(deepLinkBuilder: NavDeepLink.Builder.() -> Unit): NavDeepLink =
    NavDeepLink.Builder().apply(deepLinkBuilder).build()
