package dev.alvr.katana.shared.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.shared.di.createSharedUiTestGraph
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.yield

internal class KatanaViewModelTest : BehaviorSpec() {
    private val observeActiveSession = mock<ObserveActiveSessionUseCase>()

    private lateinit var viewModel: KatanaViewModel

    init {
        given("an user") {
            `when`("observing if the user has an active session") {
                and("the user has an active session") {
                    then("it should update the state with the active session") {
                        every { observeActiveSession.flow } returns flowOf(true.right())

                        viewModel.test { expectState { copy(loading = false, sessionActive = true) } }

                        verifySuspend(mode = VerifyMode.exactly(1)) { observeActiveSession(Unit) }
                        verify(mode = VerifyMode.exactly(1)) { observeActiveSession.flow }
                    }

                    and("the session expires") {
                        then("it should update the state without the active session") {
                            every { observeActiveSession.flow } returns
                                flow {
                                    emit(true.right())
                                    yield()
                                    emit(false.right())
                                }

                            viewModel.test {
                                expectState { copy(loading = false, sessionActive = true) }
                                expectState { copy(loading = false, sessionActive = false) }
                            }

                            verifySuspend(mode = VerifyMode.exactly(1)) { observeActiveSession(Unit) }
                            verify(mode = VerifyMode.exactly(1)) { observeActiveSession.flow }
                        }
                    }
                }

                and("the user does not have an active session") {
                    then("it should update the state without the active session") {
                        every { observeActiveSession.flow } returns flowOf(false.right())

                        viewModel.test { expectState { copy(loading = false, sessionActive = false) } }

                        verifySuspend(mode = VerifyMode.exactly(1)) { observeActiveSession(Unit) }
                        verify(mode = VerifyMode.exactly(1)) { observeActiveSession.flow }
                    }
                }

                and("an error occurs") {
                    then("it should update the state without the active session") {
                        every { observeActiveSession.flow } returns flowOf(SessionFailure.CheckingActiveSession.left())

                        viewModel.test { expectState { copy(loading = false, sessionActive = false) } }

                        verifySuspend(mode = VerifyMode.exactly(1)) { observeActiveSession(Unit) }
                        verify(mode = VerifyMode.exactly(1)) { observeActiveSession.flow }
                    }
                }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        everySuspend { observeActiveSession(Unit) } returns Unit
        viewModel = createSharedUiTestGraph(observeActiveSessionUseCase = observeActiveSession).katanaViewModel
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetAnswers(observeActiveSession)
        resetCalls(observeActiveSession)
    }
}
