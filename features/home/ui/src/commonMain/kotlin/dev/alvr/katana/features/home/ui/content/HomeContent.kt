package dev.alvr.katana.features.home.ui.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBar
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBarType
import dev.alvr.katana.core.ui.theme.noInsets
import dev.alvr.katana.features.home.ui.component.HomeComponent
import dev.alvr.katana.features.home.ui.component.HomeComponent.Child
import dev.alvr.katana.features.home.ui.navigation.HomeNavigationBar
import kotlinx.collections.immutable.toImmutableList

@Composable
fun HomeContent(
    component: HomeComponent,
    modifier: Modifier = Modifier,
) {
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.configuration
    val items = remember { HomeNavigationBar.entries.toImmutableList() }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.noInsets,
        bottomBar = {
            KatanaNavigationBar(
                items = items,
                isSelected = { it.key == activeComponent::class },
                onClick = component::onNavigationBarItemClicked,
                type = KatanaNavigationBarType.Bottom,
            )
        },
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            KatanaNavigationBar(
                items = items,
                isSelected = { it.key == activeComponent::class },
                onClick = component::onNavigationBarItemClicked,
                type = KatanaNavigationBarType.Rail,
            )

            HomeChildren(stack = stack)
        }
    }
}

@Composable
private fun HomeChildren(
    stack: ChildStack<*, Child>,
    modifier: Modifier = Modifier,
) {
    Children(
        modifier = modifier,
        stack = stack,
        animation = stackAnimation(),
    ) { (_, instance) ->
        when (instance) {
            is Child.AnimeListChild -> Text("AnimeList")
            is Child.MangaListChild -> Text("MangaList")
            is Child.ExploreChild -> Text("Explore")
            is Child.SocialChild -> Text("Social")
            is Child.AccountChild -> Text("Account")
        }
    }
}
