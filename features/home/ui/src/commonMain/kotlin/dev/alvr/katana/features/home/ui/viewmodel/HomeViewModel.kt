package dev.alvr.katana.features.home.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dev.alvr.katana.common.session.domain.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.ui.viewmodel.BaseViewModel
import dev.alvr.katana.core.ui.viewmodel.EmptyState
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

internal class HomeViewModel(
    private val observeActiveSessionUseCase: ObserveActiveSessionUseCase,
    private val clearActiveSessionUseCase: ClearActiveSessionUseCase,
) : BaseViewModel<EmptyState, HomeEffect>() {
    override val container = viewModelScope.container<EmptyState, HomeEffect>(EmptyState) {
        observeSession()
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
                        postSideEffect(HomeEffect.ExpiredToken)
                    },
                    ifRight = { isActive ->
                        if (!isActive) {
                            postSideEffect(HomeEffect.ExpiredToken)
                        }
                    },
                )
            }
        }
    }
}
