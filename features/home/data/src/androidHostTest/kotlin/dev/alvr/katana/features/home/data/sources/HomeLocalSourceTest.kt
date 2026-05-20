package dev.alvr.katana.features.home.data.sources

import app.cash.turbine.test
import dev.alvr.katana.core.preferences.utils.flow
import dev.alvr.katana.core.preferences.utils.set
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.home.data.di.createHomeLocalSourceTestGraph
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import eu.anifantakis.lib.ksafe.KSafe
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.io.IOException

@Suppress("UnusedFlow")
internal class HomeLocalSourceTest : FreeSpec() {
    private val safe = mockk<KSafe>()

    private lateinit var source: HomeLocalSource

    init {
        "successful" -
            {
                "getting the welcome card visibility" {
                    every { safe.flow(WelcomeCardVisiblePrefKey, any<Boolean>()) } returns
                        flowOf(false, false, true, true)
                    source = createHomeLocalSourceTestGraph(safe).homeLocalSource

                    source.welcomeCardVisible.test {
                        awaitItem().shouldBeRight(false)
                        awaitItem().shouldBeRight(true)
                        awaitComplete()
                    }

                    verify { safe.getFlow<Boolean>(WelcomeCardVisiblePrefKey.toString(), true) }
                }

                "hiding the welcome card" {
                    every { safe.flow(WelcomeCardVisiblePrefKey, any<Boolean>()) } returns flowOf(true)
                    justRun { safe[WelcomeCardVisiblePrefKey] = any() }

                    source.hideWelcomeCard().shouldBeRight()

                    verify { safe.putDirect<Boolean>(WelcomeCardVisiblePrefKey.toString(), false) }
                }
            }

        "failure" -
            {
                "getting the welcome card visibility fails AND it's a common Exception" {
                    every { safe.flow(WelcomeCardVisiblePrefKey, any<Boolean>()) } returns
                        flow { throw IllegalStateException() }
                    source = createHomeLocalSourceTestGraph(safe).homeLocalSource

                    source.welcomeCardVisible.test {
                        awaitItem().shouldBeLeft(HomeFailure.GettingWelcomeCardVisibility)
                        awaitComplete()
                    }

                    verify { safe.getFlow<Boolean>(WelcomeCardVisiblePrefKey.toString(), true) }
                }

                "getting the welcome card visibility fails AND it's a reading Exception" {
                    every { safe.flow(WelcomeCardVisiblePrefKey, any<Boolean>()) } returns
                        flow { throw IOException("Oops.") }
                    source = createHomeLocalSourceTestGraph(safe).homeLocalSource

                    source.welcomeCardVisible.test {
                        awaitItem().shouldBeLeft(HomeFailure.GettingWelcomeCardVisibility)
                        awaitComplete()
                    }

                    verify { safe.getFlow<Boolean>(WelcomeCardVisiblePrefKey.toString(), true) }
                }

                "hiding the welcome card fails AND it's a common Exception" {
                    every { safe[WelcomeCardVisiblePrefKey] = any() } throws Exception()
                    source.hideWelcomeCard().shouldBeLeft(HomeFailure.HidingWelcomeCard)
                    verify { safe.putDirect<Boolean>(WelcomeCardVisiblePrefKey.toString(), false) }
                }

                "hiding the welcome card fails AND it's a writing Exception" {
                    every { safe[WelcomeCardVisiblePrefKey] = any() } throws IOException("Oops.")
                    source.hideWelcomeCard().shouldBeLeft(HomeFailure.HidingWelcomeCard)
                    verify { safe.putDirect<Boolean>(WelcomeCardVisiblePrefKey.toString(), false) }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        every { safe.flow(WelcomeCardVisiblePrefKey, any<Boolean>()) } returns flowOf(true)
        source = createHomeLocalSourceTestGraph(safe).homeLocalSource
    }
}
