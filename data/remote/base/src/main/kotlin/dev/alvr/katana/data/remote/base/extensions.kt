package dev.alvr.katana.data.remote.base

import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.HttpCacheMissException
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import dev.alvr.katana.domain.base.failures.Failure

fun Throwable.toFailure(
    network: Failure = Failure.Unknown,
    response: Failure = Failure.Unknown,
    cache: Failure = Failure.Unknown,
    unknown: Failure = Failure.Unknown,
): Failure = when (this) {
    is ApolloHttpException,
    is ApolloNetworkException -> network
    is CacheMissException,
    is HttpCacheMissException -> cache
    is ApolloParseException,
    is JsonDataException,
    is JsonEncodingException -> response
    else -> unknown
}
