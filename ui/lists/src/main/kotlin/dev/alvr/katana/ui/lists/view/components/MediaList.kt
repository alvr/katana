package dev.alvr.katana.ui.lists.view.components

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
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
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.entities.MediaListItem
import io.github.aakira.napier.Napier
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle

private val LocalMediaListItem =
    compositionLocalOf<MediaListItem> { error("No MediaListItem found!") }

@Composable
@OptIn(ExperimentalFoundationApi::class)
internal fun MediaList(
    items: List<MediaListItem>,
    modifier: Modifier = Modifier,
    addPlusOne: (Int) -> Unit = { Napier.d { "Adding +1 to $it" } },
    editEntry: (Int) -> Unit = { Napier.d { "Editing entry $it" } },
    openDetails: (Int) -> Unit = { Napier.d { "Opening details $it" } },
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = CARD_WIDTH),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = items,
            key = { it.mediaId },
        ) { item ->
            CompositionLocalProvider(LocalMediaListItem provides item) {
                MediaListItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth(),
                    addPlusOne = addPlusOne,
                    editEntry = editEntry,
                    openDetails = openDetails,
                )
            }
        }
    }
}

@Composable
@ExperimentalFoundationApi
private fun MediaListItem(
    addPlusOne: (Int) -> Unit,
    editEntry: (Int) -> Unit,
    openDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val entryId = LocalMediaListItem.current.entryId

    Card(
        modifier = modifier
            .height(144.dp)
            .combinedClickable(
                enabled = true,
                onClick = { openDetails(entryId) },
                onDoubleClick = { addPlusOne(entryId) },
                onLongClick = { editEntry(entryId) },
            ),
    ) {
        CardContent(addPlusOne = addPlusOne)
    }
}

@Composable
private fun CardContent(
    addPlusOne: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout {
        val (image, score, title, subtitle, plusOne, progress) = createRefs()

        Cover(
            modifier = modifier
                .widthIn(max = COVER_MAX_WIDTH)
                .fillMaxHeight()
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
        )

        Score(
            modifier = modifier.constrainAs(score) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            },
        )

        Title(
            modifier = modifier.constrainAs(title) {
                width = Dimension.fillToConstraints
                top.linkTo(parent.top, margin = 4.dp)
                start.linkTo(image.end, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
            },
        )

        Subtitle(
            modifier = modifier.constrainAs(subtitle) {
                width = Dimension.fillToConstraints
                top.linkTo(title.bottom, margin = 4.dp)
                start.linkTo(image.end, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
            },
        )

        PlusOne(
            addPlusOne = addPlusOne,
            modifier = modifier.constrainAs(plusOne) {
                width = Dimension.wrapContent
                bottom.linkTo(progress.top)
                end.linkTo(parent.end, margin = 8.dp)
            },
        )

        Progress(
            modifier = modifier.constrainAs(progress) {
                width = Dimension.fillToConstraints
                start.linkTo(image.end)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
        )
    }
}

@Composable
private fun Cover(
    modifier: Modifier = Modifier,
) {
    val cover = LocalMediaListItem.current.cover
    val title = LocalMediaListItem.current.title

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
    modifier: Modifier = Modifier,
) {
    val title = LocalMediaListItem.current.title

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
    modifier: Modifier = Modifier,
) {
    val item = LocalMediaListItem.current

    val text = buildAnnotatedString {
        append(stringResource(id = item.format.value))

        if (item is MediaListItem.AnimeListItem && item.nextEpisode != null) {
            append(" ${stringResource(id = R.string.next_episode_separator)} ")
            append(
                stringResource(
                    R.string.next_episode,
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

    if (score != DEFAULT_SCORE) {
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
    addPlusOne: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val item = LocalMediaListItem.current

    // Episodes - Chapters (Anime & Manga)
    if (item.progress != item.total) {
        Row(modifier = modifier) {
            PlusOneButton(
                progress = stringResource(id = R.string.progress, item.progress, item.total ?: "?"),
                addPlusOne = addPlusOne,
            )
        }
    }
}

@Composable
private fun PlusOneButton(
    progress: String,
    addPlusOne: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val entryId = LocalMediaListItem.current.entryId

    TextButton(
        onClick = { addPlusOne(entryId) },
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colors.onPrimary,
        ),
    ) {
        Text(text = stringResource(id = R.string.plus_one, progress))
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
        .appendLiteral(" ${stringResource(id = R.string.next_episode_date_time_separator)} ")
        .append(timePattern)
        .toFormatter(locale)
}

private val scoreFormatter = { score: Double ->
    DecimalFormat("0.#").format(score)
}

private val CARD_WIDTH = 384.dp
private val COVER_MAX_WIDTH = CARD_WIDTH / 4f

private const val DEFAULT_SCORE = 0.0
private const val PROGRESS_IF_UNKNOWN = .1f