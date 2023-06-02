package dev.alvr.katana.data.remote.lists

import com.apollographql.apollo3.mockserver.MockResponse
import com.apollographql.apollo3.mockserver.MockServer

internal fun MockServer.enqueueResponse(builder: MockResponse.Builder.() -> Unit) {
    repeat(4) {
        enqueue(MockResponse.Builder().apply(builder).build())
    }
}
