package dev.alvr.katana.core.tests

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.config.LogLevel
import io.kotest.core.names.DuplicateTestNameMode
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.AssertionMode

object KotestProjectConfig : AbstractProjectConfig() {
    private const val NUM_THREADS = 1

    override val assertionMode = AssertionMode.Warn
    override val coroutineDebugProbes = true
    override val duplicateTestNameMode = DuplicateTestNameMode.Silent
    override val globalAssertSoftly = true
    override val includeTestScopePrefixes = true
    override val isolationMode = IsolationMode.SingleInstance
    override val logLevel = LogLevel.Warn
    override val parallelism = NUM_THREADS
    override val testNameAppendTags = true
    override val testNameRemoveWhitespace = true
}
