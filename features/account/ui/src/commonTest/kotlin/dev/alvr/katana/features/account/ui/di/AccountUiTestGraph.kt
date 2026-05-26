package dev.alvr.katana.features.account.ui.di

import dev.alvr.katana.common.session.domain.usecases.LogOutUseCase
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.account.ui.viewmodel.AccountViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface AccountUiTestGraph : TestAppGraph {
    val accountViewModel: AccountViewModel

    @Provides
    fun accountViewModel(
        observeUserInfoUseCase: ObserveUserInfoUseCase,
        logOutUseCase: LogOutUseCase,
    ): AccountViewModel = AccountViewModel(observeUserInfoUseCase, logOutUseCase)

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides observeUserInfoUseCase: ObserveUserInfoUseCase,
            @Provides logOutUseCase: LogOutUseCase,
        ): AccountUiTestGraph
    }
}

internal fun createAccountUiTestGraph(observeUserInfoUseCase: ObserveUserInfoUseCase, logOutUseCase: LogOutUseCase) =
    createGraphFactory<AccountUiTestGraph.Factory>().create(observeUserInfoUseCase, logOutUseCase)
