package dev.alvr.katana.ui.account.viewmodel

import dev.alvr.katana.domain.account.usecases.GetAccountInfoUseCase
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.usecases.LogOutUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent

internal class AccountViewModel(
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val logOutUseCase: LogOutUseCase,
) : BaseViewModel<AccountState, Nothing>() {
    override val container = coroutineScope.container<AccountState, Nothing>(AccountState()) {
        getAccountInfo()
    }

    fun clearSession() {
        intent {
            logOutUseCase()
        }
    }

    private fun getAccountInfo() {
        intent {
            getAccountInfoUseCase()
        }
    }
}
