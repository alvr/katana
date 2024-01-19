package dev.alvr.katana.ui.base.decompose.extensions

import com.arkivanov.decompose.ComponentContext
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.base.decompose.DefaultAppComponentContext

fun <C : ComponentContext> C.asAppComponent(): AppComponentContext = if (this is AppComponentContext) {
    this
} else {
    DefaultAppComponentContext(this)
}
