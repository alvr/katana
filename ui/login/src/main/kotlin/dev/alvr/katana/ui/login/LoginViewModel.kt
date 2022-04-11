package dev.alvr.katana.ui.login

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.usecases.SaveAnilistTokenUseCase
import dev.alvr.katana.domain.user.usecases.SaveUserIdUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import io.github.aakira.napier.Napier
import javax.inject.Inject
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveAnilistTokenUseCase: SaveAnilistTokenUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase
) : BaseViewModel<Unit, LoginEffect>() {

    override val container = container<Unit, LoginEffect>(Unit)

    init {
        saveAnilistToken(savedStateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN))
    }

    private fun saveAnilistToken(token: String?) {
        token?.let { t ->
            postSideEffect(LoginEffect.Loading)

            executeUseCase({
                val parsedToken = t.substringBefore('&')

                saveAnilistTokenUseCase(AnilistToken(parsedToken))
                saveUserIdUseCase()

                postSideEffect(LoginEffect.Saved)
            }, {
                postSideEffect(LoginEffect.Error)
            })
        } ?: Napier.i { "No token found in StateHandle" }
    }
}
