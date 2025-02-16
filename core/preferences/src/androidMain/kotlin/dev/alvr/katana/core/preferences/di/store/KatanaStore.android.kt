package dev.alvr.katana.core.preferences.di.store

import dev.alvr.katana.core.common.KatanaPath
import io.github.xxfast.kstore.Codec
import io.github.xxfast.kstore.file.FileCodec
import kotlinx.serialization.Serializable

@PublishedApi
internal actual inline fun <reified T : @Serializable Any> codec(path: KatanaPath, name: String): Codec<T> = FileCodec(
    file = path.resolve("$name.json").toKtPath(),
)
