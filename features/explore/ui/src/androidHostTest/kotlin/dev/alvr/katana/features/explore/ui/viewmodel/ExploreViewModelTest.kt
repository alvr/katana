package dev.alvr.katana.features.explore.ui.viewmodel

import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.explore.ui.di.createExploreUiTestGraph
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.mockk.clearAllMocks

internal class ExploreViewModelTest : BehaviorSpec() {
    private lateinit var viewModel: ExploreViewModel

    init {
        given("a ExploreViewModel") {
            then("it should have an initial state") {
                viewModel.test {
                    // only for run init()
                }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        clearAllMocks()
        viewModel = createExploreUiTestGraph().exploreViewModel
    }
}
