package dev.alvr.katana.shared.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

sealed interface KatanaComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data object LoginChild : Child
        data object HomeChild : Child
    }
}
