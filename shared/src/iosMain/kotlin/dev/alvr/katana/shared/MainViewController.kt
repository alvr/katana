package dev.alvr.katana.shared

import androidx.compose.ui.window.ComposeUIViewController
import dev.alvr.katana.shared.di.AppGraph
import dev.zacsweers.metro.createGraph
import platform.UIKit.UIViewController

private val appGraph: AppGraph by lazy { createGraph<AppGraph>() }

@Suppress("FunctionNaming", "FunctionName")
fun MainViewController(): UIViewController {
    val app = appGraph.app
    return ComposeUIViewController { app() }
}

fun handleDeepLink(url: String) {
    appGraph.deepLinkDispatcher.dispatch(url)
}
