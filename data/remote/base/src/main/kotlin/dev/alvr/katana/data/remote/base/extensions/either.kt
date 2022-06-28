package dev.alvr.katana.data.remote.base.extensions

import arrow.core.Either
import arrow.core.Option
import com.apollographql.apollo3.api.Optional

fun <A, B> Either<A, B>.optional() = Optional.presentIfNotNull(orNull())
fun <A> Option<A>.optional() = Optional.presentIfNotNull(orNull())
