package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.symbols.Cross
import dev.alvr.katana.core.ui.symbols.KatanaSymbols
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.features.home.ui.ANILIST_LOGIN
import dev.alvr.katana.features.home.ui.ANILIST_REGISTER
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.welcome_card_close_card_a11y
import dev.alvr.katana.features.home.ui.resources.welcome_card_login_button
import dev.alvr.katana.features.home.ui.resources.welcome_card_message
import dev.alvr.katana.features.home.ui.resources.welcome_card_register_button
import dev.alvr.katana.features.home.ui.resources.welcome_card_title
import dev.alvr.katana.features.home.ui.viewmodel.HomeIntent

@Composable
internal fun WelcomeCard(
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    var isCardVisible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        modifier = modifier,
        visible = isCardVisible,
        exit = fadeOut() + slideOutHorizontally { it },
    ) {
        ElevatedCard {
            Column(
                modifier = Modifier.padding(
                    start = KatanaTheme.dimensions.spacing4,
                    end = KatanaTheme.dimensions.spacing4,
                    top = KatanaTheme.dimensions.spacing2,
                    bottom = KatanaTheme.dimensions.spacing4,
                ),
            ) {
                WelcomeCardHeader(
                    onCloseCard = {
                        isCardVisible = false
                        onIntent(HomeIntent.ForYouIntent.CloseWelcomeCard)
                    },
                )

                WelcomeCardBody()
            }
        }
    }
}

@Composable
private fun WelcomeCardHeader(
    onCloseCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = Res.string.welcome_card_title.value,
            style = KatanaTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
        )

        IconButton(onClick = onCloseCard) {
            Icon(
                imageVector = KatanaSymbols.Cross,
                contentDescription = Res.string.welcome_card_close_card_a11y.value,
            )
        }
    }
}

@Composable
private fun WelcomeCardBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.spacing2),
    ) {
        Text(
            text = Res.string.welcome_card_message.value,
            textAlign = TextAlign.Justify,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.spacing3),
        ) {
            WelcomeCardRegisterButton(Modifier.weight(1f))
            WelcomeCardLoginButton(
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun WelcomeCardRegisterButton(
    modifier: Modifier = Modifier,
    ) {
    val uriHandler = LocalUriHandler.current

    OutlinedButton(
        modifier = modifier,
        onClick = { uriHandler.openUri(ANILIST_REGISTER) },
    ) {
        Text(
            text = Res.string.welcome_card_register_button.value,
        )
    }
}

@Composable
private fun WelcomeCardLoginButton(
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Button(
        modifier = modifier,
        onClick = {
            uriHandler.openUri(ANILIST_LOGIN)
        },
    ) {
        Text(
            text = Res.string.welcome_card_login_button.value,
        )
    }
}
