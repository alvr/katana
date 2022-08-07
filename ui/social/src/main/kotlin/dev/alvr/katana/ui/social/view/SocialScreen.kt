package dev.alvr.katana.ui.social.view

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import dev.alvr.katana.ui.base.components.home.HomeScaffold
import dev.alvr.katana.ui.base.components.home.HomeTopAppBar
import dev.alvr.katana.ui.social.R
import dev.alvr.katana.ui.social.view.pages.Following
import dev.alvr.katana.ui.social.view.pages.Global

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
internal fun Social() {
    HomeScaffold(
        tabs = enumValues<SocialTabs>(),
        backContent = { _, _ -> Text(text = "TODO") },
        pageContent = { page ->
            when (page) {
                SocialTabs.Following -> Following()
                SocialTabs.Global -> Global()
            }
        },
    )
}

@Immutable
private enum class SocialTabs(override val label: Int) : HomeTopAppBar {
    Following(R.string.tab_following),
    Global(R.string.tab_global),
}
