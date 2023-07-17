package dev.alvr.katana.data.remote.lists

import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.mockserver.MockResponse
import com.apollographql.apollo3.mockserver.MockServer

@OptIn(ApolloExperimental::class)
internal fun MockServer.enqueueResponse(builder: MockResponse.Builder.() -> Unit) {
    repeat(4) {
        enqueue(MockResponse.Builder().apply(builder).build())
    }
}
