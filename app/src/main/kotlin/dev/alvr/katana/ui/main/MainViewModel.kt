package dev.alvr.katana.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.domain.session.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.domain.session.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.navigation.NavGraphs
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import javax.inject.Inject
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val clearActiveSessionUseCase: ClearActiveSessionUseCase,
    private val getAnilistTokenUseCase: GetAnilistTokenUseCase,
    private val observeActiveSessionUseCase: ObserveActiveSessionUseCase,
) : BaseViewModel<MainState, Nothing>() {
    override val container = container<MainState, Nothing>(
        MainState(
            initialNavGraph = initialNavGraph,
        ),
    ) {
        observeSession()
    }

    private val initialNavGraph
        get() = if (getAnilistTokenUseCase.sync().nonEmpty()) {
            NavGraphs.home
        } else {
            LoginNavGraph
        }

    fun clearSession() {
        intent { clearActiveSessionUseCase() }
    }

    private fun observeSession() {
        observeActiveSessionUseCase()

        intent {
            observeActiveSessionUseCase.flow.collect { active ->
                reduce { state.copy(isSessionActive = active) }
            }
        }
    }
}
