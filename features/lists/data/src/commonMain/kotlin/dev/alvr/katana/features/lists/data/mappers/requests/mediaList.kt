package dev.alvr.katana.features.lists.data.mappers.requests

import dev.alvr.katana.core.remote.optional
import dev.alvr.katana.features.lists.data.MediaListEntriesMutation
import dev.alvr.katana.features.lists.domain.models.lists.MediaList

internal fun MediaList.toMutation() = MediaListEntriesMutation(
    id = id,
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
