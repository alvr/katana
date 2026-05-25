package dev.alvr.katana.features.home.ui.screens.activity.viewmodel

import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.home.ui.di.createActivityUiTestGraph
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import io.mockk.clearAllMocks

internal class ActivityViewModelTest : BehaviorSpec() {
    private lateinit var viewModel: ActivityViewModel

    init {
        given("an intent") {
            beforeEach { viewModel = createActivityUiTestGraph() }

            and("is of type ActivityIntent") {
                `when`("intent ActivityIntent") {
                    then("it should do nothing (yet)") { viewModel.test { intent(ActivityIntent) } }
                }
            }
        }
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }
}
