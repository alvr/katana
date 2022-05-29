package dev.alvr.katana.ui.lists.view.pages

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.alvr.katana.ui.lists.R

@Composable
internal fun AnimeList() {
    Text(text = stringResource(id = R.string.tab_anime))
}
