package dev.alvr.katana.ui.social

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import dev.alvr.katana.ui.base.components.home.HomeScaffold
import dev.alvr.katana.ui.base.components.home.HomeTopAppBar
import dev.alvr.katana.ui.social.pages.Following
import dev.alvr.katana.ui.social.pages.Global

@Preview
@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
fun Social() {
    HomeScaffold(
        tabs = enumValues<SocialTabs>(),
        backContent = { },
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
