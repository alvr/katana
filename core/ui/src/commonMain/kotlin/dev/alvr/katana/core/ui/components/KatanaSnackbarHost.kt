package dev.alvr.katana.core.ui.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
    messageResource: StringResource,
    actionLabelResource: StringResource? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration =
        if (actionLabelResource == null) {
            SnackbarDuration.Short
        } else {
            SnackbarDuration.Indefinite
        },
    onAction: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    val result =
        showSnackbar(
            KatanaSnackbarVisuals(
                messageResource = messageResource,
                actionLabelResource = actionLabelResource,
                withDismissAction = withDismissAction,
                duration = duration,
            )
        )

    when (result) {
        SnackbarResult.ActionPerformed -> onAction?.invoke()
        SnackbarResult.Dismissed -> onDismiss?.invoke()
    }
}

@Stable
internal class KatanaSnackbarVisuals(
    internal val messageResource: StringResource,
    internal val actionLabelResource: StringResource? = null,
    internal val onAction: (() -> Unit)? = null,
    internal val onDismiss: (() -> Unit)? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : SnackbarVisuals {
    override val message: String = String.empty
    override val actionLabel: String? = null
}

@Composable
private fun KatanaSnackbarVisuals.toSnackbarVisuals(): SnackbarVisuals =
    object : SnackbarVisuals {
        override val message: String = this@toSnackbarVisuals.messageResource.value
        override val actionLabel: String? = this@toSnackbarVisuals.actionLabelResource?.value
        override val withDismissAction: Boolean = this@toSnackbarVisuals.withDismissAction
        override val duration: SnackbarDuration = this@toSnackbarVisuals.duration
    }

@Stable
private class KatanaSnackbarData(data: SnackbarData, override val visuals: SnackbarVisuals) : SnackbarData by data
