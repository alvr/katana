package dev.alvr.katana.shared.content

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import dev.alvr.katana.core.ui.theme.noInsets
import dev.alvr.katana.shared.component.KatanaComponent
import dev.alvr.katana.shared.component.KatanaComponent.Child

@Composable
internal fun KatanaContent(
    component: KatanaComponent,
    modifier: Modifier = Modifier,
) {
    val stack by component.stack.subscribeAsState()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.noInsets,
    ) { paddingValues ->
        KatanaChildren(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .displayCutoutPadding(),
            stack = stack,
        )
    }
}

@Composable
private fun KatanaChildren(
    stack: ChildStack<*, Child>,
    modifier: Modifier = Modifier,
) {
    Children(
        modifier = modifier,
        stack = stack,
        animation = stackAnimation(),
    ) { (_, instance) ->
        when (instance) {
            is Child.LoginChild -> Text("Login")
            is Child.HomeChild -> Text("Home")
        }
    }
}
