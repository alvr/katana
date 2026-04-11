package dev.alvr.katana.core.remote.di

import com.apollographql.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.cache.normalized.sql.SqlNormalizedCacheFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun apolloDatabaseModule(): Module = module {
    single<NormalizedCacheFactory> { SqlNormalizedCacheFactory(context = androidApplication(), name = CACHE_DATABASE) }
}
