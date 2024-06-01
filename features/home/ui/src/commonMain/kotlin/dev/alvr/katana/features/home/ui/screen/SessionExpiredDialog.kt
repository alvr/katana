package dev.alvr.katana.features.home.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.screens.KatanaScreen
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.session_expired_error_confirm_button
import dev.alvr.katana.features.home.ui.resources.session_expired_error_message
import dev.alvr.katana.features.home.ui.resources.session_expired_error_title

internal fun NavGraphBuilder.expiredSessionDialog(homeNavigator: HomeNavigator) {
    dialog(
        route = KatanaScreen.ExpiredSessionDialog.name,
        dialogProperties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    ) {
        SessionExpiredDialog(
            onConfirmButton = homeNavigator::navigateToLogin,
        )
    }
}

@Composable
private fun SessionExpiredDialog(
    onConfirmButton: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            /* Nothing */
        },
        confirmButton = {
            TextButton(onClick = onConfirmButton) {
                Text(text = Res.string.session_expired_error_confirm_button.value)
            }
        },
        title = { Text(text = Res.string.session_expired_error_title.value) },
        text = { Text(text = Res.string.session_expired_error_message.value) },
    )
}
