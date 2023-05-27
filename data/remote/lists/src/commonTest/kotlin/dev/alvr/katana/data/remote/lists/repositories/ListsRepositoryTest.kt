package dev.alvr.katana.data.remote.lists.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.MockCommonListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.MockAnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MockMangaListsRemoteSource
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.fakeMediaList
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf
import org.kodein.mock.Mocker
import org.kodein.mock.UsesFakes
import org.kodein.mock.UsesMocks

@UsesFakes(MediaList::class)
@UsesMocks(
    CommonListsRemoteSource::class,
    AnimeListsRemoteSource::class,
    MangaListsRemoteSource::class,
)
internal class ListsRepositoryTest : FreeSpec() {
    private val mocker = Mocker()
    private val commonSource = MockCommonListsRemoteSource(mocker)
    private val animeSource = MockAnimeListsRemoteSource(mocker)
    private val mangaSource = MockMangaListsRemoteSource(mocker)

    private val repo: ListsRepository = ListsRepositoryImpl(commonSource, animeSource, mangaSource)

    init {
        "collecting anime collection flow" {
            val collection = MediaCollection<MediaEntry.Anime>(emptyList())
            mocker.every { animeSource.animeCollection } returns flowOf(collection.right())

            repo.animeCollection.test(5.seconds) {
                awaitItem().shouldBeRight(collection)
                awaitComplete()
            }

            mocker.verify { animeSource.animeCollection }
        }

        "collecting manga collection flow" {
            val collection = MediaCollection<MediaEntry.Manga>(emptyList())
            mocker.every { mangaSource.mangaCollection } returns flowOf(collection.right())

            repo.mangaCollection.test(5.seconds) {
                awaitItem().shouldBeRight(collection)
                awaitComplete()
            }

            mocker.verify { mangaSource.mangaCollection }
        }

        "successfully updating list" {
            mocker.everySuspending { commonSource.updateList(isAny()) } returns Unit.right()
            repo.updateList(fakeMediaList()).shouldBeRight(Unit)
            mocker.verifyWithSuspend { commonSource.updateList(isAny()) }
        }

        listOf(
            ListsFailure.UpdatingList to ListsFailure.UpdatingList.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure updating the list ($expected)" {
                mocker.everySuspending { repo.updateList(isAny()) } returns failure
                repo.updateList(fakeMediaList()).shouldBeLeft(expected)
                mocker.verifyWithSuspend { repo.updateList(fakeMediaList()) }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
