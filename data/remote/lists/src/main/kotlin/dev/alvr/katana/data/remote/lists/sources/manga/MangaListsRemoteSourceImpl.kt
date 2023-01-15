package dev.alvr.katana.data.remote.lists.sources.manga

import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.domain.lists.models.entries.MediaEntry

@JvmInline
internal value class MangaListsRemoteSourceImpl(
    private val delegate: CommonListsRemoteSource,
) : MangaListsRemoteSource {
    override val mangaCollection get() = delegate.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA)
}
