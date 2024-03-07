package dev.alvr.katana.shared.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import dev.alvr.katana.core.ui.decompose.AppComponentContext
import kotlinx.serialization.Serializable

internal class DefaultKatanaComponent(
    componentContext: AppComponentContext,
) : KatanaComponent, AppComponentContext by componentContext {
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
