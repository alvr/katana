package dev.alvr.katana.ui.social.content

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.base.design.noInsets
import dev.alvr.katana.ui.social.component.SocialComponent

@Composable
fun SocialContent(
    component: SocialComponent,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.noInsets,
    ) { paddingValues ->
        Text("SocialContent")
    }
}
