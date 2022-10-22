package dev.alvr.katana.data.remote.base.failures

import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.HttpCacheMissException
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import dev.alvr.katana.domain.base.failures.Failure

sealed class CommonRemoteFailure : Failure() {
    object Cache : CommonRemoteFailure()
    object Network : CommonRemoteFailure()
    object Response : CommonRemoteFailure()
    object Unknown : CommonRemoteFailure()

    companion object {
        operator fun invoke(ex: Throwable) = when (ex) {
            is ApolloHttpException -> Network
            is ApolloNetworkException -> Network
            is CacheMissException -> Cache
            is HttpCacheMissException -> Cache
            is ApolloParseException -> Response
            is JsonDataException -> Response
            is JsonEncodingException -> Response
            else -> Unknown
        }
    }
}
