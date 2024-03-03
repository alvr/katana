package dev.alvr.katana.features.social.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.components.home.KatanaHomeScaffold
import dev.alvr.katana.core.ui.navigation.Destination
import dev.alvr.katana.features.social.ui.strings.LocalSocialStrings

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
