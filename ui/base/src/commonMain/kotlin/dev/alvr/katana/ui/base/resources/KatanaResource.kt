package dev.alvr.katana.ui.base.resources

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.saveable.Saver
import kotlin.jvm.JvmInline

@Immutable
@JvmInline
value class KatanaResource(internal val key: String) {
    companion object {
        val saver = Saver<KatanaResource, String>(
            save = { res -> res.key },
            restore = { key -> KatanaResource(key) }
        )
    }
}
