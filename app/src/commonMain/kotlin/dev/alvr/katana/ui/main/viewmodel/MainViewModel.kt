package dev.alvr.katana.ui.main.viewmodel

import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.main.component.DefaultKatanaComponent.Config
import org.orbitmvi.orbit.container

internal class MainViewModel(
    private val getAnilistTokenUseCase: GetAnilistTokenUseCase,
) : BaseViewModel<Unit, Nothing>() {
    override val container = coroutineScope.container<Unit, Nothing>(Unit)

    val initialConfiguration
        get() = if (getAnilistTokenUseCase.sync().isSome()) {
            Config.Home
        } else {
            Config.Login
        }
}
