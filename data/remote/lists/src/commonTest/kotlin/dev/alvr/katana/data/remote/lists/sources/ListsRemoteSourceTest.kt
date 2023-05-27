package dev.alvr.katana.data.remote.lists.sources

import app.cash.turbine.test
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSourceImpl
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(CommonListsRemoteSource::class)
internal class ListsRemoteSourceTest : FreeSpec() {
    private val mocker = Mocker()

    private val source = MockCommonListsRemoteSource(mocker)
    private val animeSource: AnimeListsRemoteSource = AnimeListsRemoteSourceImpl(source)
    private val mangaSource: MangaListsRemoteSource = MangaListsRemoteSourceImpl(source)

    init {
        "the data is null" {
            mocker.every { source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME) } returns flowOf(
                MediaCollection<MediaEntry.Anime>(emptyList()).right(),
            )
            mocker.every { source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA) } returns flowOf(
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

            mocker.verify {
                source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME)
                source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA)
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
