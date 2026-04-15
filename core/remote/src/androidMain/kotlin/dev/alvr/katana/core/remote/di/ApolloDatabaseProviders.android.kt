package dev.alvr.katana.core.remote.di

import android.content.Context
import com.apollographql.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.cache.normalized.sql.SqlNormalizedCacheFactory
import dev.alvr.katana.core.common.di.AppContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
actual interface ApolloDatabaseProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun normalizedCacheFactory(@AppContext context: Context): NormalizedCacheFactory =
        SqlNormalizedCacheFactory(context = context, name = CACHE_DATABASE)
}
