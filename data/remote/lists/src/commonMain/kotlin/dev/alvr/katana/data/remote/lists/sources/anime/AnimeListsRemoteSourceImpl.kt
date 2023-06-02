package dev.alvr.katana.data.remote.lists.sources.anime

import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.domain.lists.models.entries.MediaEntry

@JvmInline
internal value class AnimeListsRemoteSourceImpl(
    private val delegate: CommonListsRemoteSource,
) : AnimeListsRemoteSource {
    override val animeCollection get() = delegate.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME)
}
