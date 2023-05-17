package dev.alvr.katana.data.remote.lists.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class ListsRepositoryTest : TestBase() {
    @MockK
    private lateinit var commonSource: CommonListsRemoteSource
    @MockK
    private lateinit var animeSource: AnimeListsRemoteSource
    @MockK
    private lateinit var mangaSource: MangaListsRemoteSource

    private lateinit var repo: ListsRepository

    override suspend fun beforeEach() {
        repo = ListsRepositoryImpl(commonSource, animeSource, mangaSource)
    }

    @Test
    @DisplayName("WHEN observing the anime collection THEN collecting the flow should get the same items")
    fun `observing the anime collection`() = runTest {
        // GIVEN
        val collection = MediaCollection<MediaEntry.Anime>(emptyList())
        every { animeSource.animeCollection } returns flowOf(collection.right())

        // WHEN
        repo.animeCollection.test(5.seconds) {
            awaitItem().shouldBeRight(collection)
            awaitComplete()
        }

        // THEN
        verify(exactly = 1) { animeSource.animeCollection }
    }

    @Test
    @DisplayName("WHEN observing the manga collection THEN collecting the flow should get the same items")
    fun `observing the manga collection`() = runTest {
        // GIVEN
        val collection = MediaCollection<MediaEntry.Manga>(emptyList())
        every { mangaSource.mangaCollection } returns flowOf(collection.right())

        // WHEN
        repo.mangaCollection.test(5.seconds) {
            awaitItem().shouldBeRight(collection)
            awaitComplete()
        }

        // THEN
        verify(exactly = 1) { mangaSource.mangaCollection }
    }

    @Test
    @DisplayName("WHEN updating the list without error THEN the result should be right")
    fun `updating the list without error`() = runTest {
        // GIVEN
        coEitherJustRun { commonSource.updateList(any()) }

        // WHEN
        val result = repo.updateList(mockk())

        // THEN
        result.shouldBeRight()
        coVerify(exactly = 1) { commonSource.updateList(any()) }
    }

    @Test
    @DisplayName("WHEN updating the list with an error AND the error is a ListsFailure THEN the result should be left")
    fun `updating the list with an error AND the error is a ListsFailure`() = runTest {
        // GIVEN
        coEvery { commonSource.updateList(any()) } returns ListsFailure.UpdatingList.left()

        // WHEN
        val result = repo.updateList(mockk())

        // THEN
        result.shouldBeLeft(ListsFailure.UpdatingList)
        coVerify(exactly = 1) { commonSource.updateList(any()) }
    }

    @Test
    @DisplayName("WHEN updating the list with an error AND the error in unknown THEN the result should be left")
    fun `updating the list with an error AND the error in unknown`() = runTest {
        // GIVEN
        coEvery { commonSource.updateList(any()) } returns Failure.Unknown.left()

        // WHEN
        val result = repo.updateList(mockk())

        // THEN
        result.shouldBeLeft(Failure.Unknown)
        coVerify(exactly = 1) { commonSource.updateList(any()) }
    }
}
