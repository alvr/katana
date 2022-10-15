package dev.alvr.katana.domain.base.failures

abstract class Failure : Throwable() {
    object Unknown : Failure()
}
