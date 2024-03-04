package dev.alvr.katana.core.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.saveable.Saver
import kotlin.jvm.JvmInline
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.readResourceBytes

@Immutable
@JvmInline
value class KatanaResource(private val key: String) {

    @OptIn(ExperimentalResourceApi::class)
    val asPainter @Composable get() = painterResource(DrawableResource(key))

    @OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)
    val asByteArray get() = runBlocking { readResourceBytes(key) }

    val id get() = key.substringAfterLast('/').substringBeforeLast('.')

    companion object {
        val saver = Saver<KatanaResource, String>(
            save = { res -> res.key },
            restore = { key -> KatanaResource(key) }
        )
    }
}
