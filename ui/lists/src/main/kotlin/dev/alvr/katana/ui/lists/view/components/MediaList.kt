package dev.alvr.katana.ui.lists.view.components

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.alvr.katana.common.core.unknown
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.ui.base.components.KatanaPullRefresh
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.viewmodel.ListState
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import kotlinx.collections.immutable.ImmutableList

private val LocalMediaListItem =
    compositionLocalOf<MediaListItem> { error("No MediaListItem found!") }

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
internal fun MediaList(
    listState: ListState<out MediaListItem>,
    onRefresh: () -> Unit,
    onAddPlusOne: (MediaListItem) -> Unit,
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
    onAddPlusOne: (MediaListItem) -> Unit,
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
            CompositionLocalProvider(LocalMediaListItem provides item) {
                MediaListItem(
                    modifier = Modifier.fillMaxWidth(),
                    onAddPlusOne = onAddPlusOne,
                    onEditEntry = onEditEntry,
                    onEntryDetails = onEntryDetails,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp)) // 56.dp FAB + 16.dp spacing bottom
        }
    }
}

@Composable
@ExperimentalFoundationApi
private fun MediaListItem(
    onAddPlusOne: (MediaListItem) -> Unit,
    onEditEntry: (Int) -> Unit,
    onEntryDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val entry = LocalMediaListItem.current

    Card(
        modifier = modifier
            .height(144.dp)
            .combinedClickable(
                onClick = { onEntryDetails(entry.entryId) },
                onDoubleClick = { onAddPlusOne(entry) },
                onLongClick = { onEditEntry(entry.entryId) },
            ),
    ) {
        CardContent(onAddPlusOne = onAddPlusOne)
    }
}

