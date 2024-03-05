package dev.alvr.katana.features.account.ui.viewmodel

import dev.alvr.katana.common.session.domain.usecases.LogOutUseCase
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.core.common.di.ScreenScope
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.ui.viewmodel.BaseViewModel
import dev.alvr.katana.features.account.ui.entities.mappers.toEntity
import me.tatarka.inject.annotations.Inject
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent

@Inject
@ScreenScope
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
