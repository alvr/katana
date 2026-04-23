package dev.alvr.katana.shared

import androidx.compose.ui.window.ComposeUIViewController
import dev.alvr.katana.shared.di.AppGraph
import dev.zacsweers.metro.createGraph
import platform.UIKit.UIViewController

@Suppress("FunctionNaming", "FunctionName")
fun MainViewController(): UIViewController {
    val app = createGraph<AppGraph>().app
    return ComposeUIViewController { app() }
}
