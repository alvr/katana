package dev.alvr.katana

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.sync
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.navigation.screens.Screen
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    getAnilistTokenUseCase: GetAnilistTokenUseCase
) : ViewModel() {
    val initialRoute = if (getAnilistTokenUseCase.sync() != null) {
        Screen.Home
    } else {
        Screen.Login
    }.route
}
