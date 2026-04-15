package dev.alvr.katana.core.remote.di

import com.apollographql.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.cache.normalized.sql.SqlNormalizedCacheFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
actual interface ApolloDatabaseProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun normalizedCacheFactory(): NormalizedCacheFactory = SqlNormalizedCacheFactory(CACHE_DATABASE)
}
