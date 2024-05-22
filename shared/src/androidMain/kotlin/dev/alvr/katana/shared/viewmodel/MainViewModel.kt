package dev.alvr.katana.shared.viewmodel

import androidx.lifecycle.viewModelScope
import dev.alvr.katana.common.session.domain.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.domain.usecases.sync
import dev.alvr.katana.core.ui.viewmodel.BaseViewModel
import dev.alvr.katana.features.login.ui.navigation.LoginNavGraph
import dev.alvr.katana.shared.navigation.NavGraphs
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

internal class MainViewModel(
    private val clearActiveSessionUseCase: ClearActiveSessionUseCase,
    private val getAnilistTokenUseCase: GetAnilistTokenUseCase,
    private val observeActiveSessionUseCase: ObserveActiveSessionUseCase,
) : BaseViewModel<MainState, Nothing>() {
    override val container = viewModelScope.container<MainState, Nothing>(
        MainState(initialNavGraph = initialNavGraph),
    ) {
        observeSession()
    }

    private val initialNavGraph
        get() = if (getAnilistTokenUseCase.sync().isSome()) {
            NavGraphs.home
        } else {
            LoginNavGraph
        }

    fun clearSession() {
        intent {
            clearActiveSessionUseCase()
        }
    }

    private fun observeSession() {
        observeActiveSessionUseCase()

        intent {
            observeActiveSessionUseCase.flow.collect { active ->
                active.fold(
                    ifLeft = {
                        reduce { state.copy(isSessionActive = false) }
                    },
                    ifRight = { isActive ->
                        reduce { state.copy(isSessionActive = isActive) }
                    },
                )
            }
        }
    }
}
