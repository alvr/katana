package dev.alvr.katana.core.tests

import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestType
import io.kotest.core.test.isRootTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.mock.MockProvider
import org.koin.test.mock.Provider

class KoinExtension(
    private val modules: List<Module>,
    private val mockProvider: Provider<*>? = null,
    private val mode: KoinLifecycleMode = KoinLifecycleMode.Test,
) : TestCaseExtension {

    constructor(
        module: Module,
        mockProvider: Provider<*>? = null,
        mode: KoinLifecycleMode = KoinLifecycleMode.Test,
    ) : this(listOf(module), mockProvider, mode)

    @Suppress("TooGenericExceptionCaught")
    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult {
        return if (testCase.isApplicable()) {
            try {
                stopKoin()
                startKoin {
                    if (mockProvider != null) MockProvider.register(mockProvider)
                    modules(modules)
                }
                execute(testCase)
            } catch (t: Throwable) {
                throw IllegalStateException(t)
            } finally {
                stopKoin()
            }
        } else {
            execute(testCase)
        }
    }

    private fun TestCase.isApplicable() =
        mode == KoinLifecycleMode.Root && isRootTest() ||
            mode == KoinLifecycleMode.Test && type == TestType.Test

    enum class KoinLifecycleMode {
        Root, Test
    }
}
