package dev.alvr.katana.features.lists.data.mappers.requests

import dev.alvr.katana.common.media.domain.models.lists.MediaList
import dev.alvr.katana.core.remote.optional
import dev.alvr.katana.features.lists.data.MediaListEntriesMutation

internal fun MediaList.toMutation() =
    MediaListEntriesMutation(
        id = id.value,
        score = score.optional,
        progress = progress.optional,
        progressVolumes = progressVolumes.optional,
        repeat = repeat.optional,
        private = private.optional,
        notes = notes.optional,
        hiddenFromStatusLists = hiddenFromStatusLists.optional,
        startedAt = startedAt?.toFuzzyDate().optional,
        completedAt = completedAt?.toFuzzyDate().optional,
    )
