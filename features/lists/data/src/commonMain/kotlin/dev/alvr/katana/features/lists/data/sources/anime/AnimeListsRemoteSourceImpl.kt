package dev.alvr.katana.features.lists.data.sources.anime

import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.features.lists.data.sources.CommonListsRemoteSource
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import kotlin.jvm.JvmInline

@JvmInline
internal value class AnimeListsRemoteSourceImpl(
    private val delegate: CommonListsRemoteSource,
) : AnimeListsRemoteSource {
    override val animeCollection get() = delegate.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME)
}
