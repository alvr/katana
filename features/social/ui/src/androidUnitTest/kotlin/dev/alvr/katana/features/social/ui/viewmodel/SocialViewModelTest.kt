package dev.alvr.katana.features.social.ui.viewmodel

import dev.alvr.katana.core.tests.orbitTestScope
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import org.orbitmvi.orbit.test.test

internal class SocialViewModelTest : BehaviorSpec() {
    private lateinit var viewModel: SocialViewModel

    init {
        given("a SocialViewModel") {
            then("it should have an initial state") {
                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                }
            }
        }
    }

    override suspend fun beforeTest(testCase: TestCase) {
        viewModel = SocialViewModel()
    }
}
