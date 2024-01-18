package dev.alvr.katana.ui.main.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.alvr.katana.ui.home.component.HomeComponent
import dev.alvr.katana.ui.login.component.LoginComponent

internal sealed interface KatanaComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class HomeChild(val component: HomeComponent) : Child
        class LoginChild(val component: LoginComponent) : Child
    }
}
