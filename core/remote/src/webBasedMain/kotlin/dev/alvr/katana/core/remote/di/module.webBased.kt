package dev.alvr.katana.core.remote.di

import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.api.NormalizedCacheFactory
import org.koin.dsl.module

internal actual fun apolloDatabaseModule() = module {
    single<NormalizedCacheFactory> { MemoryCacheFactory() }
}
