package dev.alvr.katana.data.remote.lists.sources.manga

import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("UseDataClass")
internal class MangaListsRemoteSourceImpl @Inject constructor(
    private val delegate: CommonListsRemoteSource,
) : MangaListsRemoteSource {
    override val mangaCollection get() = delegate.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA)
}
