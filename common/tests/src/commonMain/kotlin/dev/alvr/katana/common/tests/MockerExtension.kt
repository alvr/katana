package dev.alvr.katana.common.tests

import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.kodein.mock.Mocker

private class MockerExtension(private val mocker: Mocker) : TestCaseExtension {
    override suspend fun intercept(
        testCase: TestCase,
        execute: suspend (TestCase) -> TestResult
    ) = execute(testCase).also { mocker.reset() }
}

operator fun Mocker.invoke(): TestCaseExtension = MockerExtension(this)
