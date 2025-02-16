package dev.alvr.katana.core.remote.di

import com.apollographql.apollo.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import dev.alvr.katana.core.common.KatanaFilesPath
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun apolloDatabaseModule(): Module = module {
    single<NormalizedCacheFactory> {
        val db = get<KatanaFilesPath>().resolve(CACHE_DATABASE).path
        SqlNormalizedCacheFactory("jdbc:sqlite:$db")
    }
}
