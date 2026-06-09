package dev.alvr.katana.core.common

import okio.Path

interface KatanaStorage {
    val files: Path
    val cache: Path
}

internal data class KatanaStorageImpl(override val files: Path, override val cache: Path) : KatanaStorage
