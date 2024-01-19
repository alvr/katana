package dev.alvr.katana.ui.base.decompose.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigationSource
import com.arkivanov.decompose.router.stack.childStack
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.base.decompose.DefaultAppComponentContext
import kotlinx.serialization.KSerializer

fun <C : Any, T : Any> ComponentContext.appChildStack(
    source: StackNavigationSource<C>,
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
    source: StackNavigationSource<C>,
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
