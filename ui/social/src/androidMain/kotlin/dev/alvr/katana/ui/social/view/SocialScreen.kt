package dev.alvr.katana.ui.social.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import dev.alvr.katana.ui.base.components.home.KatanaHomeScaffold
import dev.alvr.katana.ui.social.strings.LocalSocialStrings

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
internal fun SocialScreen() {
    val strings = LocalSocialStrings.current

    KatanaHomeScaffold(
        title = strings.socialToolbarTitle,
        searchPlaceholder = strings.socialToolbarSearchPlaceholder,
        onSearch = {},
        backContent = { Filter() },
    ) { paddingValues ->
        Text(
            modifier = Modifier.padding(paddingValues),
            text = strings.socialToolbarTitle,
        )
    }
}

@Composable
private fun Filter() {
    Text(text = "Social Filter")
}
