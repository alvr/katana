package dev.alvr.katana.shared

import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import dev.alvr.katana.core.ui.decompose.appComponentContext
import dev.alvr.katana.shared.component.DefaultKatanaComponent

@Suppress("FunctionNaming")
fun MainViewController() = ComposeUIViewController(
    configure = {
        onFocusBehavior = OnFocusBehavior.DoNothing
    },
) {
    val katanaComponent = DefaultKatanaComponent(
        componentContext = DefaultComponentContext(
            lifecycle = ApplicationLifecycle(),
        ).appComponentContext,
    )

    Katana(katanaComponent)
}
