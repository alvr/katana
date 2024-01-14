package dev.alvr.katana.ui.main.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

internal sealed interface KatanaComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data object LoginChild : Child
        data object HomeChild : Child
    }
}
