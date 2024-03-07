package dev.alvr.katana.core.ui.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.serialization.KSerializer

fun <C : Any, T : Any> ComponentContext.appChildStack(
    source: StackNavigation<C>,
    serializer: KSerializer<C>?,
    initialConfiguration: C,
    key: String = "DefaultChildStack",
    handleBackButton: Boolean = false,
    childFactory: (C, AppComponentContext) -> T
) = childStack(
    source = source,
    serializer = serializer,
    initialStack = { listOf(initialConfiguration) },
    key = key,
    handleBackButton = handleBackButton,
    childFactory = { configuration, componentContext ->
        childFactory(configuration, DefaultAppComponentContext(componentContext))
    },
)

fun <C : Any, T : Any> ComponentContext.appChildStack(
    source: StackNavigation<C>,
    serializer: KSerializer<C>?,
    initialStack: () -> List<C>,
    key: String = "DefaultChildStack",
    handleBackButton: Boolean = false,
    childFactory: (C, AppComponentContext) -> T
) = childStack(
    source = source,
    serializer = serializer,
    initialStack = initialStack,
    key = key,
    handleBackButton = handleBackButton,
    childFactory = { configuration, componentContext ->
        childFactory(configuration, DefaultAppComponentContext(componentContext))
    },
)

val ComponentContext.appComponentContext
    get() = if (this is AppComponentContext) {
        this
    } else {
        DefaultAppComponentContext(this)
    }
