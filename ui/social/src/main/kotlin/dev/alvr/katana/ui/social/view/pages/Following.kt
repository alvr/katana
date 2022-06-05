package dev.alvr.katana.ui.social.view.pages

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.alvr.katana.ui.social.R

@Composable
internal fun Following() {
    Text(text = stringResource(id = R.string.tab_following))
}
