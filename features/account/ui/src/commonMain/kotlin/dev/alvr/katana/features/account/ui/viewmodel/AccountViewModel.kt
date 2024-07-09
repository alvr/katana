package dev.alvr.katana.features.account.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dev.alvr.katana.common.session.domain.usecases.LogOutUseCase
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.ui.viewmodel.BaseViewModel
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.features.account.ui.entities.mappers.toEntity
import org.orbitmvi.orbit.container

internal class AccountViewModel(
    private val observeUserInfoUseCase: ObserveUserInfoUseCase,
    private val logOutUseCase: LogOutUseCase,
) : BaseViewModel<AccountState, EmptyEffect>() {
    override val container = viewModelScope.container<AccountState, EmptyEffect>(AccountState()) {
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
