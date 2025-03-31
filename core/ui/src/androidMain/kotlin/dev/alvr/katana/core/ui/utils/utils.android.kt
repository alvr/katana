package dev.alvr.katana.core.ui.utils

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal actual fun calculateWindowSizeClass() =
    calculateWindowSizeClass(LocalActivity.current ?: LocalView.current.context as Activity)
