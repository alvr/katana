package dev.alvr.katana.ui.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.serialization.Serializable

internal class DefaultKatanaComponent(
    componentContext: ComponentContext,
) : KatanaComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        childFactory = ::childFactory,
    )

    private fun childFactory(config: Config, componentContext: ComponentContext) = when (config) {
        Config.Login -> KatanaComponent.Child.LoginChild
        Config.Home -> KatanaComponent.Child.HomeChild
    }

    @Serializable
    internal sealed interface Config {
        @Serializable
        data object Login : Config

        @Serializable
        data object Home : Config
    }
}
