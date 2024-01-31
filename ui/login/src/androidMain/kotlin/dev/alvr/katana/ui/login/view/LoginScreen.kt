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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
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
import dev.alvr.katana.ui.login.login.generated.resources.Res
import dev.alvr.katana.ui.login.login.generated.resources.background_chihiro
import dev.alvr.katana.ui.login.login.generated.resources.background_howl
import dev.alvr.katana.ui.login.login.generated.resources.background_mononoke
import dev.alvr.katana.ui.login.login.generated.resources.background_totoro
import dev.alvr.katana.ui.login.login.generated.resources.begin_description
import dev.alvr.katana.ui.login.login.generated.resources.begin_login_button
import dev.alvr.katana.ui.login.login.generated.resources.begin_register_button
import dev.alvr.katana.ui.login.login.generated.resources.content_description_background
import dev.alvr.katana.ui.login.login.generated.resources.content_description_get_started_arrow
import dev.alvr.katana.ui.login.login.generated.resources.content_description_katana_logo
import dev.alvr.katana.ui.login.login.generated.resources.fetch_user_id_error
import dev.alvr.katana.ui.login.login.generated.resources.get_started_button
import dev.alvr.katana.ui.login.login.generated.resources.get_started_description
import dev.alvr.katana.ui.login.login.generated.resources.header_katana_description
import dev.alvr.katana.ui.login.login.generated.resources.ic_katana_logo
import dev.alvr.katana.ui.login.login.generated.resources.save_token_error
import dev.alvr.katana.ui.login.navigation.LoginNavigator
import dev.alvr.katana.ui.login.viewmodel.LoginState
import dev.alvr.katana.ui.login.viewmodel.LoginViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Destination(
    deepLinks = [
        DeepLink(uriPattern = LOGIN_DEEP_LINK),
    ],
)
internal fun LoginScreen(
    token: String?,
    navigator: LoginNavigator,
    vm: LoginViewModel = koinViewModel { parametersOf(token) },
) {
    val state by vm.collectAsState()

    Login(
        state = state,
        onLogin = navigator::toHome,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
private fun Login(state: LoginState, onLogin: () -> Unit) {
    val background = remember {
        listOf(
            Res.drawable.background_chihiro,
            Res.drawable.background_howl,
            Res.drawable.background_mononoke,
            Res.drawable.background_totoro,
        ).random()
    }

    val scaffoldState = rememberScaffoldState()

    when {
        state.saved -> onLogin()
        state.errorType != null -> {
            val message = when (state.errorType) {
                LoginState.ErrorType.SaveToken -> Res.string.save_token_error
                LoginState.ErrorType.SaveUserId -> Res.string.fetch_user_id_error
            }.let { res -> stringResource(res) }
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
                .padding(padding)
                .consumeWindowInsets(padding),
        ) {
            if (state.loading) {
                Loading()
            } else {
                Image(
                    painter = painterResource(background),
                    contentDescription = stringResource(Res.string.content_description_background),
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
@OptIn(ExperimentalResourceApi::class)
private fun KatanaLogo() {
    val configuration = LocalConfiguration.current
    val sizeFraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LOGO_RESIZED
    } else {
        LOGO_FULL_SIZE
    }

    Image(
        painter = painterResource(Res.drawable.ic_katana_logo),
        contentDescription = stringResource(Res.string.content_description_katana_logo),
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(fraction = sizeFraction),
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
private fun Description() {
    Text(
        text = stringResource(Res.string.header_katana_description),
        style = MaterialTheme.typography.headlineSmall,
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
@OptIn(ExperimentalResourceApi::class)
private fun GetStartedDescription() {
    Text(
        text = stringResource(Res.string.get_started_description),
        textAlign = TextAlign.Justify,
    )
}

@Composable
private fun GetStartedButton(onStartedClick: (State) -> Unit) {
    val inlineArrow = "inlineArrowContent"
    val text = buildAnnotatedString {
        append(stringResource(Res.string.get_started_button))
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
                width = MaterialTheme.typography.headlineSmall.fontSize,
                height = MaterialTheme.typography.headlineSmall.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(Res.string.content_description_get_started_arrow),
                modifier = Modifier.offset(x = translation),
                tint = MaterialTheme.colorScheme.onSurface,
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
@OptIn(ExperimentalResourceApi::class)
private fun BeginText() {
    Text(
        text = stringResource(Res.string.begin_description),
        textAlign = TextAlign.Justify,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
private fun BeginRegisterButton(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    OutlinedButton(
        modifier = modifier,
        onClick = { uriHandler.openUri(ANILIST_REGISTER) },
    ) {
        Text(
            text = stringResource(Res.string.begin_register_button),
        )
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
private fun BeginLoginButton(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    Button(
        modifier = modifier,
        onClick = { uriHandler.openUri(ANILIST_LOGIN) },
    ) {
        Text(
            text = stringResource(Res.string.begin_login_button),
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
