package dev.alvr.katana.core.ui.utils

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest

@Composable
fun isLandscape(): Boolean {
    val windowSizeClass = calculateWindowSizeClass()

    return remember(windowSizeClass.widthSizeClass, windowSizeClass.heightSizeClass) {
        val expanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
        val medium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium &&
            windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

        expanded || medium
    }
}

@Composable
fun rememberSnackbarHostState() = remember { SnackbarHostState() }

@Composable
internal expect fun calculateWindowSizeClass(): WindowSizeClass

@Composable
fun imageRequest(builder: ImageRequest.Builder.() -> Unit) =
    ImageRequest.Builder(LocalPlatformContext.current)
        .apply(builder)
        .build()