@Composable
private fun CardContent(
    onAddPlusOne: (MediaListItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout {
        val (image, score, title, subtitle, plusOne, progress) = createRefs()

        Cover(
            modifier = modifier
                .widthIn(max = COVER_MAX_WIDTH)
                .fillMaxHeight()
                .constrainAs(image) {
                    top.linkTo(anchor = parent.top)
                    start.linkTo(anchor = parent.start)
                    bottom.linkTo(anchor = parent.bottom)
                },
        )

        Score(
            modifier = modifier
                .constrainAs(score) {
                    start.linkTo(anchor = parent.start)
                    bottom.linkTo(anchor = parent.bottom)
                }
                .testTag(ITEM_SCORE_TAG),
        )

        Title(
            modifier = modifier
                .constrainAs(title) {
                    width = Dimension.fillToConstraints
                    top.linkTo(anchor = parent.top, margin = CONSTRAINT_VERTICAL_MARGIN)
                    start.linkTo(anchor = image.end, margin = CONSTRAINT_HORIZONTAL_MARGIN)
                    end.linkTo(anchor = parent.end, margin = CONSTRAINT_HORIZONTAL_MARGIN)
                }
                .testTag(ITEM_TITLE_TAG),
        )

        Subtitle(
            modifier = modifier
                .constrainAs(subtitle) {
                    width = Dimension.fillToConstraints
                    top.linkTo(anchor = title.bottom, margin = CONSTRAINT_VERTICAL_MARGIN)
                    start.linkTo(anchor = image.end, margin = CONSTRAINT_HORIZONTAL_MARGIN)
                    end.linkTo(anchor = parent.end, margin = CONSTRAINT_HORIZONTAL_MARGIN)
                }
                .testTag(ITEM_SUBTITLE_TAG),
        )

        PlusOne(
            onAddPlusOne = onAddPlusOne,
            modifier = modifier
                .constrainAs(plusOne) {
                    width = Dimension.wrapContent
                    end.linkTo(anchor = parent.end, margin = CONSTRAINT_HORIZONTAL_MARGIN)
                    bottom.linkTo(anchor = progress.top)
                }
                .testTag(ITEM_PLUSONE_TAG),
        )

        Progress(
            modifier = modifier.constrainAs(progress) {
                width = Dimension.fillToConstraints
                start.linkTo(anchor = image.end)
                end.linkTo(anchor = parent.end)
                bottom.linkTo(anchor = parent.bottom)
            },
        )
    }
}

@Composable
private fun Cover(
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(LocalMediaListItem.current.cover)
            .error(R.drawable.default_cover)
            .crossfade(true)
            .build(),
        contentDescription = LocalMediaListItem.current.title,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
) {
    Text(
        text = LocalMediaListItem.current.title,
        modifier = modifier,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun Subtitle(
    modifier: Modifier = Modifier,
) {
    val item = LocalMediaListItem.current

    val text = buildAnnotatedString {
        append(stringResource(item.format.value))

        if (item is MediaListItem.AnimeListItem && item.nextEpisode != null) {
            append(" ${stringResource(R.string.lists_entry_next_episode_separator)} ")
            append(
                stringResource(
                    R.string.lists_entry_next_episode,
                    item.nextEpisode.number,
                    item.nextEpisode.date.format(episodeFormatter()).toString(),
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
    modifier: Modifier = Modifier,
) {
    val score = LocalMediaListItem.current.score

    if (score != Double.zero) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(topEnd = 4.dp))
                .background(MaterialTheme.colors.surface.copy(alpha = .6f))
                .padding(4.dp)
                .defaultMinSize(minWidth = 18.dp),
        ) {
            Text(
                text = scoreFormatter(score),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun PlusOne(
    onAddPlusOne: (MediaListItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val item = LocalMediaListItem.current

    // Episodes - Chapters (Anime & Manga)
    if (item.progress != item.total) {
        PlusOneButton(
            progress = stringResource(R.string.lists_entry_progress, item.progress, item.total ?: String.unknown),
            onAddPlusOne = onAddPlusOne,
            modifier = modifier,
        )
    }
}

@Composable
private fun PlusOneButton(
    progress: String,
    onAddPlusOne: (MediaListItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val entry = LocalMediaListItem.current

    TextButton(
        onClick = { onAddPlusOne(entry) },
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colors.onPrimary,
        ),
    ) {
        Text(text = stringResource(R.string.lists_entry_plus_one, progress))
    }
}

@Composable
private fun Progress(
    modifier: Modifier = Modifier,
) {
    val progress = LocalMediaListItem.current.progress
    val total = LocalMediaListItem.current.total

    // For those entries where the total number of episodes/chapters is not known,
    // the progress bar is incomplete and is filled with about 90% of the current progress.
    val totalProgress = total ?: progress.plus(progress.times(PROGRESS_IF_UNKNOWN))
    val currentProgress = progress / totalProgress.toFloat()

    LinearProgressIndicator(
        progress = currentProgress,
        modifier = modifier,
    )
}

private val episodeFormatter = @Composable {
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        LocalConfiguration.current.locales[0]
    } else {
        @Suppress("DEPRECATION")
        LocalConfiguration.current.locale
    }

    val datePattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    val timePattern = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    DateTimeFormatterBuilder()
        .append(datePattern)
        .appendLiteral(" ${stringResource(R.string.lists_entry_next_episode_date_time_separator)} ")
        .append(timePattern)
        .toFormatter(locale)
}

private val scoreFormatter = { score: Double ->
    DecimalFormat("0.#").format(score)
}

private val CARD_WIDTH = 384.dp
private val COVER_MAX_WIDTH = CARD_WIDTH / 4f
private val ARRANGEMENT_SPACING = 8.dp
private val CONSTRAINT_VERTICAL_MARGIN = 4.dp
private val CONSTRAINT_HORIZONTAL_MARGIN = 8.dp

private const val PROGRESS_IF_UNKNOWN = .1f

internal const val ITEM_TITLE_TAG = "itemTitle"
internal const val ITEM_SUBTITLE_TAG = "itemSubtitle"
internal const val ITEM_SCORE_TAG = "itemScore"
internal const val ITEM_PLUSONE_TAG = "itemPlusOne"
