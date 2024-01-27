package dev.alvr.katana.ui.login.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.observe
import com.arkivanov.essenty.lifecycle.doOnCreate
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.base.decompose.extensions.collectWithLifecycle
import dev.alvr.katana.ui.login.viewmodel.LoginEvent
import dev.alvr.katana.ui.login.viewmodel.LoginViewModel
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

internal class DefaultLoginComponent(
    componentContext: AppComponentContext,
    override val token: String?,
    override val navigateToHome: () -> Unit,
) : LoginComponent, AppComponentContext by componentContext {
    private val viewModel by inject<LoginViewModel> { parametersOf(token) }

    override val state = viewModel.container.stateFlow

    init {
        viewModel.container.sideEffectFlow.collectWithLifecycle { event ->
            when (event) {
                is LoginEvent.LoginSuccessful -> navigateToHome()
            }
        }
    }
}
