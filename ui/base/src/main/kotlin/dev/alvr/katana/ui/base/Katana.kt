package dev.alvr.katana.ui.base

import androidx.compose.runtime.Composable

@Composable
fun Katana(content: @Composable () -> Unit) {
    KatanaTheme(content)
}
