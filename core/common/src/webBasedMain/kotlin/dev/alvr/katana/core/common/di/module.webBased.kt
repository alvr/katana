package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.common.KatanaFilesPath
import kotlinx.io.files.Path
import okio.Path.Companion.toPath
import org.koin.dsl.module

internal actual fun katanaPathModule() = module {
    single<KatanaCachePath> { KatanaCachePath(Path("cache").toString().toPath()) }
    single<KatanaFilesPath> { KatanaFilesPath(Path("files").toString().toPath()) }
}
