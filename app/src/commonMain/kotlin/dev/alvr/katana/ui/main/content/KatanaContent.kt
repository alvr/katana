package dev.alvr.katana.ui.main.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import dev.alvr.katana.ui.home.content.HomeContent
import dev.alvr.katana.ui.login.content.LoginContent
import dev.alvr.katana.ui.main.component.KatanaComponent
import dev.alvr.katana.ui.main.component.KatanaComponent.Child

@Composable
internal fun KatanaContent(
    component: KatanaComponent,
    modifier: Modifier = Modifier,
) {
    Children(
        modifier = modifier,
        stack = component.stack,
    ) { (_, instance) ->
        when (instance) {
            is Child.LoginChild -> LoginContent(instance.component)
            is Child.HomeChild -> HomeContent(instance.component)
        }
    }
}
