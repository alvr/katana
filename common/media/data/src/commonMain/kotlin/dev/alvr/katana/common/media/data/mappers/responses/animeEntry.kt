package dev.alvr.katana.common.media.data.mappers.responses

import dev.alvr.katana.common.media.data.fragment.MediaEntry as MediaEntryFragment
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry

internal fun MediaEntryFragment.animeEntry() =
    MediaEntry.Anime(
        entry = mediaEntry(),
        episodes = episodes,
        nextEpisode =
            nextAiringEpisode?.let { next ->
                MediaEntry.Anime.NextEpisode(number = next.episode, at = next.airingAt.toLocalDateTime())
            },
    )
