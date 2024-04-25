package dev.scottpierce.netflix.logo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath

private const val NETFLIX_LOGO_STROKE_WIDTH_PERCENT = 494 / 1377f // Measured from an image
private const val STROKE_1_PERCENT_COMPLETE = 1 / 3f
private const val STROKE_2_PERCENT_COMPLETE = 2 / 3f
private const val STROKE_3_PERCENT_COMPLETE = 1f

fun DrawScope.drawNetflixN(drawPercent: Float, drawShadow: Boolean) {
    val strokeWidth = size.width * NETFLIX_LOGO_STROKE_WIDTH_PERCENT

    // Calculate animation values
    val stroke1DrawPercent: Float = if (drawPercent >= STROKE_1_PERCENT_COMPLETE) {
        1f
    } else {
        drawPercent / STROKE_1_PERCENT_COMPLETE
    }
    val stroke2DrawPercent: Float  = if (drawPercent >= STROKE_2_PERCENT_COMPLETE) {
        1f
    } else {
        val amountToDraw = (drawPercent - STROKE_1_PERCENT_COMPLETE).coerceAtLeast(0f)
        val stroke2Size = STROKE_2_PERCENT_COMPLETE - STROKE_1_PERCENT_COMPLETE
        amountToDraw / stroke2Size
    }
    val stroke3DrawPercent: Float  = if (drawPercent >= STROKE_3_PERCENT_COMPLETE) {
        1f
    } else {
        val amountToDraw = (drawPercent - STROKE_2_PERCENT_COMPLETE).coerceAtLeast(0f)
        val stroke3Size = STROKE_3_PERCENT_COMPLETE - STROKE_2_PERCENT_COMPLETE
        amountToDraw / stroke3Size
    }

    // The bottom of the netflix logo has a clipped arc at the bottom
    clipPath(path = bottomArcClipPath(), clipOp = ClipOp.Difference) {
        drawNetflixNStroke1(strokeWidth = strokeWidth, drawPercent = stroke1DrawPercent)
        drawNetflixNStroke3(strokeWidth = strokeWidth, drawPercent = stroke3DrawPercent)

        // Stroke 2 goes on top of the other strokes, so it's drawn last
        drawNetflixNStroke2(
            strokeWidth = strokeWidth,
            drawPercent = stroke2DrawPercent,
            drawShadow = drawShadow
        )
    }
}

private fun DrawScope.bottomArcClipPath(): Path {
    return Path().apply {
        val radius = size.height * 2 // Based on the height so it automatically scales properly
        val radiusOffset = size.height * 0.0184f // Calculated an offset percent based on an image

        addOval(
            Rect(
                center = Offset(
                    x = size.width / 2f,
                    y = size.height + radius - radiusOffset,
                ),
                radius = radius,
            ),
        )
    }
}

private fun DrawScope.drawNetflixNStroke1(
    strokeWidth: Float,
    drawPercent: Float,
) {
    val drawHeight = drawPercent * size.height
    drawRect(
        color = NetflixLogo.COLOR_RED_DARK,
        topLeft = Offset(
            x = 0f,
            y = size.height - drawHeight,
        ),
        size = Size(width = strokeWidth, height = drawHeight),
    )
}

private val path = Path()

private fun DrawScope.drawNetflixNStroke2(
    strokeWidth: Float,
    drawPercent: Float,
    drawShadow: Boolean,
) {
    val drawHeight = size.height * drawPercent

    if (drawShadow) {
        // Stroke 2 shadow
        // - The blend mode only works if the calling DrawScope is being drawn in a separate
        //   layer, via the modifier call:
        //   graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        drawPath(
            path = Path().apply {
                val shadowStrokeWidth = size.width / 2.1f
                val drawWidth = (size.width - shadowStrokeWidth) * drawPercent

                moveTo(x = 0f, y = 0f) // Top left
                lineTo(x = shadowStrokeWidth, y = 0f) // Top right
                lineTo(x = drawWidth + shadowStrokeWidth, y = drawHeight) // Bottom right
                lineTo(x = drawWidth, y = drawHeight) // Bottom left
                close() // Close the path to form a rhombus
            },
            color = NetflixLogo.COLOR_SHADOW,
            // Use a blend mode to only draw the shadow on the rest of the N. Otherwise it
            // looks odd with the shadow on the background.
            blendMode = BlendMode.DstOut,
        )
    }

    drawPath(
        path = path.apply {
            val drawWidth = (size.width - strokeWidth) * drawPercent

            moveTo(x = 0f, y = 0f) // Top left
            lineTo(x = strokeWidth, y = 0f) // Top right
            lineTo(x = drawWidth + strokeWidth, y = drawHeight) // Bottom right
            lineTo(x = drawWidth, y = drawHeight) // Bottom left
            close() // Close the path to form a rhombus
        },
        color = NetflixLogo.COLOR_RED,
    )
    path.reset()
}

private fun DrawScope.drawNetflixNStroke3(
    strokeWidth: Float,
    drawPercent: Float,
) {
    val drawHeight = drawPercent * size.height
    drawRect(
        color = NetflixLogo.COLOR_RED_DARK,
        topLeft = Offset(
            x = size.width - strokeWidth,
            y = size.height - drawHeight,
        ),
        size = Size(width = strokeWidth, height = drawHeight),
    )
}
