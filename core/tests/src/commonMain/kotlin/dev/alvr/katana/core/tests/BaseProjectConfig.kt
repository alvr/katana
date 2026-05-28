package dev.alvr.katana.core.tests

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.config.LogLevel
import io.kotest.core.extensions.Extension
import io.kotest.core.names.DuplicateTestNameMode
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.AssertionMode
import io.kotest.engine.config.IncludeTestScopeAffixes

/**
 * Shared Kotest project configuration for every test module.
 *
 * On multiplatform projects Kotest detects the [io.kotest.provided.ProjectConfig] class via KSP, which is source-based
 * and does not scan across module boundaries. To share this configuration, each test module defines a thin
 * `io.kotest.provided.ProjectConfig : BaseProjectConfig()` subclass in its own test source set.
 *
 * See https://kotest.io/docs/next/framework/project-config.html#sharing-config-across-modules
 */
abstract class BaseProjectConfig : AbstractProjectConfig() {
    override val assertionMode = AssertionMode.Warn
    override val coroutineDebugProbes = true
    override val coroutineTestScope = true
    override val duplicateTestNameMode = DuplicateTestNameMode.Silent
    override val globalAssertSoftly = true
    override val includeTestScopeAffixes = IncludeTestScopeAffixes.ALWAYS
    override val isolationMode = IsolationMode.SingleInstance
    override val logLevel = LogLevel.Warn
    override val removeTestNameWhitespace = true
    override val testNameAppendTags = true

    override val extensions: List<Extension> = listOf(KatanaTestMainDispatcherExtension)
}
