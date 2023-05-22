package dev.alvr.katana.common.tests

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <A, B> Either<A, B>.shouldBeRight(
    failureMessage: (A) -> String = { "Expected Either.Right, but found Either.Left with value $it" }
): B {
    contract {
        returns() implies (this@shouldBeRight is Right<B>)
    }
    return when (this) {
        is Right -> value
        is Left -> throw AssertionError(failureMessage(value))
    }
}

infix fun <A, B> Either<A, B>.shouldBeRight(b: B): B =
    shouldBeRight().shouldBe(b)

infix fun <A, B> Either<A, B>.shouldNotBeRight(b: B): B =
    shouldBeRight().shouldNotBe(b)

@OptIn(ExperimentalContracts::class)
fun <A, B> Either<A, B>.shouldBeLeft(
    failureMessage: (B) -> String = { "Expected Either.Left, but found Either.Right with value $it" }
): A {
    contract {
        returns() implies (this@shouldBeLeft is Left<A>)
    }
    return when (this) {
        is Left -> value
        is Right -> throw AssertionError(failureMessage(value))
    }
}

infix fun <A, B> Either<A, B>.shouldBeLeft(a: A): A =
    shouldBeLeft().shouldBe(a)

infix fun <A, B> Either<A, B>.shouldNotBeLeft(a: A): A =
    shouldBeLeft().shouldNotBe(a)

@OptIn(ExperimentalContracts::class)
fun <A> Option<A>.shouldBeSome(failureMessage: () -> String = { "Expected Some, but found None" }): A {
    contract {
        returns() implies (this@shouldBeSome is Some<A>)
    }
    return when (this) {
        None -> throw AssertionError(failureMessage())
        is Some -> value
    }
}

infix fun <A> Option<A>.shouldBeSome(a: A): A =
    shouldBeSome().shouldBe(a)

infix fun <A> Option<A>.shouldNotBeSome(a: A): A =
    shouldBeSome().shouldNotBe(a)

@OptIn(ExperimentalContracts::class)
fun <A> Option<A>.shouldBeNone(
    failureMessage: (Some<A>) -> String = { "Expected None, but found Some with value ${it.value}" }
): None {
    contract {
        returns() implies (this@shouldBeNone is None)
    }
    return when (this) {
        None -> None
        is Some -> throw AssertionError(failureMessage(this))
    }
}
