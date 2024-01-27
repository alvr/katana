package dev.alvr.katana.ui.base.decompose.extensions

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

@Composable
fun <C : Any, CS : ChildSlot<C, *>, V: Value<CS>> V.subscribeChildSlot(block: @Composable (C) -> Unit) {
    subscribeAsState().value.child?.configuration?.let { block(it) }
}
