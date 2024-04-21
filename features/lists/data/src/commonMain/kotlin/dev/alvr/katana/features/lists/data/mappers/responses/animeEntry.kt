package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.data.fragment.MediaEntry as MediaEntryFragment

internal fun MediaEntryFragment.animeEntry() = MediaEntry.Anime(
    entry = mediaEntry(),
    episodes = episodes,
    nextEpisode = nextAiringEpisode?.let { next ->
        MediaEntry.Anime.NextEpisode(
            number = next.episode,
            at = next.airingAt.toLocalDateTime(),
        )
    },
)
