package dev.alvr.katana.ui.login

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.usecases.SaveAnilistTokenUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import io.github.aakira.napier.Napier
import javax.inject.Inject
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveAnilistTokenUseCase: SaveAnilistTokenUseCase,
) : BaseViewModel<Unit, LoginEffect>() {

    init {
        saveAnilistToken(savedStateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN))
    }

    override val container get() = container<Unit, LoginEffect>(Unit)

    private fun saveAnilistToken(token: String?) {
        token?.let { t ->
            executeUseCase({
                val parsedToken = t.substringBefore('&')

                Napier.d { "Saving Anilist token $parsedToken" }

                saveAnilistTokenUseCase(AnilistToken(parsedToken))
                postSideEffect(LoginEffect.Saved)
            }, {
                postSideEffect(LoginEffect.Error)
            })
        } ?: run { Napier.d { "No token found in StateHandle" } }
    }
}
