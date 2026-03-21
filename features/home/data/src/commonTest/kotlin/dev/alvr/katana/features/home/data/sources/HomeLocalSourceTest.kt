package dev.alvr.katana.features.home.data.sources

import app.cash.turbine.test
import dev.alvr.katana.core.preferences.di.store.KatanaStore
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.home.data.entities.HomePreferences
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.io.IOException

internal class HomeLocalSourceTest : FreeSpec() {
    private val store = mock<KatanaStore<HomePreferences>> { every { data } returns emptyFlow() }

    private lateinit var source: HomeLocalSource

    init {
        "successful" -
            {
                every { store.data } returns
                    flowOf(
                        HomePreferences(welcomeCardVisible = false),
                        HomePreferences(welcomeCardVisible = false),
                        HomePreferences(welcomeCardVisible = true),
                        HomePreferences(welcomeCardVisible = true),
                    )

                "getting the welcome card visibility" {
                    source.welcomeCardVisible.test {
                        awaitItem().shouldBeRight(false)
                        awaitItem().shouldBeRight(true)
                        awaitComplete()
                    }

                    verify { store.data }
                }

                "hiding the welcome card" {
                    everySuspend { store.update(any()) } returns HomePreferences()
                    source.hideWelcomeCard().shouldBeRight()
                    verifySuspend { store.update(any()) }
                }
            }

        "failure" -
            {
                "getting the welcome card visibility fails AND it's a common Exception" {
                    every { store.data } returns flow { throw IllegalStateException() }
                    source = HomeLocalSourceImpl(store)

                    source.welcomeCardVisible.test {
                        awaitItem().shouldBeLeft(HomeFailure.GettingWelcomeCardVisibility)
                        awaitComplete()
                    }

                    verify { store.data }
                }

                "getting the welcome card visibility fails AND it's a reading Exception" {
                    every { store.data } returns flow { throw IOException("Oops.") }
                    source = HomeLocalSourceImpl(store)

                    source.welcomeCardVisible.test {
                        awaitItem().shouldBeLeft(HomeFailure.GettingWelcomeCardVisibility)
                        awaitComplete()
                    }

                    verify { store.data }
                }

                "hiding the welcome card fails AND it's a common Exception" {
                    everySuspend { store.update(any()) } throws Exception()
                    source.hideWelcomeCard().shouldBeLeft(HomeFailure.HidingWelcomeCard)
                    verifySuspend { store.update(any()) }
                }

                "hiding the welcome card fails AND it's a writing Exception" {
                    everySuspend { store.update(any()) } throws IOException("Oops.")
                    source.hideWelcomeCard().shouldBeLeft(HomeFailure.HidingWelcomeCard)
                    verifySuspend { store.update(any()) }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        source = HomeLocalSourceImpl(store)
    }
}
