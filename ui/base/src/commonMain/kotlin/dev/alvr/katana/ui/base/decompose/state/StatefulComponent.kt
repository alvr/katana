package dev.alvr.katana.ui.base.decompose.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow

interface StatefulComponent<out S : UiState> {
    val state: StateFlow<S>
}

@Composable
fun <S : UiState> StatefulComponent<S>.collectAsState() = state.collectAsState()
