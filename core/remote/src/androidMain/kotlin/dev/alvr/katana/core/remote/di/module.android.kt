package dev.alvr.katana.core.remote.di

import com.apollographql.apollo3.ApolloClient
import io.sentry.apollo3.sentryTracing

internal actual fun ApolloClient.Builder.sentryInterceptor() = sentryTracing(captureFailedRequests = true)
