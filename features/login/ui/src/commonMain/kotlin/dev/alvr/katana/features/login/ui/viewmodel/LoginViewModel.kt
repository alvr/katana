package dev.alvr.katana.features.login.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.ui.viewmodel.BaseViewModel
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.features.login.ui.viewmodel.LoginState.ErrorType
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

internal class LoginViewModel(
    private val token: String?,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
) : BaseViewModel<LoginState, EmptyEffect>() {
    override val container = viewModelScope.container<LoginState, EmptyEffect>(LoginState()) {
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
