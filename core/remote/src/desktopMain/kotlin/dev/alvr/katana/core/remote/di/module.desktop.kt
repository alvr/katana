package dev.alvr.katana.core.remote.di

import com.apollographql.apollo.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import dev.alvr.katana.core.common.configDirectory
import org.koin.core.module.Module
import org.koin.dsl.module

private val cacheDatabasePath = configDirectory() / CACHE_DATABASE

internal actual fun apolloDatabaseModule(): Module = module {
    single<NormalizedCacheFactory> { SqlNormalizedCacheFactory("jdbc:sqlite:$cacheDatabasePath") }
}
