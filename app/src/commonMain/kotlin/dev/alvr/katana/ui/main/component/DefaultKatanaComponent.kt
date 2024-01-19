package dev.alvr.katana.ui.main.component

import com.arkivanov.decompose.router.stack.StackNavigation
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.base.decompose.extensions.appChildStack
import dev.alvr.katana.ui.home.component.createHomeComponent
import dev.alvr.katana.ui.login.component.createLoginComponent
import dev.alvr.katana.ui.main.component.KatanaComponent.Child.HomeChild
import dev.alvr.katana.ui.main.component.KatanaComponent.Child.LoginChild
import dev.alvr.katana.ui.main.viewmodel.MainViewModel
import kotlinx.serialization.Serializable
import org.koin.core.component.inject

internal class DefaultKatanaComponent(
    componentContext: AppComponentContext,
) : KatanaComponent, AppComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    private val viewModel by inject<MainViewModel>()

    override val stack = appChildStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = viewModel.initialConfiguration,
        handleBackButton = true,
        childFactory = ::childFactory,
    )

    private fun childFactory(
        rootRoute: Config,
        componentContext: AppComponentContext,
    ) = when (rootRoute) {
        is Config.Login -> componentContext.loginChildFactory()
        is Config.Home -> componentContext.homeChildFactory()
    }

    private fun AppComponentContext.loginChildFactory() =
        LoginChild(createLoginComponent())

    private fun AppComponentContext.homeChildFactory() =
        HomeChild(createHomeComponent())

    @Serializable
    internal sealed interface Config {
        @Serializable
        data object Login : Config

        @Serializable
        data object Home : Config
    }
}
