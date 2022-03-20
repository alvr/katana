package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.domain.lists.models.AnimeEntry
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal fun MediaListCollectionQuery.Media?.animeEntry() = let { entry ->
    AnimeEntry(
        entry = mediaEntry(),
        episodes = entry?.episodes,
        nextEpisode = entry?.nextAiringEpisode?.let { next ->
            AnimeEntry.NextEpisode(
                number = next.episode,
                timeUntilAiring = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(next.airingAt.toLong()),
                    ZoneId.systemDefault()
                )
            )
        }
    )
}
