package dev.alvr.katana.shared.viewmodel

import arrow.core.none
import arrow.core.some
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.core.domain.usecases.sync
import dev.alvr.katana.core.tests.orbitTestScope
import dev.alvr.katana.core.ui.destinations.RootDestination
import io.kotest.core.NamedTag
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.orbitmvi.orbit.test.test

internal class MainViewModelTest : BehaviorSpec() {
    private val getAnilistToken = mockk<GetAnilistTokenUseCase>()

    private lateinit var viewModel: MainViewModel

    init {
        given("a logged in user") {
            `when`("the user does have a saved token") {
                then("the initial navGraph should be `RootScreen.Home`") {
                    viewModel.test(orbitTestScope) {
                        runOnCreate()
                        expectState(MainState(initialScreen = RootDestination.Home))
                    }

                    verify(exactly = 1) { getAnilistToken.sync() }
                }
            }
        }

        given("a logged out user") {
            `when`("the user does not have a saved token") {
                then("the initial screen should be `RootScreen.Auth`").config(tags = setOf(LOGGED_OUT_TEST)) {
                    viewModel.test(orbitTestScope) {
                        runOnCreate()
                        expectState(MainState(initialScreen = RootDestination.Auth))
                    }

                    verify(exactly = 1) { getAnilistToken.sync() }
                }
            }
        }
    }

    override suspend fun beforeTest(testCase: TestCase) {
        clearMocks(getAnilistToken)

        every { getAnilistToken.sync() } answers {
            if (testCase.config.tags.contains(LOGGED_OUT_TEST)) {
                none()
            } else {
                AnilistToken("TOKEN").some()
            }
        }

        viewModel = MainViewModel(getAnilistToken)
    }

    private companion object {
        val LOGGED_OUT_TEST = NamedTag("loggedOut")
    }
}
