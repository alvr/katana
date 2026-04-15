package io.kotest.provided

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.config.LogLevel
import io.kotest.core.names.DuplicateTestNameMode
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.AssertionMode
import io.kotest.engine.config.IncludeTestScopeAffixes

object ProjectConfig : AbstractProjectConfig() {
    override val assertionMode = AssertionMode.Warn
    override val coroutineDebugProbes = true
    override val duplicateTestNameMode = DuplicateTestNameMode.Silent
    override val globalAssertSoftly = true
    override val includeTestScopeAffixes = IncludeTestScopeAffixes.ALWAYS
    override val isolationMode = IsolationMode.SingleInstance
    override val logLevel = LogLevel.Warn
    override val removeTestNameWhitespace = true
    override val testNameAppendTags = true
}
