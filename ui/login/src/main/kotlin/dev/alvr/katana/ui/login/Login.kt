package dev.alvr.katana.ui.login

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun Login(token: String?) {
    val background = rememberSaveable {
        listOf(
            R.drawable.background_chihiro,
            R.drawable.background_howl,
            R.drawable.background_mononoke,
            R.drawable.background_totoro
        ).random()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(background),
            contentDescription = stringResource(id = R.string.content_description_background),
            contentScale = ContentScale.Crop,
            modifier = Modifier.alpha(BACKGROUND_ALPHA)
        )

        Box(modifier = Modifier.padding(24.dp)) {
            Header(modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun Header(modifier: Modifier = Modifier) {
    val animVisibleState = remember { MutableTransitionState(false) }.apply {
        targetState = true
    }

    AnimatedVisibility(
        modifier = modifier,
        visibleState = animVisibleState,
        enter = slideInVertically(
            animationSpec = tween(
                delayMillis = LOGO_ANIMATION_DELAY,
                durationMillis = LOGO_ANIMATION_DURATION
            )
        ) { height -> height / 2 } + fadeIn(
            animationSpec = tween(
                delayMillis = LOGO_ANIMATION_DELAY,
                durationMillis = LOGO_ANIMATION_DURATION
            ),
            initialAlpha = 0f
        )
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KatanaLogo()
            Description()
        }
    }
}

@Composable
private fun KatanaLogo() {
    val configuration = LocalConfiguration.current
    val sizeFraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LOGO_RESIZED
    } else {
        LOGO_FULL_SIZE
    }

    Image(
        painter = painterResource(R.drawable.ic_katana_logo),
        contentDescription = stringResource(id = R.string.content_description_katana_logo),
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(fraction = sizeFraction),
    )
}

@Composable
private fun Description() {
    Text(
        text = stringResource(id = R.string.katana_description),
        style = MaterialTheme.typography.h5
    )
}

private const val BACKGROUND_ALPHA = .3f
private const val LOGO_FULL_SIZE = 1f
private const val LOGO_RESIZED = .6f

private const val LOGO_ANIMATION_DELAY = 300
private const val LOGO_ANIMATION_DURATION = 1250
