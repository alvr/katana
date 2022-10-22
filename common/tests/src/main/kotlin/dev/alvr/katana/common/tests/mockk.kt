package dev.alvr.katana.common.tests

import arrow.core.Either
import arrow.core.right
import io.mockk.MockKMatcherScope
import io.mockk.coEvery
import io.mockk.mockk

inline fun <reified T : Any> valueMockk() = mockk<T>(relaxed = true)

fun coEitherJustRun(block: suspend MockKMatcherScope.() -> Either<*, Unit>) = coEvery(block) returns Unit.right()
