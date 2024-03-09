package dev.alvr.katana.shared.component

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.replaceAll
import dev.alvr.katana.core.ui.decompose.AppComponentContext
import dev.alvr.katana.core.ui.decompose.appChildStack
import dev.alvr.katana.features.home.ui.component.HomeComponent
import kotlinx.serialization.Serializable

internal class DefaultKatanaComponent(
    componentContext: AppComponentContext,
    private val homeComponentFactory: HomeComponent.Factory,
) : KatanaComponent, AppComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack = appChildStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        childFactory = ::childFactory,
    )

    private fun childFactory(config: Config, componentContext: AppComponentContext) =
        when (config) {
            Config.Login -> KatanaComponent.Child.LoginChild
            Config.Home -> componentContext.homeFactory()
        }

    private fun AppComponentContext.homeFactory() = KatanaComponent.Child.HomeChild(
        homeComponentFactory.create(
            componentContext = this,
            navigateToLogin = { navigation.replaceAll(Config.Login) },
        ),
    )

    @Serializable
    internal sealed interface Config {
        @Serializable
        data object Login : Config

        @Serializable
        data object Home : Config
    }
}
