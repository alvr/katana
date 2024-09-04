package dev.alvr.katana

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import dev.alvr.katana.shared.Katana

internal fun main() = application {
    Window(
        resizable = false,
        onCloseRequest = ::exitApplication,
        title = "Katana",
        state = WindowState(
            size = DpSize(480.dp, 860.dp),
            position = WindowPosition.Aligned(Alignment.Center)
        )
    ) {
        Katana()
    }
}
