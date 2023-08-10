package dev.alvr.katana.ui.main.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import dev.alvr.katana.ui.main.KR

@Composable
internal fun SessionExpiredDialog(
    visible: Boolean,
    onAccept: () -> Unit,
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = {
                /* Nothing */
            },
            confirmButton = {
                TextButton(onClick = onAccept) {
                    Text(text = KR.string.session_expired_error_confirm_button)
                }
            },
            title = { Text(text = KR.string.session_expired_error_title) },
            text = { Text(text = KR.string.session_expired_error_message) },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
        )
    }
}
