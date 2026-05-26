package dev.alvr.katana.features.home.ui.screens.activity.viewmodel

import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.home.ui.di.createActivityUiTestGraph
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult

internal class ActivityViewModelTest : BehaviorSpec() {
    private lateinit var viewModel: ActivityViewModel

    init {
        given("an ActivityViewModel") {
            then("it should have an initial state") {
                viewModel.test {
                    // only for run init()
                }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        viewModel = createActivityUiTestGraph().activityViewModel
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetAnswers()
        resetCalls()
    }
}
