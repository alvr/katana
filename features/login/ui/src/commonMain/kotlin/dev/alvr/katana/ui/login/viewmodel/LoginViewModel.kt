package dev.alvr.katana.ui.login.viewmodel

import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.usecases.SaveSessionUseCase
import dev.alvr.katana.domain.user.usecases.SaveUserIdUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.login.viewmodel.LoginState.ErrorType
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

internal class LoginViewModel(
    private val token: String?,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
) : BaseViewModel<LoginState, Nothing>() {
    override val container = coroutineScope.container<LoginState, Nothing>(LoginState()) {
        saveAnilistToken(token)
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
        saveSessionUseCase(AnilistToken(token)).fold(
            ifLeft = {
                updateState { copy(loading = false, errorType = ErrorType.SaveToken) }
            },
            ifRight = { saveUserId() },
        )
    }

    private suspend fun saveUserId() {
        saveUserIdUseCase().fold(
            ifLeft = {
                updateState { copy(loading = false, errorType = ErrorType.SaveUserId) }
            },
            ifRight = {
                updateState { copy(saved = true, loading = false, errorType = null) }
            },
        )
    }

    companion object {
        private const val TOKEN_SEPARATOR = '&'
    }
}
