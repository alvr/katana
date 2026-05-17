package dev.alvr.katana.core.remote.di

import com.apollographql.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.cache.normalized.sql.SqlNormalizedCacheFactory
import dev.alvr.katana.core.common.di.AppContext
import dev.alvr.katana.core.common.di.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
actual object ApolloDatabaseProviders {

    @Provides
    @SingleIn(AppScope::class)
    actual fun normalizedCacheFactory(@AppContext context: PlatformContext): NormalizedCacheFactory =
        SqlNormalizedCacheFactory(context = context, name = CACHE_DATABASE)
}
