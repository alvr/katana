package dev.alvr.katana.utils.tests.unit

import io.kotest.common.ExperimentalKotest
import io.kotest.core.config.AbstractProjectConfig

object ProjectConfig : AbstractProjectConfig() {
    override val coroutineDebugProbes = true
    override val includeTestScopePrefixes = true
    override val parallelism = 3
    @ExperimentalKotest override var testCoroutineDispatcher = true
}
