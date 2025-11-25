package dev.alvr.katana.core.ui.symbols

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object KatanaSymbols

@Stable
internal class KatanaSymbol(
    private val autoMirror: Boolean = false,
    private val builder: PathBuilder.() -> Unit,
) : ReadOnlyProperty<KatanaSymbols, ImageVector> {
    private var symbol: ImageVector? = null

    override fun getValue(
        thisRef: KatanaSymbols,
        property: KProperty<*>,
    ): ImageVector = symbol ?: materialSymbol(name = property.name, autoMirror = autoMirror) {
        materialPath(pathBuilder = builder)
    }.also { imageVector -> symbol = imageVector }
}

private inline fun materialSymbol(
    name: String,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> Unit,
): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = MaterialSymbolSize,
    defaultHeight = MaterialSymbolSize,
    viewportWidth = MaterialSymbolViewport,
    viewportHeight = MaterialSymbolViewport,
    autoMirror = autoMirror,
).apply(block).build()

private inline fun ImageVector.Builder.materialPath(
    fillAlpha: Float = 1f,
    strokeAlpha: Float = 1f,
    pathFillType: PathFillType = DefaultFillType,
    pathBuilder: PathBuilder.() -> Unit,
): ImageVector.Builder = path(
    fill = SolidColor(Color.Black),
    fillAlpha = fillAlpha,
    stroke = null,
    strokeAlpha = strokeAlpha,
    strokeLineWidth = 1f,
    strokeLineCap = StrokeCap.Butt,
    strokeLineJoin = StrokeJoin.Bevel,
    strokeLineMiter = 1f,
    pathFillType = pathFillType,
    pathBuilder = pathBuilder,
)

private val MaterialSymbolSize = 24.dp
private const val MaterialSymbolViewport = 960f
