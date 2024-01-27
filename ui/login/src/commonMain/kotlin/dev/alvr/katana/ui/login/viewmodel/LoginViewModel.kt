package dev.alvr.katana.ui.login.viewmodel

import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.usecases.SaveSessionUseCase
import dev.alvr.katana.domain.user.usecases.SaveUserIdUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.login.viewmodel.LoginState.ErrorType
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

internal class LoginViewModel(
    private val token: String?,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
) : BaseViewModel<LoginState, LoginEvent>() {
    private val jwtRegex by lazy { "[A-Za-z0-9_-]{2,}(?:\\.[A-Za-z0-9_-]{2,}){2}".toRegex() }

    override val container = coroutineScope.container<LoginState, LoginEvent>(LoginState()) {
        saveAnilistToken(token)
    }

    private fun saveAnilistToken(token: String?) {
        if (token.isNullOrBlank()) return
        val parsedToken = jwtRegex.find(token)?.value ?: return

        intent {
            reduce { state.copy(loading = true) }
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
                intent { postSideEffect(LoginEvent.LoginSuccessful) }
            },
        )
    }
}
