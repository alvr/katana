package dev.alvr.katana.ui.lists.viewmodel

import arrow.core.right
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.OneShot
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.test

internal class ListsBaseViewModelTest : BehaviorSpec({
    val updateList = mockk<UpdateListUseCase>()

    given("a ListsBaseViewModel") {
        val viewModel = object : ListsBaseViewModel<MediaEntry, MediaListItem>(updateList) {
            override val listsFlow: Flow<MediaCollection<MediaEntry>> = flowOf(MediaCollection(lists = emptyList()))
            override fun List<MediaListGroup<MediaEntry>>.listMapper() = emptyList<MediaListItem>()
        }.test(ListsState())

        `when`("initializing the viewModel") {
            viewModel.runOnCreate()

            then("it should update it's state") {
                viewModel.assert(ListsState()) {
                    states(
                        { copy(isLoading = true) },
                        { copy(isLoading = false, isEmpty = true, currentListItems = emptyList()) },
                    )
                }
            }
        }

        `when`("adding a +1 to an entry") {
            coEvery { updateList(any()) } returns Unit.right()

            then("it should call the updateList with the progress incremented by 1") {
                viewModel.testIntent {
                    addPlusOne(
                        MediaListItem.AnimeListItem(
                            entryId = Int.zero,
                            mediaId = Int.zero,
                            title = String.empty,
                            score = Double.zero,
                            format = OneShot,
                            cover = String.empty,
                            progress = 233,
                            total = Int.zero,
                            repeat = Int.zero,
                            private = false,
                            notes = String.empty,
                            hiddenFromStatusLists = false,
                            startedAt = LocalDate.MAX,
                            completedAt = LocalDate.MAX,
                            updatedAt = LocalDateTime.MAX,
                            nextEpisode = null,
                        ),
                    )
                }

                coVerify(exactly = 1) {
                    updateList(
                        MediaList(
                            id = Int.zero,
                            score = Double.zero,
                            progress = 234,
                            progressVolumes = null,
                            repeat = Int.zero,
                            private = false,
                            notes = String.empty,
                            hiddenFromStatusLists = false,
                            startedAt = LocalDate.MAX,
                            completedAt = LocalDate.MAX,
                            updatedAt = LocalDateTime.MAX,
                        ),
                    )
                }
            }
        }
    }
},)
