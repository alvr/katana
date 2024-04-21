package dev.alvr.katana.features.lists.data.mappers.requests

import dev.alvr.katana.features.lists.data.MediaListEntriesMutation
import dev.alvr.katana.features.lists.domain.models.lists.MediaList

internal fun MediaList.toMutation() = MediaListEntriesMutation(
    id = id,
    score = score,
    progress = progress,
    progressVolumes = progressVolumes,
    repeat = repeat,
    private = private,
    notes = notes,
    hiddenFromStatusLists = hiddenFromStatusLists,
    startedAt = startedAt.toFuzzyDate().takeIfValid(),
    completedAt = completedAt.toFuzzyDate().takeIfValid(),
)
