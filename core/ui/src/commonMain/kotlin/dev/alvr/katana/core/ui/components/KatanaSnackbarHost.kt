package dev.alvr.katana.core.ui.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.ui.resources.value
import org.jetbrains.compose.resources.StringResource

@Composable
fun KatanaSnackbarHost(hostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        val visuals = (data.visuals as? KatanaSnackbarVisuals)?.toSnackbarVisuals() ?: data.visuals
        Snackbar(KatanaSnackbarData(data, visuals))
    }
}

suspend fun SnackbarHostState.showSnackbar(
    stringResource: StringResource,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration =
        if (actionLabel == null) {
            SnackbarDuration.Short
        } else {
            SnackbarDuration.Indefinite
        },
) =
    showSnackbar(
        KatanaSnackbarVisuals(
            stringResource = stringResource,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration,
        )
    )

@Stable
private class KatanaSnackbarVisuals(
    val stringResource: StringResource,
    override val actionLabel: String?,
    override val withDismissAction: Boolean,
    override val duration: SnackbarDuration,
) : SnackbarVisuals {
    override val message: String = String.empty
}

@Composable
private fun KatanaSnackbarVisuals.toSnackbarVisuals(): SnackbarVisuals = let { visuals ->
    object : SnackbarVisuals {
        override val message: String = visuals.stringResource.value
        override val withDismissAction: Boolean = visuals.withDismissAction
        override val actionLabel: String? = visuals.actionLabel
        override val duration: SnackbarDuration = visuals.duration
    }
}

@Stable
private class KatanaSnackbarData(data: SnackbarData, override val visuals: SnackbarVisuals) : SnackbarData by data
