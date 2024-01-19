package dev.alvr.katana.ui.main

import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.lifecycle.ApplicationLifecycle
import dev.alvr.katana.ui.base.decompose.extensions.asAppComponent
import dev.alvr.katana.ui.main.component.DefaultKatanaComponent

@Suppress("FunctionName", "Unused")
@OptIn(ExperimentalDecomposeApi::class)
fun MainViewController() = ComposeUIViewController(
    configure = {
        onFocusBehavior = OnFocusBehavior.DoNothing
    },
) {
    val katanaComponent = DefaultKatanaComponent(
        componentContext = DefaultComponentContext(
            lifecycle = ApplicationLifecycle(),
        ).asAppComponent(),
    )

    KatanaApp(katanaComponent)
}
