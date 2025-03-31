package dev.alvr.katana

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.alvr.katana.shared.Katana

@OptIn(ExperimentalComposeUiApi::class)
internal fun main() {
    CanvasBasedWindow("Katana", canvasElementId = "katanaCanvas") {
        Katana()
    }
}
