package dev.alvr.katana

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.alvr.katana.core.common.KatanaBuildConfig
import dev.alvr.katana.shared.Katana
import dev.alvr.katana.shared.di.katanaModule
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@OptIn(ExperimentalComposeUiApi::class)
internal fun main() {
    startKoin {
        printLogger(if (KatanaBuildConfig.DEBUG) Level.DEBUG else Level.NONE)
        modules(katanaModule)
    }

    CanvasBasedWindow("Katana", canvasElementId = "katanaCanvas") {
        Katana()
    }
}
