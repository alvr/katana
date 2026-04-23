package dev.alvr.katana.shared.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.common.di.AppContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface CoilProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun imageLoader(@AppContext context: PlatformContext, cachePath: KatanaCachePath): ImageLoader =
        ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCache { MemoryCache.Builder().maxSizePercent(context, CoilMemoryCachePercent).build() }
            .diskCache {
                DiskCache.Builder()
                    .directory(cachePath.path / CoilImagesPath)
                    .maxSizePercent(CoilDiskCachePercent)
                    .build()
            }
            .build()
}

private const val CoilDiskCachePercent = 0.25
private const val CoilMemoryCachePercent = 0.05
private const val CoilImagesPath = "images_cache"
