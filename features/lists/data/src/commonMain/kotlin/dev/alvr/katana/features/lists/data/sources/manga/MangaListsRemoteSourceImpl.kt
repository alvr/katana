package dev.alvr.katana.features.lists.data.sources.manga

import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.features.lists.data.sources.CommonListsRemoteSource
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import kotlin.jvm.JvmInline

@JvmInline
internal value class MangaListsRemoteSourceImpl(
    private val delegate: CommonListsRemoteSource,
) : MangaListsRemoteSource {
    override val mangaCollection get() = delegate.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA)
}
