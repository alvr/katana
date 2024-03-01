package dev.alvr.katana.core.common

import arrow.core.Either

inline fun Either.Companion.catchUnit(f: () -> Unit): Either<Throwable, Unit> = catch(f)
