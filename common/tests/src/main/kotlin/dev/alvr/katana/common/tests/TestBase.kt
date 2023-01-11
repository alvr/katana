package dev.alvr.katana.common.tests

import io.mockk.junit5.MockKExtension
import java.util.TimeZone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@Suppress("UnnecessaryAbstractClass")
@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(value = [MockKExtension::class])
abstract class TestBase {
    private val dispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(dispatcher)

    @BeforeEach
    internal fun setUp() {
        Dispatchers.setMain(dispatcher)
        runBlocking(dispatcher) { beforeEach() }
    }

    open suspend fun beforeEach() {
        // Nothing...
    }

    @AfterEach
    internal fun tearDown() {
        runBlocking(dispatcher) { afterEach() }
        Dispatchers.resetMain()
    }

    open suspend fun afterEach() {
        // Nothing...
    }

    protected fun runTest(test: suspend TestScope.() -> Unit) {
        testScope.runTest(testBody = test)
    }

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setUpAll() {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        }
    }
}
