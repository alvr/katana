package dev.alvr.katana.data.remote.lists.sources.anime

import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("UseDataClass")
internal class AnimeListsRemoteSourceImpl @Inject constructor(
    private val delegate: CommonListsRemoteSource,
) : AnimeListsRemoteSource {
    override val animeCollection get() = delegate.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME)
}
