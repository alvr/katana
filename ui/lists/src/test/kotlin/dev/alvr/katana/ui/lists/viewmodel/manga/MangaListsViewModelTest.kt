package dev.alvr.katana.ui.lists.viewmodel.manga

import arrow.core.right
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.OneShot
import dev.alvr.katana.ui.lists.viewmodel.ListsState
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.test

internal class MangaListsViewModelTest : BehaviorSpec({
    val updateList = mockk<UpdateListUseCase>()
    val observeManga = mockk<ObserveMangaListUseCase>()

    given("a MangaListsViewModel") {
        val viewModel = MangaListsViewModel(updateList, observeManga).test(ListsState())

        `when`("initializing the viewModel") {
            and("the collection is empty") {
                every { observeManga.flow } returns flowOf(MediaCollection(lists = emptyList()))
                coJustRun { observeManga() }

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

            and("the collection has entries") {
                every { observeManga.flow } returns flowOf(
                    MediaCollection(
                        lists = listOf(
                            MediaListGroup(
                                name = String.empty,
                                entries = listOf(
                                    MediaListEntry(
                                        list = MediaList(
                                            id = Int.zero,
                                            score = Double.zero,
                                            progress = 234,
                                            progressVolumes = Int.zero,
                                            repeat = Int.zero,
                                            private = false,
                                            notes = String.empty,
                                            hiddenFromStatusLists = false,
                                            startedAt = LocalDate.MAX,
                                            completedAt = LocalDate.MAX,
                                            updatedAt = LocalDateTime.MAX,
                                        ),
                                        entry = MediaEntry.Manga(
                                            entry = CommonMediaEntry(
                                                id = Int.zero,
                                                title = String.empty,
                                                coverImage = String.empty,
                                                format = CommonMediaEntry.Format.ONE_SHOT,
                                            ),
                                            chapters = null,
                                            volumes = null,
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                )
                coJustRun { observeManga() }

                viewModel.runOnCreate()

                then("it should update it's state with the mapped entry") {
                    viewModel.assert(ListsState()) {
                        states(
                            { copy(isLoading = true) },
                            {
                                copy(
                                    isLoading = false,
                                    isEmpty = false,
                                    currentListItems = listOf(
                                        MediaListItem.MangaListItem(
                                            entryId = Int.zero,
                                            mediaId = Int.zero,
                                            title = String.empty,
                                            score = Double.zero,
                                            format = OneShot,
                                            cover = String.empty,
                                            progress = 234,
                                            volumesProgress = Int.zero,
                                            total = null,
                                            volumesTotal = null,
                                            repeat = Int.zero,
                                            private = false,
                                            notes = String.empty,
                                            hiddenFromStatusLists = false,
                                            startedAt = LocalDate.MAX,
                                            completedAt = LocalDate.MAX,
                                            updatedAt = LocalDateTime.MAX,
                                        ),
                                    ),
                                )
                            },
                        )
                    }

                    coVerify(exactly = 1) { observeManga() }
                    coVerify(exactly = 1) { observeManga.flow }
                }
            }
        }

        `when`("adding a +1 to an entry") {
            coEvery { updateList(any()) } returns Unit.right()

            then("it should call the updateList with the progress incremented by 1") {
                viewModel.testIntent {
                    addPlusOne(
                        MediaListItem.MangaListItem(
                            entryId = Int.zero,
                            mediaId = Int.zero,
                            title = String.empty,
                            score = Double.zero,
                            format = OneShot,
                            cover = String.empty,
                            progress = 233,
                            total = Int.zero,
                            volumesProgress = Int.zero,
                            volumesTotal = Int.zero,
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

                coVerify(exactly = 1) {
                    updateList(
                        MediaList(
                            id = Int.zero,
                            score = Double.zero,
                            progress = 234,
                            progressVolumes = Int.zero,
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
