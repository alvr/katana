package dev.alvr.katana.data.remote.base.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import org.koin.core.module.Module

internal actual fun ApolloClient.Builder.sentryInterceptor() = this
