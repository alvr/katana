package dev.alvr.katana.data.preferences.session.di

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.domain.session.models.AnilistToken
import okio.FileSystem
import okio.Path
import okio.fakefilesystem.FakeFileSystem

internal actual val fileSystem = FileSystem.SYSTEM

internal actual fun corruptionHandler(createFile: Path) = ReplaceFileCorruptionHandler {
    FakeFileSystem().delete(createFile, mustExist = false) // Delete to be able to create the file again
    Session(anilistToken = AnilistToken("recreated"))
}
