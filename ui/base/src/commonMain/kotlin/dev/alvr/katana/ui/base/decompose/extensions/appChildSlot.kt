package dev.alvr.katana.ui.base.decompose.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigationSource
import com.arkivanov.decompose.router.slot.childSlot
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.base.decompose.DefaultAppComponentContext
import kotlinx.serialization.KSerializer

fun <C : Any, T : Any> ComponentContext.appChildSlot(
    source: SlotNavigationSource<C>,
    serializer: KSerializer<C>?,
    initialConfiguration: C,
    key: String = "DefaultChildSlot",
    handleBackButton: Boolean = false,
    childFactory: (C, AppComponentContext) -> T
) = childSlot(
    source = source,
    serializer = serializer,
    initialConfiguration = { initialConfiguration },
    key = key,
    handleBackButton = handleBackButton,
    childFactory = { configuration, componentContext ->
        childFactory(configuration, DefaultAppComponentContext(componentContext))
    },
)

fun <C : Any, T : Any> ComponentContext.appChildSlot(
    source: SlotNavigationSource<C>,
    serializer: KSerializer<C>?,
    initialConfiguration: () -> C? = { null },
    key: String = "DefaultChildSlot",
    handleBackButton: Boolean = false,
    childFactory: (C, AppComponentContext) -> T
) = childSlot(
    source = source,
    serializer = serializer,
    initialConfiguration = initialConfiguration,
    key = key,
    handleBackButton = handleBackButton,
    childFactory = { configuration, componentContext ->
        childFactory(configuration, DefaultAppComponentContext(componentContext))
    },
)
