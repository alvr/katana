package dev.alvr.katana.data.remote.base.di

import com.apollographql.apollo3.ApolloClient

internal actual fun ApolloClient.Builder.sentryInterceptor() = this
