package dev.alvr.katana.core.common

import kotlin.jvm.JvmInline
import kotlinx.io.files.SystemFileSystem
import okio.Path
import kotlinx.io.files.Path as KtPath

sealed interface KatanaPath {
    val path: Path

    fun resolve(name: String): KatanaPath = when (this) {
        is KatanaFilesPath -> KatanaFilesPath(path / name)
        is KatanaCachePath -> KatanaCachePath(path / name)
    }

    fun toKtPath(): KtPath = KtPath(path.toString())
}

@JvmInline
value class KatanaFilesPath(override val path: Path) : KatanaPath {
    init {
        toKtPath().parent?.let {
            SystemFileSystem.createDirectories(it)
        }
    }
}

@JvmInline
value class KatanaCachePath(override val path: Path) : KatanaPath {
    init {
        toKtPath().parent?.let {
            SystemFileSystem.createDirectories(it)
        }
    }
}
