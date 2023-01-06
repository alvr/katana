package dev.alvr.katana.data.remote.lists.sources

import app.cash.turbine.test
import arrow.core.right
import com.apollographql.apollo3.annotations.ApolloExperimental
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSourceImpl
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifyAll
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ApolloExperimental
@ExperimentalCoroutinesApi
internal class ListsRemoteSourceTest : TestBase() {
    @MockK
    private lateinit var source: CommonListsRemoteSource

    private lateinit var animeSource: AnimeListsRemoteSource
    private lateinit var mangaSource: MangaListsRemoteSource

    override suspend fun beforeEach() {
        animeSource = AnimeListsRemoteSourceImpl(source)
        mangaSource = MangaListsRemoteSourceImpl(source)
    }

    @Test
    @DisplayName("WHEN querying the list AND the data is empty THEN the collection should be empty")
    fun `the data is null`() = runTest {
        // GIVEN
        every { source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME) } returns flowOf(
            MediaCollection<MediaEntry.Anime>(emptyList()).right(),
        )
        every { source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA) } returns flowOf(
            MediaCollection<MediaEntry.Manga>(emptyList()).right(),
        )

        // WHEN
        val resultAnime = animeSource.animeCollection
        val resultManga = mangaSource.mangaCollection

        // THEN
        resultAnime.test(5.seconds) {
            awaitItem().shouldBeRight(MediaCollection(emptyList()))
            cancelAndIgnoreRemainingEvents()
        }

        resultManga.test(5.seconds) {
            awaitItem().shouldBeRight(MediaCollection(emptyList()))
            cancelAndIgnoreRemainingEvents()
        }

        verifyAll {
            source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME)
            source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA)
        }
    }
}
