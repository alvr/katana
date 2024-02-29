package dev.alvr.katana.common.tests

import arrow.core.Either
import arrow.core.right
import io.mockk.MockKMatcherScope
import io.mockk.coEvery

fun coEitherJustRun(block: suspend MockKMatcherScope.() -> Either<*, Unit>) = coEvery(block) returns Unit.right()
