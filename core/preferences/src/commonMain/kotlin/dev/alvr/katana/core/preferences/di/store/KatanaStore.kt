package dev.alvr.katana.core.preferences.di.store

import dev.alvr.katana.core.common.KatanaPath
import io.github.xxfast.kstore.Codec
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.serialization.Serializable

@PublishedApi
internal expect inline fun <reified T : @Serializable Any> codec(path: KatanaPath, name: String): Codec<T>

inline fun <reified T : @Serializable Any> katanaStoreOf(
    path: KatanaPath,
    name: String,
    default: T
): KatanaStore<T> = KStore(
    default = default,
    codec = codec(path, name),
).toKatanaStore(default)

interface KatanaStore<T : @Serializable Any> : AutoCloseable {
    val data: Flow<T>
    val default: T

    suspend fun set(value: T)
    suspend fun get(): T
    suspend fun update(operation: (T) -> T): T
    suspend fun delete()
    suspend fun reset()
}

private class KatanaStoreImpl<T : @Serializable Any>(
    private val store: KStore<T>,
    override val default: T,
) : KatanaStore<T> {
    override val data = store.updates.filterNotNull()

    override suspend fun set(value: T) {
        store.set(value)
    }

    override suspend fun get(): T = store.get() ?: default

    override suspend fun update(operation: (T) -> T): T {
        store.update { operation(it ?: default) }
        return get()
    }

    override suspend fun delete() {
        store.delete()
    }

    override suspend fun reset() {
        store.reset()
    }

    override fun close() {
        store.close()
    }
}

@PublishedApi
internal fun <T : Any> KStore<T>.toKatanaStore(
    default: T,
): KatanaStore<T> = KatanaStoreImpl(this, default)
