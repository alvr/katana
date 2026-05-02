package dev.alvr.katana.core.ui.components.snackbar

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> { error("No SnackbarController provided") }
