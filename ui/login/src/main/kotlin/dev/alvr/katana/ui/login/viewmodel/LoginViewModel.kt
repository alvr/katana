package dev.alvr.katana.ui.login.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.usecases.SaveAnilistTokenUseCase
import dev.alvr.katana.domain.user.usecases.SaveUserIdUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.login.LOGIN_DEEP_LINK_TOKEN
import dev.alvr.katana.ui.login.R
import javax.inject.Inject
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveAnilistTokenUseCase: SaveAnilistTokenUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
) : BaseViewModel<LoginState, Nothing>() {
    override val container = container<LoginState, Nothing>(LoginState.initial()) {
        saveAnilistToken(savedStateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN))
    }

    private fun saveAnilistToken(token: String?) {
        if (token.isNullOrBlank()) return

        intent {
            reduce { state.copy(loading = true) }

            val parsedToken = token.substringBefore(TOKEN_SEPARATOR)
            saveToken(parsedToken)
        }
    }

    private suspend fun saveToken(token: String) {
        saveAnilistTokenUseCase(AnilistToken(token)).fold(
            ifLeft = {
                updateState { copy(loading = false, errorMessage = R.string.save_token_error) }
            },
            ifRight = { saveUserId() },
        )
    }

    private suspend fun saveUserId() {
        saveUserIdUseCase().fold(
            ifLeft = {
                updateState { copy(loading = false, errorMessage = R.string.fetch_userid_error) }
            },
            ifRight = {
                updateState { copy(saved = true, loading = false, errorMessage = null) }
            },
        )
    }

    companion object {
        private const val TOKEN_SEPARATOR = '&'
    }
}
