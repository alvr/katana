package dev.alvr.katana.ui.lists.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.alvr.katana.common.core.unknown
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.domain.base.formatters.KatanaDateFormatter
import dev.alvr.katana.domain.base.formatters.KatanaNumberFormatter
import dev.alvr.katana.ui.base.components.KatanaPullRefresh
import dev.alvr.katana.ui.base.modifiers.katanaPlaceholder
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.viewmodel.ListState
import kotlinx.collections.immutable.ImmutableList

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
internal fun MediaList(
    listState: ListState<out MediaListItem>,
    onRefresh: () -> Unit,
    onAddPlusOne: (Int) -> Unit,
    onEditEntry: (Int) -> Unit,
    onEntryDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
) {
    KatanaPullRefresh(
        modifier = modifier,
        loading = listState.isLoading,
        onRefresh = onRefresh,
    ) {
        MediaList(
            lazyGridState = lazyGridState,
            items = listState.items,
            itemLoading = listState.isLoading,
            modifier = Modifier.fillMaxSize(),
            onAddPlusOne = onAddPlusOne,
            onEditEntry = onEditEntry,
            onEntryDetails = onEntryDetails,
        )
    }
}

@Composable
@ExperimentalFoundationApi
private fun MediaList(
    lazyGridState: LazyGridState,
    items: ImmutableList<MediaListItem>,
    itemLoading: Boolean,
    onAddPlusOne: (Int) -> Unit,
    onEditEntry: (Int) -> Unit,
    onEntryDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        state = lazyGridState,
        modifier = modifier,
        columns = GridCells.Adaptive(CARD_WIDTH),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(ARRANGEMENT_SPACING),
        horizontalArrangement = Arrangement.spacedBy(ARRANGEMENT_SPACING),
    ) {
        items(
            items = items,
            key = { it.mediaId },
        ) { item ->
            MediaListItem(
                item = item,
                modifier = Modifier.fillMaxWidth(),
                itemLoading = itemLoading,
                onAddPlusOne = { onAddPlusOne(item.entryId) },
                onEditEntry = { onEditEntry(item.entryId) },
                onEntryDetails = { onEntryDetails(item.entryId) },
            )
        }

        item {
            Spacer(modifier = Modifier.height(72.dp)) // 56.dp FAB + 16.dp spacing bottom
        }
    }
}

@Composable
@ExperimentalFoundationApi
private fun MediaListItem(
    item: MediaListItem,
    itemLoading: Boolean,
    onAddPlusOne: () -> Unit,
    onEditEntry: () -> Unit,
    onEntryDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .height(144.dp)
            .combinedClickable(
                onClick = onEntryDetails,
                onDoubleClick = onAddPlusOne,
                onLongClick = onEditEntry,
            ),
    ) {
        CardContent(
            item = item,
            itemLoading = itemLoading,
            onAddPlusOne = onAddPlusOne,
        )
    }
}

@Composable
private fun CardContent(
    item: MediaListItem,
    itemLoading: Boolean,
    onAddPlusOne: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        CoverAndScore(
            cover = item.cover,
            score = item.score,
            title = item.title,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight()
                .katanaPlaceholder(
                    visible = itemLoading,
                    shape = RectangleShape,
                ),
        )

        Column(
            modifier = Modifier
                .padding(top = CONTENT_TOP_PADDING)
                .fillMaxHeight(),
        ) {
            Title(
                title = item.title,
                modifier = Modifier
                    .padding(start = CONTENT_HORIZONTAL_PADDING)
                    .testTag(ITEM_TITLE_TAG)
                    .katanaPlaceholder(visible = itemLoading),
            )

            Subtitle(
                format = item.format,
                nextEpisode = (item as? MediaListItem.AnimeListItem)?.nextEpisode,
                modifier = Modifier
                    .padding(
                        start = CONTENT_HORIZONTAL_PADDING,
                        top = CONTENT_TOP_PADDING,
                    )
                    .testTag(ITEM_SUBTITLE_TAG)
                    .katanaPlaceholder(visible = itemLoading),
            )

            Spacer(modifier = Modifier.weight(1f))

            PlusOne(
                progress = item.progress,
                total = item.total,
                itemLoading = itemLoading,
                onAddPlusOne = onAddPlusOne,
                modifier = Modifier
                    .padding(end = CONTENT_HORIZONTAL_PADDING)
                    .align(Alignment.End)
                    .testTag(ITEM_PLUSONE_TAG),
            )

            Progress(
                progress = item.progress,
                total = item.total,
                modifier = Modifier
                    .fillMaxWidth()
                    .katanaPlaceholder(
                        visible = itemLoading,
                        shape = RectangleShape,
                    ),
            )
        }
    }
}

