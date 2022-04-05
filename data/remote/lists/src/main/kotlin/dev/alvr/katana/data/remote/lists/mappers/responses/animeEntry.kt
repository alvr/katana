package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal fun MediaEntryFragment?.animeEntry() = let { entry ->
    MediaEntry.Anime(
        entry = mediaEntry(),
        episodes = entry?.episodes,
        nextEpisode = entry?.nextAiringEpisode?.let { next ->
            MediaEntry.Anime.NextEpisode(
                number = next.episode,
                at = next.airingAt.toLocalDateTime()
            )
        }
    )
}
