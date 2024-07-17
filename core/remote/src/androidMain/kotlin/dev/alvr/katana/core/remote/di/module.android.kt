package dev.alvr.katana.core.remote.di

import com.apollographql.apollo.ApolloClient

// TODO: Wait for Sentry Apollo 4
// internal actual fun ApolloClient.Builder.sentryInterceptor() = sentryTracing(captureFailedRequests = true)
internal actual fun ApolloClient.Builder.sentryInterceptor() = this
