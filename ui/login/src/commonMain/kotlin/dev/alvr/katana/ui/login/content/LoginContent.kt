package dev.alvr.katana.ui.login.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode.Reverse
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.ui.base.resources.KatanaResource
import dev.alvr.katana.ui.base.utils.isLandscape
import dev.alvr.katana.ui.login.component.LoginComponent
import dev.alvr.katana.ui.login.content.State.Buttons
import dev.alvr.katana.ui.login.resources.KatanaResources
import dev.alvr.katana.ui.login.strings.LocalLoginStrings
import dev.alvr.katana.ui.login.viewmodel.LoginState

@Composable
fun LoginContent(component: LoginComponent, modifier: Modifier = Modifier) {
    val state by component.state.collectAsState()

    Login(
        modifier = modifier,
        state = state,
    )
}

@Composable
private fun Login(state: LoginState, modifier: Modifier = Modifier) {
    val strings = LocalLoginStrings.current
    var loading by remember { mutableStateOf(false) }

    val background = rememberSaveable(saver = KatanaResource.saver) {
        listOf(
            KatanaResources.backgroundChihiro,
            KatanaResources.backgroundHowl,
            KatanaResources.backgroundMononoke,
            KatanaResources.backgroundTotoro,
        ).random()
    }

    val scaffoldState = rememberScaffoldState()

    when {
        state.loading -> loading = true
        state.errorType != null -> {
            loading = false
            val message = when (state.errorType) {
                LoginState.ErrorType.SaveToken -> strings.saveTokenError
                LoginState.ErrorType.SaveUserId -> strings.fetchUserIdError
            }
            LaunchedEffect(state.errorType) {
                scaffoldState.snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (loading) {
                Loading()
            } else {
                Image(
                    painter = background.asPainter,
                    contentDescription = strings.contentDescriptionBackground,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(BACKGROUND_ALPHA),
                )

                Box(modifier = Modifier.padding(24.dp)) {
                    Header(modifier = Modifier.align(Alignment.TopCenter))
                    Bottom(modifier = Modifier.align(Alignment.BottomCenter))
                }
            }
        }
    }
}

@Composable
private fun Loading() {
    Box {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun Header(modifier: Modifier = Modifier) {
    Animate(
        delayMillis = HEADER_ANIMATION_DELAY,
        durationMillis = HEADER_ANIMATION_DURATION,
        modifier = modifier,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            KatanaLogo()
            Description()
        }
    }
}

@Composable
private fun KatanaLogo() {
    val sizeFraction =
        if (isLandscape()) {
            LOGO_RESIZED
        } else {
            LOGO_FULL_SIZE
        }

    Image(
        painter = KatanaResources.icKatanaLogo.asPainter,
        contentDescription = LocalLoginStrings.current.contentDescriptionKatanaLogo,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(fraction = sizeFraction),
    )
}

@Composable
private fun Description() {
    Text(
        text = LocalLoginStrings.current.headerKatanaDescription,
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
private fun Bottom(modifier: Modifier = Modifier) {
    var currentState by rememberSaveable { mutableStateOf(State.GetStarted) }

    Animate(
        delayMillis = BOTTOM_ANIM_DELAY,
        durationMillis = BOTTOM_ANIM_DURATION,
        modifier = modifier,
    ) {
        Crossfade(
            label = "BottomCrossfade",
            targetState = currentState,
            animationSpec = tween(durationMillis = BOTTOM_CROSSFADE_ANIM_DURATION),
        ) { state ->
            when (state) {
                State.GetStarted -> GetStarted { changedState ->
                    currentState = changedState
                }

                State.Buttons -> Begin()
            }
        }
    }
}

@Composable
private fun GetStarted(onStartedClick: (State) -> Unit) {
    Column {
        GetStartedDescription()
        Spacer(modifier = Modifier.height(8.dp))
        GetStartedButton(onStartedClick)
    }
}

@Composable
private fun GetStartedDescription() {
    Text(
        text = LocalLoginStrings.current.getStartedDescription,
        textAlign = TextAlign.Justify,
    )
}

@Composable
private fun GetStartedButton(onStartedClick: (State) -> Unit) {
    val inlineArrow = "inlineArrowContent"
    val text = buildAnnotatedString {
        append(LocalLoginStrings.current.getStartedButton)
        append(' ')
        appendInlineContent(inlineArrow)
    }

    val translation by rememberInfiniteTransition(
        label = "InfiniteArrowTransition",
    ).animateValue(
        label = "ArrowTranslation",
        initialValue = Int.zero.dp,
        targetValue = 5.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = BOTTOM_ARROW_ANIM_DURATION,
                easing = LinearEasing,
            ),
            repeatMode = Reverse,
        ),
    )

    val inlineContent = mapOf(
        inlineArrow to InlineTextContent(
            Placeholder(
                width = MaterialTheme.typography.headlineSmall.fontSize,
                height = MaterialTheme.typography.headlineSmall.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
        ) {
            Icon(
                imageVector = Filled.ArrowForward,
                contentDescription = LocalLoginStrings.current.contentDescriptionGetStartedArrow,
                modifier = Modifier.offset(x = translation),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
    )

    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onStartedClick(Buttons) },
    ) {
        Text(
            text = text,
            inlineContent = inlineContent,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun Begin() {
    Column {
        BeginText()
        Spacer(modifier = Modifier.height(8.dp))
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val buttonWidth = maxWidth / 2 - 16.dp

            BeginRegisterButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(buttonWidth),
            )
            BeginLoginButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(buttonWidth),
            )
        }
    }
}

@Composable
private fun BeginText() {
    Text(
        text = LocalLoginStrings.current.beginDescription,
        textAlign = TextAlign.Justify,
    )
}

@Composable
private fun BeginRegisterButton(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    OutlinedButton(
        modifier = modifier,
        onClick = { uriHandler.openUri(ANILIST_REGISTER) },
    ) {
        Text(
            text = LocalLoginStrings.current.beginRegisterButton,
        )
    }
}

@Composable
private fun BeginLoginButton(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    Button(
        modifier = modifier,
        onClick = { uriHandler.openUri(ANILIST_LOGIN) },
    ) {
        Text(
            text = LocalLoginStrings.current.beginLoginButton,
        )
    }
}

@Composable
private fun Animate(
    delayMillis: Int,
    modifier: Modifier = Modifier,
    durationMillis: Int = BOTTOM_ANIM_DURATION,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    var animationFinished by rememberSaveable { mutableStateOf(false) }
    val animVisibleState = remember { MutableTransitionState(animationFinished) }.apply {
        targetState = true
    }

    LaunchedEffect(Unit) {
        animationFinished = true
    }

    AnimatedVisibility(
        modifier = modifier,
        visibleState = animVisibleState,
        enter = slideInVertically(
            animationSpec = tween(
                delayMillis = delayMillis,
                durationMillis = durationMillis,
            ),
        ) { height -> height / 2 } + fadeIn(
            animationSpec = tween(
                delayMillis = delayMillis,
                durationMillis = durationMillis,
            ),
            initialAlpha = Float.zero,
        ),
        content = content,
    )
}

private enum class State {
    GetStarted,
    Buttons,
}

private const val BACKGROUND_ALPHA = .3f
private const val LOGO_FULL_SIZE = 1f
private const val LOGO_RESIZED = .6f

private const val HEADER_ANIMATION_DELAY = 300
private const val HEADER_ANIMATION_DURATION = 1250

private const val BOTTOM_ANIM_DELAY = HEADER_ANIMATION_DELAY + HEADER_ANIMATION_DURATION / 2
private const val BOTTOM_ANIM_DURATION = HEADER_ANIMATION_DURATION
private const val BOTTOM_ARROW_ANIM_DURATION = 750
private const val BOTTOM_CROSSFADE_ANIM_DURATION = 800

private const val ANILIST_LOGIN = "https://anilist.co/api/v2/oauth/authorize?client_id=7275&response_type=token"
private const val ANILIST_REGISTER = "https://anilist.co/signup"
