package dev.alvr.katana.ui.lists.content.anime

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.base.design.noInsets
import dev.alvr.katana.ui.lists.component.anime.AnimeListComponent

@Composable
fun AnimeListContent(
    component: AnimeListComponent,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.noInsets,
    ) { paddingValues ->
        Text("AnimeListContent")
    }
}
