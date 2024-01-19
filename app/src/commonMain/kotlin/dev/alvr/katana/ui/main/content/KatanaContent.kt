package dev.alvr.katana.ui.main.content

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import dev.alvr.katana.ui.base.design.noInsets
import dev.alvr.katana.ui.home.content.HomeContent
import dev.alvr.katana.ui.login.content.LoginContent
import dev.alvr.katana.ui.main.component.KatanaComponent
import dev.alvr.katana.ui.main.component.KatanaComponent.Child

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
    ) { (_, instance) ->
        when (instance) {
            is Child.LoginChild -> LoginContent(instance.component)
            is Child.HomeChild -> HomeContent(instance.component)
        }
    }
}
