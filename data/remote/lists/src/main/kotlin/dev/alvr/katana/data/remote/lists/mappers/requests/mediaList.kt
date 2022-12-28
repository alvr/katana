package dev.alvr.katana.data.remote.lists.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.data.remote.lists.MediaListEntriesMutation
import dev.alvr.katana.domain.lists.models.lists.MediaList

internal fun MediaList.toMutation() = MediaListEntriesMutation(
    id = id,
    score = Optional.presentIfNotNull(score),
    progress = Optional.presentIfNotNull(progress),
    progressVolumes = Optional.presentIfNotNull(progressVolumes),
    repeat = Optional.presentIfNotNull(repeat),
    private = Optional.presentIfNotNull(private),
    notes = Optional.presentIfNotNull(notes),
    hiddenFromStatusLists = Optional.presentIfNotNull(hiddenFromStatusLists),
    startedAt = Optional.present(startedAt.toFuzzyDate().takeIfValid()),
    completedAt = Optional.present(completedAt.toFuzzyDate().takeIfValid()),
)
