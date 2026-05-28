package dev.alvr.katana.features.account.ui.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.common.session.domain.usecases.LogOutUseCase
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.features.account.ui.entities.mappers.toEntity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Stable
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class AccountViewModel(
    private val observeUserInfoUseCase: ObserveUserInfoUseCase,
    private val logOutUseCase: LogOutUseCase,
) : KatanaViewModel<AccountState, AccountEffect, AccountIntent>(AccountState()) {

    /**
     * Initializes the ViewModel by starting observation of the current user information.
     */
    override fun init() {
        observeUserInfo()
    }

    /**
     * Handles an AccountIntent and performs the corresponding account action; for AccountIntent.Logout this triggers a user logout.
     *
     * @param intent The incoming intent to handle.
     */
    override fun intent(intent: AccountIntent) {
        when (intent) {
            AccountIntent.Logout -> logOut()
        }
    }

    private fun observeUserInfo() {
        execute(
            useCase = observeUserInfoUseCase,
            params = Unit,
            onFailure = { state { copy(error = true, loading = false) } },
            onSuccess = { userInfo -> state { copy(error = false, loading = false, userInfo = userInfo.toEntity()) } },
        )
    }

    private fun logOut() {
        execute(
            useCase = logOutUseCase,
            params = Unit,
            onFailure = {
                state { copy(userInfo = null) }
                effect(AccountEffect.LoggingOutFailure)
            },
            onSuccess = {
                state { copy(userInfo = null) }
                effect(AccountEffect.LoggingOutSuccess)
            },
        )
    }
}
