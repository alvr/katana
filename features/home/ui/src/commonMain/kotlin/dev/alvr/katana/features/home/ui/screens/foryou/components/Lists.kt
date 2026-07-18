package dev.alvr.katana.features.home.ui.screens.foryou.components

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.core.ui.modifiers.katanaPlaceholder
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.symbols.ArrowForward
import dev.alvr.katana.core.ui.symbols.KatanaSymbols
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.viewmodel.SectionStatus
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.media_section_empty
import dev.alvr.katana.features.home.ui.resources.media_section_error
import dev.alvr.katana.features.home.ui.resources.media_type_anime
import dev.alvr.katana.features.home.ui.resources.media_type_manga
import dev.alvr.katana.features.home.ui.screens.foryou.entities.HomeMediaItem
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun Lists(
    title: String,
    status: SectionStatus<ImmutableList<HomeMediaItem>>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    iconButtonContentDescription: String? = null,
    onNavigateClick: (() -> Unit)? = null,
    selectedType: MediaListType? = null,
    onTypeSelect: ((MediaListType) -> Unit)? = null,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.spacing2)) {
        Header(
            title = title,
            iconButtonContentDescription = iconButtonContentDescription,
            onNavigateClick = onNavigateClick,
            selectedType = selectedType,
            onTypeSelect = onTypeSelect,
        )

        when (status) {
            SectionStatus.Loading -> LoadingRow()
            is SectionStatus.Error -> ErrorRow(onRetry = onRetry)
            is SectionStatus.Success if status.data.isEmpty() -> EmptyRow()
            is SectionStatus.Success if status.data.isNotEmpty() -> MediaRow(items = status.data)
            else -> Unit
        }
    }
}

@Composable
private fun Header(
    title: String,
    iconButtonContentDescription: String?,
    onNavigateClick: (() -> Unit)?,
    selectedType: MediaListType?,
    onTypeSelect: ((MediaListType) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, style = KatanaTheme.typography.headlineSmall)

        Spacer(Modifier.weight(1f))

        if (selectedType != null && onTypeSelect != null) {
            MediaTypeSelector(selectedType = selectedType, onTypeSelect = onTypeSelect)
        }

        if (iconButtonContentDescription != null && onNavigateClick != null) {
            IconButton(onClick = onNavigateClick) {
                Icon(imageVector = KatanaSymbols.ArrowForward, contentDescription = iconButtonContentDescription)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MediaTypeSelector(selectedType: MediaListType, onTypeSelect: (MediaListType) -> Unit) {
    SingleChoiceSegmentedButtonRow {
        MediaListType.entries.forEachIndexed { index, type ->
            SegmentedButton(
                selected = selectedType == type,
                onClick = { onTypeSelect(type) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = MediaListType.entries.size),
                label = {
                    Text(
                        text =
                            when (type) {
                                MediaListType.Anime -> Res.string.media_type_anime.value
                                MediaListType.Manga -> Res.string.media_type_manga.value
                            }
                    )
                },
            )
        }
    }
}

@Composable
private fun LoadingRow() {
    val lazyListState = rememberLazyListState()

    LazyRow(
        state = lazyListState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState, SnapPosition.Start),
        horizontalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.itemSpacing),
    ) {
        items(count = LoadingItems, key = { index -> "loading_$index" }) { MediaCardPlaceholder() }
    }
}

@Composable
private fun MediaRow(items: ImmutableList<HomeMediaItem>) {
    val lazyListState = rememberLazyListState()

    LazyRow(
        state = lazyListState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState, SnapPosition.Start),
        horizontalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.itemSpacing),
    ) {
        items(items = items, key = { item -> item.id.value }, contentType = { item -> item.type }) { item ->
            MediaCard(item = item, modifier = Modifier.animateItem())
        }
    }
}

@Composable
private fun ErrorRow(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(KatanaTheme.sizes.size18),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.spacing2),
    ) {
        Text(text = Res.string.media_section_error.value, modifier = Modifier.weight(1f))
        OutlinedButton(onClick = onRetry) { Text(text = "Error") }
    }
}

@Composable
private fun EmptyRow(modifier: Modifier = Modifier) {
    Box(modifier = modifier.height(KatanaTheme.sizes.size18), contentAlignment = Alignment.CenterStart) {
        Text(text = Res.string.media_section_empty.value)
    }
}

@Composable
private fun MediaCard(item: HomeMediaItem, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier.width(KatanaTheme.sizes.size32)) {
        Column {
            AsyncImage(
                modifier = Modifier.width(KatanaTheme.sizes.size32).height(KatanaTheme.sizes.size48),
                model = item.cover,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
            )

            Text(
                text = item.title,
                modifier = Modifier.padding(KatanaTheme.dimensions.spacing1),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = KatanaTheme.typography.bodyMedium,
            )

            if (item.progress != null && item.total != null && item.total > 0) {
                LinearProgressIndicator(progress = { item.progress.toFloat() / item.total.toFloat() })
            }
        }
    }
}

@Composable
private fun MediaCardPlaceholder(modifier: Modifier = Modifier) {
    Column(modifier = modifier.width(KatanaTheme.sizes.size32)) {
        Box(
            modifier =
                Modifier.width(KatanaTheme.sizes.size32)
                    .height(KatanaTheme.sizes.size48)
                    .clip(RoundedCornerShape(KatanaTheme.sizes.size2))
                    .katanaPlaceholder(visible = true)
        )
        Spacer(Modifier.height(KatanaTheme.sizes.size2))
        Box(
            modifier =
                Modifier.width(KatanaTheme.sizes.size32)
                    .height(KatanaTheme.sizes.size4)
                    .katanaPlaceholder(visible = true)
        )
        Spacer(Modifier.height(KatanaTheme.sizes.size1))
        Box(
            modifier =
                Modifier.width(KatanaTheme.sizes.size18)
                    .height(KatanaTheme.sizes.size4)
                    .katanaPlaceholder(visible = true)
        )
    }
}

private const val LoadingItems = 6
