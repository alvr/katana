package dev.alvr.katana.features.explore.ui.viewmodel

import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.explore.ui.di.createExploreUiTestGraph
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult

internal class ExploreViewModelTest : BehaviorSpec() {
    private lateinit var viewModel: ExploreViewModel

    init {
        given("an ExploreViewModel") {
            then("it should have an initial state") {
                viewModel.test {
                    // only for run init()
                }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        viewModel = createExploreUiTestGraph().exploreViewModel
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetAnswers()
        resetCalls()
    }
}
