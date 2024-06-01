package dev.alvr.katana.features.explore.ui.viewmodel

import dev.alvr.katana.core.tests.orbitTestScope
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import org.orbitmvi.orbit.test.test

internal class ExploreViewModelTest : BehaviorSpec() {
    private lateinit var viewModel: ExploreViewModel

    init {
        given("a ExploreViewModel") {
            then("it should have an initial state") {
                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                }
            }
        }
    }

    override suspend fun beforeTest(testCase: TestCase) {
        viewModel = ExploreViewModel()
    }
}
