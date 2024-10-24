package dev.alvr.katana.shared.viewmodel

import androidx.lifecycle.viewModelScope
import dev.alvr.katana.common.session.domain.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.core.domain.usecases.sync
import dev.alvr.katana.core.ui.screens.KatanaScreen
import dev.alvr.katana.core.ui.viewmodel.BaseViewModel
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import org.orbitmvi.orbit.container

internal class MainViewModel(
    private val getAnilistTokenUseCase: GetAnilistTokenUseCase,
) : BaseViewModel<MainState, EmptyEffect>() {
    override val container = viewModelScope.container<MainState, EmptyEffect>(
        MainState(initialScreen = initialNavGraph),
    )

    private val initialNavGraph
        get() = if (getAnilistTokenUseCase.sync().isSome()) {
            KatanaScreen.Home
        } else {
            KatanaScreen.Auth
        }
}
