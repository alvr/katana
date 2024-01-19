package dev.alvr.katana.ui.home.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import dev.alvr.katana.ui.account.content.AccountContent
import dev.alvr.katana.ui.base.components.navigation.KatanaNavigationBar
import dev.alvr.katana.ui.base.components.navigation.KatanaNavigationBarType
import dev.alvr.katana.ui.base.design.noInsets
import dev.alvr.katana.ui.explore.content.ExploreContent
import dev.alvr.katana.ui.home.component.HomeComponent
import dev.alvr.katana.ui.home.component.HomeComponent.Child
import dev.alvr.katana.ui.home.component.HomeComponent.Child.AccountChild
import dev.alvr.katana.ui.home.component.HomeComponent.Child.AnimeListChild
import dev.alvr.katana.ui.home.component.HomeComponent.Child.ExploreChild
import dev.alvr.katana.ui.home.component.HomeComponent.Child.MangaListChild
import dev.alvr.katana.ui.home.component.HomeComponent.Child.SocialChild
import dev.alvr.katana.ui.home.navigation.HomeNavigationBar
import dev.alvr.katana.ui.lists.content.anime.AnimeListContent
import dev.alvr.katana.ui.lists.content.manga.MangaListContent
import dev.alvr.katana.ui.social.content.SocialContent
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
            is AnimeListChild -> AnimeListContent(instance.component)
            is MangaListChild -> MangaListContent(instance.component)
            is ExploreChild -> ExploreContent(instance.component)
            is SocialChild -> SocialContent(instance.component)
            is AccountChild -> AccountContent(instance.component)
        }
    }
}
