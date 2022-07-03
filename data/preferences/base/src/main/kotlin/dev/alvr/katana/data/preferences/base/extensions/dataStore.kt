package dev.alvr.katana.data.preferences.base.extensions

import dev.alvr.katana.domain.base.failures.Failure
import java.io.IOException

fun Throwable.handleDataStore(
    rwException: () -> Failure,
    other: () -> Failure,
) = if (this is IOException) {
    rwException()
} else {
    other()
}
