package dev.alvr.katana.common.tests

import io.mockk.mockk

inline fun <reified T : Any> valueMockk() = mockk<T>(relaxed = true)