@Composable
private fun CoverAndScore(
    cover: String,
    score: Double,
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.widthIn(max = COVER_MAX_WIDTH),
    ) {
        Cover(
            cover = cover,
            title = title,
            modifier = Modifier.fillMaxHeight(),
        )

        Score(
            score = score,
            modifier = Modifier
                .align(AbsoluteAlignment.BottomLeft)
                .testTag(ITEM_SCORE_TAG),
        )
    }
}

@Composable
private fun Cover(
    cover: String,
    title: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(cover)
            .error(R.drawable.default_cover)
            .crossfade(true)
            .build(),
        contentDescription = title,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun Title(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun Subtitle(
    format: MediaListItem.Format,
    modifier: Modifier = Modifier,
    nextEpisode: MediaListItem.AnimeListItem.NextEpisode? = null,
) {
    val text = buildAnnotatedString {
        append(stringResource(format.value))

        if (nextEpisode != null) {
            append(" ${stringResource(R.string.lists_entry_next_episode_separator)} ")
            append(
                stringResource(
                    R.string.lists_entry_next_episode,
                    nextEpisode.number,
                    KatanaDateFormatter.DateWithTime(nextEpisode.date),
                ),
            )
        }
    }

    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.caption,
    )
}

@Composable
private fun Score(
    score: Double,
    modifier: Modifier = Modifier,
) {
    if (score != Double.zero) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(topEnd = 4.dp))
                .background(MaterialTheme.colors.surface.copy(alpha = .6f))
                .padding(4.dp)
                .defaultMinSize(minWidth = 18.dp),
        ) {
            Text(
                text = KatanaNumberFormatter.Score(score),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun PlusOne(
    progress: Int,
    total: Int?,
    itemLoading: Boolean,
    onAddPlusOne: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Episodes - Chapters (Anime & Manga)
    if (progress != total) {
        PlusOneButton(
            progress = stringResource(
                R.string.lists_entry_progress,
                progress,
                total ?: String.unknown,
            ),
            itemLoading = itemLoading,
            onAddPlusOne = onAddPlusOne,
            modifier = modifier,
        )
    }
}

@Composable
private fun PlusOneButton(
    progress: String,
    itemLoading: Boolean,
    onAddPlusOne: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onAddPlusOne,
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colors.onPrimary,
        ),
    ) {
        Text(
            modifier = Modifier.katanaPlaceholder(visible = itemLoading),
            text = stringResource(R.string.lists_entry_plus_one, progress),
        )
    }
}

@Composable
private fun Progress(
    progress: Int,
    total: Int?,
    modifier: Modifier = Modifier,
) {
    // For those entries where the total number of episodes/chapters is not known,
    // the progress bar is incomplete and is filled with about 90% of the current progress.
    val totalProgress = (total ?: progress.plus(progress.times(PROGRESS_IF_UNKNOWN))).toFloat()
    val currentProgress = if (progress == Int.zero && totalProgress == Float.zero) {
        Float.zero
    } else {
        progress / totalProgress
    }

    LinearProgressIndicator(
        progress = currentProgress,
        modifier = modifier,
    )
}

private val CARD_WIDTH = 384.dp
private val COVER_MAX_WIDTH = CARD_WIDTH / 4f
private val ARRANGEMENT_SPACING = 8.dp
private val CONTENT_TOP_PADDING = 4.dp
private val CONTENT_HORIZONTAL_PADDING = 8.dp

private const val PROGRESS_IF_UNKNOWN = .1f

internal const val ITEM_TITLE_TAG = "itemTitle"
internal const val ITEM_SUBTITLE_TAG = "itemSubtitle"
internal const val ITEM_SCORE_TAG = "itemScore"
internal const val ITEM_PLUSONE_TAG = "itemPlusOne"
