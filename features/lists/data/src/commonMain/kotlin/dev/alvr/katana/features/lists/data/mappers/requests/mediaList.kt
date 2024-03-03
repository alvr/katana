package dev.alvr.katana.features.lists.data.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.features.lists.data.MediaListEntriesMutation
import dev.alvr.katana.features.lists.domain.models.lists.MediaList

internal fun MediaList.toMutation() = MediaListEntriesMutation(
    id = id,
    score = Optional.presentIfNotNull(score),
    progress = Optional.presentIfNotNull(progress),
    progressVolumes = Optional.presentIfNotNull(progressVolumes),
    repeat = Optional.presentIfNotNull(repeat),
    private = Optional.presentIfNotNull(private),
    notes = Optional.presentIfNotNull(notes),
    hiddenFromStatusLists = Optional.presentIfNotNull(hiddenFromStatusLists),
    startedAt = Optional.presentIfNotNull(startedAt.toFuzzyDate().takeIfValid()),
    completedAt = Optional.presentIfNotNull(completedAt.toFuzzyDate().takeIfValid()),
)
