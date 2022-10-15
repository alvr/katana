package dev.alvr.katana.data.remote.lists.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.common.tests.valueMockk
import dev.alvr.katana.data.remote.lists.sources.ListsRemoteSource
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class ListsRepositoryTest : BehaviorSpec() {
    private val source = mockk<ListsRemoteSource>()
    private val repo: ListsRepository = ListsRepositoryImpl(source)

    init {
        given("a ListsRepository") {
            `when`("observing the anime collection") {
                val collection = valueMockk<MediaCollection<MediaEntry.Anime>>()
                every { source.animeCollection } returns flowOf(collection.right())

                then("collecting the flow should get the same items") {
                    repo.animeCollection.test(5.seconds) {
                        awaitItem().shouldBeRight(collection)
                        awaitComplete()
                    }
                    verify(exactly = 1) { source.animeCollection }
                }
            }

            `when`("observing the manga collection") {
                val collection = valueMockk<MediaCollection<MediaEntry.Manga>>()
                every { source.mangaCollection } returns flowOf(collection.right())

                then("collecting the flow should get the same items") {
                    repo.mangaCollection.test(5.seconds) {
                        awaitItem().shouldBeRight(collection)
                        awaitComplete()
                    }
                    verify(exactly = 1) { source.mangaCollection }
                }
            }

            `when`("updating the list without error") {
                coEitherJustRun { source.updateList(any()) }

                then("the result should be right") {
                    repo.updateList(mockk()).shouldBeRight()
                    coVerify(exactly = 1) { source.updateList(any()) }
                }
            }

            `when`("updating the list with an error") {
                and("the error is a ListsFailure") {
                    coEvery { source.updateList(any()) } returns ListsFailure.UpdatingList.left()

                    then("the result should be left") {
                        repo.updateList(mockk()).shouldBeLeft(ListsFailure.UpdatingList)
                        coVerify(exactly = 1) { source.updateList(any()) }
                    }
                }

                and("the error in unknown") {
                    coEvery { source.updateList(any()) } returns Failure.Unknown.left()

                    then("the result should be left") {
                        repo.updateList(mockk()).shouldBeLeft(Failure.Unknown)
                        coVerify(exactly = 1) { source.updateList(any()) }
                    }
                }
            }
        }
    }
}
