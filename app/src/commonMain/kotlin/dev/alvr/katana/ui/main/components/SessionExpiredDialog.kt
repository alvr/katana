package dev.alvr.katana.ui.main.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import dev.alvr.katana.app.app.generated.resources.Res
import dev.alvr.katana.app.app.generated.resources.session_expired_error_confirm_button
import dev.alvr.katana.app.app.generated.resources.session_expired_error_message
import dev.alvr.katana.app.app.generated.resources.session_expired_error_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun SessionExpiredDialog(
    visible: Boolean,
    onAccept: () -> Unit,
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = {/* Nothing */ },
            confirmButton = {
                TextButton(onClick = onAccept) {
                    Text(text = stringResource(Res.string.session_expired_error_confirm_button))
                }
            },
            title = { Text(text = stringResource(Res.string.session_expired_error_title)) },
            text = { Text(text = stringResource(Res.string.session_expired_error_message)) },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
        )
    }
}
