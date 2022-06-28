package dev.alvr.katana

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.navigation.NavGraphs
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getAnilistTokenUseCase: GetAnilistTokenUseCase,
) : ViewModel() {
    val initialNavGraph
        get() = if (getAnilistTokenUseCase.sync().nonEmpty()) {
            NavGraphs.home
        } else {
            LoginNavGraph
        }
}
