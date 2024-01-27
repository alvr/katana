package dev.alvr.katana.ui.account.component

import dev.alvr.katana.ui.account.viewmodel.AccountViewModel
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import org.koin.core.component.inject

internal class DefaultAccountComponent(
    componentContext: AppComponentContext,
    override val navigateToLogin: () -> Unit,
) : AccountComponent, AppComponentContext by componentContext {
    private val viewModel by inject<AccountViewModel>()

    override val state = viewModel.container.stateFlow

    override fun onLogoutClick() {
        viewModel.clearSession()
        navigateToLogin()
    }
}
