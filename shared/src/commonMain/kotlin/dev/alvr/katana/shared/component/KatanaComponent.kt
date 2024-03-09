package dev.alvr.katana.shared.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.alvr.katana.features.home.ui.component.HomeComponent

sealed interface KatanaComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data object LoginChild : Child
        class HomeChild(val component: HomeComponent) : Child
    }
}
