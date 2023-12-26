package dev.alvr.katana.ui.account.viewmodel

import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.usecases.LogOutUseCase
import dev.alvr.katana.domain.user.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.ui.account.entities.mappers.toEntity
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent

internal class AccountViewModel(
    private val observeUserInfoUseCase: ObserveUserInfoUseCase,
    private val logOutUseCase: LogOutUseCase,
) : BaseViewModel<AccountState, Nothing>() {
    override val container = coroutineScope.container<AccountState, Nothing>(AccountState()) {
        collectUserInfo()
    }

    fun clearSession() {
        intent {
            logOutUseCase()
        }
    }

    private fun collectUserInfo() {
        observeUserInfoUseCase()
        observeUserInfo()
    }

    private fun observeUserInfo() {
        intent {
            observeUserInfoUseCase.flow.collect { res ->
                res.fold(
                    ifLeft = {
                        updateState { copy(isLoading = false, isError = true) }
                    },
                    ifRight = { userInfo ->
                        updateState {
                            copy(
                                isLoading = false,
                                isError = false,
                                userInfo = userInfo.toEntity(),
                            )
                        }
                    },
                )
            }
        }
    }
}
