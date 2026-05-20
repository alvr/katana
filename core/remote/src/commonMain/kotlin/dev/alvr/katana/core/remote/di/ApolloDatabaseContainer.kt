package dev.alvr.katana.core.remote.di

import com.apollographql.cache.normalized.api.NormalizedCacheFactory
import dev.alvr.katana.core.common.di.AppContext
import dev.alvr.katana.core.common.di.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

expect object ApolloDatabaseContainer {

    @SingleIn(AppScope::class) fun normalizedCacheFactory(@AppContext context: PlatformContext): NormalizedCacheFactory
}

internal const val CACHE_DATABASE = "katana_data.db"
