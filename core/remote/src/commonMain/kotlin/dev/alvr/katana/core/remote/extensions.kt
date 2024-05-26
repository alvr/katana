package dev.alvr.katana.core.remote

import arrow.core.Either
import arrow.core.Option
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.DefaultApolloException
import com.apollographql.apollo3.exception.HttpCacheMissException
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import com.apollographql.apollo3.exception.NoDataException
import dev.alvr.katana.core.domain.failures.Failure

fun Throwable.toFailure(
    network: Failure = Failure.Unknown,
    response: Failure = Failure.Unknown,
    cache: Failure = Failure.Unknown,
    unknown: Failure = Failure.Unknown,
): Failure = when (this) {
    is ApolloHttpException,
    is ApolloNetworkException,
    is DefaultApolloException -> network

    is CacheMissException,
    is HttpCacheMissException -> cache

    is JsonDataException,
    is JsonEncodingException,
    is NoDataException -> response

    else -> unknown
}

suspend fun <D : Operation.Data> ApolloCall<D>.executeOrThrow() = execute().also { response ->
    response.exception?.let { throw it }
}

val <V> V?.optional get(): Optional<V?> = Optional.presentIfNotNull(this)

val <V> V.present get(): Optional<V?> = Optional.present(this)

val <V> Either<*, V>.optional get(): Optional<V?> = Optional.presentIfNotNull(getOrNull())

val <V> Option<V>.optional get(): Optional<V?> = Optional.presentIfNotNull(getOrNull())
