package dev.alvr.katana.domain.base.failures

@Suppress("UnnecessaryAbstractClass")
abstract class Failure : Throwable() {
    object Unknown : Failure()
}
