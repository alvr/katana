package dev.alvr.katana.features.login.ui.screen

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
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.ui.resources.asPainter
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.screens.AuthScreen
import dev.alvr.katana.core.ui.screens.RootScreen
import dev.alvr.katana.core.ui.utils.isLandscape
import dev.alvr.katana.core.ui.utils.navDeepLink
import dev.alvr.katana.core.ui.utils.noInsets
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.login.ui.ANILIST_LOGIN
import dev.alvr.katana.features.login.ui.ANILIST_REGISTER
import dev.alvr.katana.features.login.ui.BACKGROUND_ALPHA
import dev.alvr.katana.features.login.ui.BOTTOM_ANIM_DELAY
import dev.alvr.katana.features.login.ui.BOTTOM_ANIM_DURATION
import dev.alvr.katana.features.login.ui.BOTTOM_ARROW_ANIM_DURATION
import dev.alvr.katana.features.login.ui.BOTTOM_CROSSFADE_ANIM_DURATION
import dev.alvr.katana.features.login.ui.GET_STARTED_BUTTON_TAG
import dev.alvr.katana.features.login.ui.HEADER_ANIMATION_DELAY
import dev.alvr.katana.features.login.ui.HEADER_ANIMATION_DURATION
import dev.alvr.katana.features.login.ui.LOGIN_DEEP_LINK
import dev.alvr.katana.features.login.ui.LOGO_FULL_SIZE
import dev.alvr.katana.features.login.ui.LOGO_RESIZED
import dev.alvr.katana.features.login.ui.navigation.LoginNavigator
import dev.alvr.katana.features.login.ui.resources.Res
import dev.alvr.katana.features.login.ui.resources.background_chihiro
import dev.alvr.katana.features.login.ui.resources.background_howl
import dev.alvr.katana.features.login.ui.resources.background_mononoke
import dev.alvr.katana.features.login.ui.resources.background_totoro
import dev.alvr.katana.features.login.ui.resources.begin_description
import dev.alvr.katana.features.login.ui.resources.begin_login_button
import dev.alvr.katana.features.login.ui.resources.begin_register_button
import dev.alvr.katana.features.login.ui.resources.content_description_background
import dev.alvr.katana.features.login.ui.resources.content_description_get_started_arrow
import dev.alvr.katana.features.login.ui.resources.content_description_katana_logo
import dev.alvr.katana.features.login.ui.resources.fetch_user_id_error
import dev.alvr.katana.features.login.ui.resources.get_started_button
import dev.alvr.katana.features.login.ui.resources.get_started_description
import dev.alvr.katana.features.login.ui.resources.header_katana_description
import dev.alvr.katana.features.login.ui.resources.ic_katana_logo
import dev.alvr.katana.features.login.ui.resources.save_token_error
import dev.alvr.katana.features.login.ui.viewmodel.LoginState
import dev.alvr.katana.features.login.ui.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.login(
    loginNavigator: LoginNavigator,
) {
    navigation<RootScreen.Auth>(
        startDestination = AuthScreen.Login(),
    ) {
        composable<AuthScreen.Login>(
            deepLinks = listOf(navDeepLink { setUriPattern(LOGIN_DEEP_LINK) }),
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<AuthScreen.Login>()

            LoginScreen(
                token = route.token,
                loginNavigator = loginNavigator,
            )
        }
    }
}

@Composable
@OptIn(KoinExperimentalAPI::class)
private fun LoginScreen(
    token: String?,
    loginNavigator: LoginNavigator,
    viewModel: LoginViewModel = koinViewModel { parametersOf(token) },
) {
    val state by viewModel.collectAsState()

    Login(
        state = state,
        onNavigateToHome = loginNavigator::navigateToHome,
    )
}

@Composable
private fun Login(state: LoginState, onNavigateToHome: () -> Unit) {
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
        state.saved -> onNavigateToHome()
        state.errorType != null -> {
            val message = when (state.errorType) {
                LoginState.ErrorType.SaveToken -> Res.string.save_token_error
                LoginState.ErrorType.SaveUserId -> Res.string.fetch_user_id_error
            }.value
            LaunchedEffect(state.errorType) {
                scaffoldState.snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.noInsets,
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (state.loading) {
                Loading()
            } else {
                Image(
                    painter = background.asPainter,
                    contentDescription = Res.string.content_description_background.value,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(BACKGROUND_ALPHA),
                )

                Box(
                    modifier = Modifier.padding(horizontal = 24.dp)
                        .systemBarsPadding()
                        .displayCutoutPadding(),
                ) {
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
    val sizeFraction = if (isLandscape()) {
        LOGO_RESIZED
    } else {
        LOGO_FULL_SIZE
    }

    Image(
        painter = Res.drawable.ic_katana_logo.asPainter,
        contentDescription = Res.string.content_description_katana_logo.value,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(fraction = sizeFraction),
    )
}

@Composable
private fun Description() {
    Text(
        text = Res.string.header_katana_description.value,
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
private fun GetStartedDescription() {
    Text(
        text = Res.string.get_started_description.value,
        textAlign = TextAlign.Justify,
    )
}

@Composable
private fun GetStartedButton(onStartedClick: (State) -> Unit) {
    val inlineArrow = "inlineArrowContent"
    val text = buildAnnotatedString {
        append(Res.string.get_started_button.value)
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
                contentDescription = Res.string.content_description_get_started_arrow.value,
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
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
private fun BeginText() {
    Text(
        text = Res.string.begin_description.value,
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
            text = Res.string.begin_register_button.value,
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
            text = Res.string.begin_login_button.value,
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
