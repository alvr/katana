package dev.alvr.katana.utils.tests.unit

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode

object ProjectConfig : AbstractProjectConfig() {
    override val coroutineDebugProbes = true
    override val includeTestScopePrefixes = true
    override val isolationMode = IsolationMode.InstancePerTest
    override val parallelism = 3
}
