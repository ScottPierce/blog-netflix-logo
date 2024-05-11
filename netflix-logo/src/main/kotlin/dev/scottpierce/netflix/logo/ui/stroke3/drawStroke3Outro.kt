package dev.scottpierce.netflix.logo.ui.stroke3

import android.graphics.BlurMaskFilter
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.scottpierce.netflix.logo.NetflixLogo
import dev.scottpierce.netflix.logo.ui.RED_1
import dev.scottpierce.netflix.logo.ui.RED_2
import dev.scottpierce.netflix.logo.ui.RED_3
import dev.scottpierce.netflix.logo.ui.RED_4
import dev.scottpierce.netflix.logo.ui.RED_5
import dev.scottpierce.netflix.logo.ui.RED_6
import dev.scottpierce.netflix.logo.state.Stroke3State
import dev.scottpierce.netflix.logo.ui.WHITE_1
import dev.scottpierce.netflix.logo.ui.DrawItem
import dev.scottpierce.netflix.logo.ui.Line
import dev.scottpierce.netflix.logo.ui.Space
import dev.scottpierce.netflix.logo.ui.sumOfW
import dev.scottpierce.netflix.logo.ui.toArgbInt

/** This is a number that can be moved to scale the width of all the DrawItems. */
private val DRAW_ITEMS: List<DrawItem> = listOf(
    Line(w = 5f, color = RED_5),
    Space(w = 1f),
    Line(w = 5f, color = RED_5),
    Space(w = 1f),
    Line(w = 5f, color = RED_5),
    Space(w = 1f),
    Line(w = 5f, color = RED_6),
    Space(w = 7f),
    Line(w = 10f, color = RED_1),
    Space(w = 3f),
    Line(w = 1f, color = RED_2),
    Line(w = 4f, color = RED_3),
    Space(w = 4f),
    Line(w = 4f, color = RED_5),
    Line(w = 6f, color = RED_4),
    Line(w = 4f, color = RED_3),
    Line(w = 5f, color = RED_5),
    Line(w = 2f, color = RED_5),
    Line(w = 2f, color = RED_4),
    Line(w = 2f, color = RED_5),
    Space(w = 2f),
    Line(w = 3f, color = RED_4),
    Space(w = 2f),
    Line(w = 3f, color = RED_3),
    Space(w = 4f),
    Line(w = 2f, color = RED_4),
    Space(w = 1f),
    Line(w = 5f, color = RED_4, fadeStart = 0.70f),
    Line(w = 6f, color = RED_5, fadeStart = 0.62f),
    Line(w = 5f, color = RED_3, fadeStart = 0.57f),
    Line(w = 10f, color = RED_4, fadeStart = 0.50f),
    Space(w = 1f),
    Line(w = 4f, color = RED_6),
    Space(w = 2f),
    Line(w = 5f, color = RED_4),
    Line(w = 2f, color = WHITE_1),
    Line(w = 3f, color = RED_3),
    Line(w = 2f, color = RED_4),
)
private val TOTAL_WIDTH: Float = DRAW_ITEMS.sumOfW()

private val paint: Paint = Paint().apply {
    // Because we're drawing straight lines up and down, we don't need to pay the cost of
    // anti-aliasing.
    isAntiAlias = false
    style = PaintingStyle.Fill
}
private val nativePaint: NativePaint = paint.asFrameworkPaint()
    .also {
        it.maskFilter = BlurMaskFilter(3f, BlurMaskFilter.Blur.NORMAL)
    }

/**
 * For the decay animation, it comes in 2 parts. The first half comes with a thinning of the stroke
 * and then the second half animates away the remainder of the stroke, but it animates away unevenly.
 * The animation is so quick, it doesn't have to be perfect, but with some effort we should be able
 * to closely match it.
 */
internal fun DrawScope.drawStroke3Outro(
    strokeWidth: Float,
    state: Stroke3State.Outro,
) {
    val drawHeight: Float = size.height

    val topLeft = Offset(
        x = size.width - strokeWidth,
        y = size.height - drawHeight,
    )

    var lastDrawnX = topLeft.x
    val canvas = drawContext.canvas

    for (item in DRAW_ITEMS) {
        val drawWidth = (item.w / TOTAL_WIDTH) * strokeWidth

        when (item) {
            is Line -> {
                val lineDrawHeight = item.h * drawHeight

                val top = topLeft.y + (drawHeight - lineDrawHeight)

                if (item.fadeStart == item.fadeEnd) {
                    nativePaint.shader = null
                    paint.color = item.color
                    paint.strokeWidth = drawWidth

                    canvas.drawRect(
                        left = lastDrawnX,
                        top = top,
                        right = lastDrawnX + drawWidth,
                        bottom = size.height,
                        paint,
                    )
                } else {
                    val itemColorArgb = item.color.toArgbInt()
                    nativePaint.shader = android.graphics.LinearGradient(
                        lastDrawnX + drawWidth,
                        size.height,
                        lastDrawnX,
                        top,
                        intArrayOf(itemColorArgb, itemColorArgb, Color.Black.toArgbInt()),
                        floatArrayOf(0f, item.fadeStart, item.fadeEnd),
                        android.graphics.Shader.TileMode.CLAMP,
                    )

                    canvas.drawRect(
                        left = lastDrawnX,
                        top = top,
                        right = lastDrawnX + drawWidth,
                        bottom = size.height,
                        paint,
                    )
                }
            }
            is Space -> {}
        }

        lastDrawnX += drawWidth
    }

    topLayer(strokeWidth, state)
}

private fun DrawScope.topLayer(strokeWidth: Float, state: Stroke3State.Outro) {
    val decayRevealTop = 1f - state.decayRevealTop
    val decayRevealBottom = 1f - state.decayRevealBottom
    drawRect(
        brush = Brush.verticalGradient(
            0f.coerceAtMost(decayRevealTop) to Color.Transparent,
            decayRevealTop to Color.Transparent,
            decayRevealBottom to NetflixLogo.COLOR_RED_DARK,
            1f.coerceAtLeast(decayRevealBottom) to NetflixLogo.COLOR_RED_DARK,
        ),
        topLeft = Offset(x = size.width - strokeWidth, y = 0f),
        size = Size(width = strokeWidth, height = size.height),
    )
}
