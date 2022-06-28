package dev.alvr.katana.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.navigation.NavGraphs
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import javax.inject.Inject
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getAnilistTokenUseCase: GetAnilistTokenUseCase,
) : BaseViewModel<MainState, Nothing>() {
    override val container = container<MainState, Nothing>(
        MainState(
            initialNavGraph = initialNavGraph,
        ),
    )

    private val initialNavGraph
        get() = if (getAnilistTokenUseCase.sync().nonEmpty()) {
            NavGraphs.home
        } else {
            LoginNavGraph
        }
}
