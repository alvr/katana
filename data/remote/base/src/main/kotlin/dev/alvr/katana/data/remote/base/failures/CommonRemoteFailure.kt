package dev.alvr.katana.data.remote.base.failures

import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.HttpCacheMissException
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import dev.alvr.katana.domain.base.failures.Failure

sealed interface CommonRemoteFailure : Failure {
    object CacheFailure : CommonRemoteFailure
    object NetworkFailure : CommonRemoteFailure
    object ResponseFailure : CommonRemoteFailure
    object UnknownFailure : CommonRemoteFailure

    companion object {
        operator fun invoke(ex: Throwable) = when (ex) {
            is ApolloHttpException -> NetworkFailure
            is ApolloNetworkException -> NetworkFailure
            is CacheMissException -> CacheFailure
            is HttpCacheMissException -> CacheFailure
            is ApolloParseException -> ResponseFailure
            is JsonDataException -> ResponseFailure
            is JsonEncodingException -> ResponseFailure
            else -> UnknownFailure
        }
    }
}
