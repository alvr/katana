package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.domain.lists.models.entries.AnimeEntry
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal fun MediaEntryFragment?.animeEntry() = let { entry ->
    AnimeEntry(
        entry = mediaEntry(),
        episodes = entry?.episodes,
        nextEpisode = entry?.nextAiringEpisode?.let { next ->
            AnimeEntry.NextEpisode(
                number = next.episode,
                at = next.airingAt.toLocalDateTime()
            )
        }
    )
}
