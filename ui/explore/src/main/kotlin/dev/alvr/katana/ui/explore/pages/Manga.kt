package dev.alvr.katana.ui.explore.pages

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.alvr.katana.ui.explore.R

@Composable
internal fun Manga() {
    Text(text = stringResource(id = R.string.tab_manga))
}
