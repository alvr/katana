package dev.alvr.katana.ui.login.view

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.ui.base.viewmodel.collectAsState
import dev.alvr.katana.ui.login.ANILIST_LOGIN
import dev.alvr.katana.ui.login.ANILIST_REGISTER
import dev.alvr.katana.ui.login.BACKGROUND_ALPHA
import dev.alvr.katana.ui.login.BOTTOM_ANIM_DELAY
import dev.alvr.katana.ui.login.BOTTOM_ANIM_DURATION
import dev.alvr.katana.ui.login.BOTTOM_ARROW_ANIM_DURATION
import dev.alvr.katana.ui.login.BOTTOM_CROSSFADE_ANIM_DURATION
import dev.alvr.katana.ui.login.GET_STARTED_BUTTON_TAG
import dev.alvr.katana.ui.login.HEADER_ANIMATION_DELAY
import dev.alvr.katana.ui.login.HEADER_ANIMATION_DURATION
import dev.alvr.katana.ui.login.LOGIN_DEEP_LINK
import dev.alvr.katana.ui.login.LOGO_FULL_SIZE
import dev.alvr.katana.ui.login.LOGO_RESIZED
import dev.alvr.katana.ui.login.R
import dev.alvr.katana.ui.login.navigation.LoginNavigator
import dev.alvr.katana.ui.login.strings.LocalLoginStrings
import dev.alvr.katana.ui.login.viewmodel.LoginState
import dev.alvr.katana.ui.login.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination(
    deepLinks = [
        DeepLink(uriPattern = LOGIN_DEEP_LINK),
    ],
)
internal fun LoginScreen(
    navigator: LoginNavigator,
    vm: LoginViewModel = koinViewModel(),
) {
    val state by vm.collectAsState()

    Login(
        state = state,
        onLogin = navigator::toHome,
    )
}

@Composable
private fun Login(state: LoginState, onLogin: () -> Unit) {
    val strings = LocalLoginStrings.current
    var loading by remember { mutableStateOf(false) }

    val background = rememberSaveable {
        listOf(
            R.drawable.background_chihiro,
            R.drawable.background_howl,
            R.drawable.background_mononoke,
            R.drawable.background_totoro,
        ).random()
    }

    val scaffoldState = rememberScaffoldState()

    when {
        state.saved -> onLogin()
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

    Scaffold(scaffoldState = scaffoldState) { padding ->
        Surface(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (loading) {
                Loading()
            } else {
                Image(
                    painter = painterResource(background),
                    contentDescription = strings.contentDescriptionBackground,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.alpha(BACKGROUND_ALPHA),
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
internal fun Header(modifier: Modifier = Modifier) {
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
    val configuration = LocalConfiguration.current
    val sizeFraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LOGO_RESIZED
    } else {
        LOGO_FULL_SIZE
    }

    Image(
        painter = painterResource(R.drawable.ic_katana_logo),
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
        style = MaterialTheme.typography.h5,
    )
}

@Composable
internal fun Bottom(modifier: Modifier = Modifier) {
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
            repeatMode = RepeatMode.Reverse,
        ),
    )

    val inlineContent = mapOf(
        inlineArrow to InlineTextContent(
            Placeholder(
                width = MaterialTheme.typography.h5.fontSize,
                height = MaterialTheme.typography.h5.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = LocalLoginStrings.current.contentDescriptionGetStartedArrow,
                modifier = Modifier.offset(x = translation),
                tint = MaterialTheme.colors.onSurface,
            )
        },
    )

    TextButton(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(GET_STARTED_BUTTON_TAG),
        onClick = { onStartedClick(State.Buttons) },
    ) {
        Text(
            text = text,
            inlineContent = inlineContent,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSurface,
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
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        onClick = { uriHandler.openUri(ANILIST_REGISTER) },
    ) {
        Text(
            text = LocalLoginStrings.current.beginRegisterButton,
            color = MaterialTheme.colors.onSurface,
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
            color = MaterialTheme.colors.onSurface,
        )
    }
}

@Composable
private fun Animate(
    delayMillis: Int,
    durationMillis: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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
    ) {
        content()
    }
}

private enum class State {
    GetStarted,
    Buttons,
}
