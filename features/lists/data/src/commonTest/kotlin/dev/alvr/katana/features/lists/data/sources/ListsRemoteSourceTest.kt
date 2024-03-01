package dev.alvr.katana.features.lists.data.sources

import app.cash.turbine.test
import arrow.core.right
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.features.lists.data.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.features.lists.data.sources.anime.AnimeListsRemoteSourceImpl
import dev.alvr.katana.features.lists.data.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.features.lists.data.sources.manga.MangaListsRemoteSourceImpl
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class ListsRemoteSourceTest : FreeSpec() {
    private val source = mock<CommonListsRemoteSource>()

    private val animeSource: AnimeListsRemoteSource = AnimeListsRemoteSourceImpl(source)
    private val mangaSource: MangaListsRemoteSource = MangaListsRemoteSourceImpl(source)

    init {
        "the data is null" {
            every { source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME) } returns flowOf(
                MediaCollection<MediaEntry.Anime>(emptyList()).right(),
            )
            every { source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA) } returns flowOf(
                MediaCollection<MediaEntry.Manga>(emptyList()).right(),
            )

            animeSource.animeCollection.test(5.seconds) {
                awaitItem().shouldBeRight(MediaCollection(emptyList()))
                cancelAndIgnoreRemainingEvents()
            }

            mangaSource.mangaCollection.test(5.seconds) {
                awaitItem().shouldBeRight(MediaCollection(emptyList()))
                cancelAndIgnoreRemainingEvents()
            }

            verify {
                source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME)
                source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA)
            }
        }
    }
}
