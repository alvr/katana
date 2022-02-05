package dev.alvr.katana.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.usecases.SaveAnilistTokenUseCase
import io.github.aakira.napier.Napier
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveAnilistTokenUseCase: SaveAnilistTokenUseCase,
) : ViewModel(), ContainerHost<Unit, LoginEffect> {

    override val container = container<Unit, LoginEffect>(Unit)

    init {
        saveAnilistToken(savedStateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN))
    }

    private fun saveAnilistToken(token: String?) {
        intent {
            token?.let { t ->
                val parsedToken = t.substringBefore('&')

                Napier.d { "Saving Anilist token $parsedToken" }

                saveAnilistTokenUseCase(AnilistToken(parsedToken))
                postSideEffect(LoginEffect.Saved)
            } ?: run { Napier.d { "No token found in StateHandle" } }
        }
    }
}
