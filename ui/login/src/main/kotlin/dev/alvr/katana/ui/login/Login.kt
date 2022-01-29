package dev.alvr.katana.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

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
    }
}

private const val BACKGROUND_ALPHA = .3f
