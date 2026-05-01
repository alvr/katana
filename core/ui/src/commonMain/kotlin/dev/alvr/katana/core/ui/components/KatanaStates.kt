package dev.alvr.katana.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import dev.alvr.katana.core.ui.resources.Res
import dev.alvr.katana.core.ui.resources.component_empty_state
import dev.alvr.katana.core.ui.resources.component_error_state
import dev.alvr.katana.core.ui.resources.component_error_state_try_button
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.symbols.Error
import dev.alvr.katana.core.ui.symbols.Inbox
import dev.alvr.katana.core.ui.symbols.KatanaSymbols
import dev.alvr.katana.core.ui.theme.KatanaTheme

@Composable
fun KatanaEmptyState(
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String = Res.string.component_empty_state.value,
) {
    KatanaState(
        modifier = modifier,
        text = text,
        imageVector = KatanaSymbols.Inbox,
        contentDescription = contentDescription,
    )
}

@Composable
fun KatanaErrorState(
    text: String,
    loading: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = Res.string.component_error_state_try_button.value,
    contentDescription: String = Res.string.component_error_state.value,
) {
    KatanaState(
        modifier = modifier,
        text = text,
        imageVector = KatanaSymbols.Error,
        contentDescription = contentDescription,
    ) {
        Spacer(Modifier.height(KatanaTheme.sizes.size4))

        val alpha100 = KatanaTheme.alpha.alpha100
        val alpha66 = KatanaTheme.alpha.alpha66

        OutlinedButton(
            modifier =
                Modifier.graphicsLayer {
                    alpha =
                        if (loading) {
                            alpha66
                        } else {
                            alpha100
                        }
                },
            onClick = onRetry,
            enabled = !loading,
            colors =
                ButtonDefaults.outlinedButtonColors(
                    containerColor = KatanaTheme.colorScheme.secondary,
                    contentColor = contentColorFor(KatanaTheme.colorScheme.primary),
                ),
        ) {
            Text(text = buttonText, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun KatanaState(
    text: String,
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    extraContent: @Composable (() -> Unit)? = null,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(STATE_CONTENT_FRACTION),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = imageVector,
                modifier = Modifier.size(KatanaTheme.sizes.size40),
                contentDescription = contentDescription,
            )
            Text(modifier = Modifier, text = text, textAlign = TextAlign.Justify)

            extraContent?.invoke()
        }
    }
}

private const val STATE_CONTENT_FRACTION = .9f
