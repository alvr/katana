package dev.alvr.katana.core.remote.di

import com.apollographql.apollo.ApolloClient

internal actual fun ApolloClient.Builder.sentryInterceptor() = this
