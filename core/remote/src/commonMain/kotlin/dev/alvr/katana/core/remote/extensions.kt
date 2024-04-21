package dev.alvr.katana.core.remote

import arrow.core.Either
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.DefaultApolloException
import com.apollographql.apollo3.exception.HttpCacheMissException
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import com.apollographql.apollo3.exception.NoDataException
import dev.alvr.katana.core.domain.failures.Failure

fun <A, B> Either<A, B>.optional() = Optional.presentIfNotNull(getOrNull())

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
    is ApolloParseException,
    is JsonDataException,
    is JsonEncodingException,
    is NoDataException -> response
    else -> unknown
}
