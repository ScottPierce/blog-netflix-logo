package dev.scottpierce.netflix.logo

import android.graphics.BlurMaskFilter
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import dev.scottpierce.netflix.logo.state.DrawState
import dev.scottpierce.netflix.logo.state.Stroke1State
import dev.scottpierce.netflix.logo.state.Stroke2State
import dev.scottpierce.netflix.logo.ui.stroke3.drawStroke3

private const val NETFLIX_LOGO_STROKE_WIDTH_PERCENT = 494 / 1377f // Measured from an image

internal fun DrawScope.drawNetflixN(drawState: DrawState) {
    val strokeWidth = size.width * NETFLIX_LOGO_STROKE_WIDTH_PERCENT

    // The bottom of the netflix logo has a clipped arc at the bottom
    clipPath(path = bottomArcClipPath(), clipOp = ClipOp.Difference) {
        when (val s = drawState.stroke1) {
            Stroke1State.Off -> {}
            is Stroke1State.Intro -> drawNetflixNStroke1(strokeWidth = strokeWidth, drawPercent = s.drawPercent)
            is Stroke1State.Outro -> TODO()
        }

        drawStroke3(strokeWidth, drawState.stroke3)

        // Stroke 2 goes on top of the other strokes, so it's drawn last
        when (val s = drawState.stroke2) {
            Stroke2State.Off -> {}
            is Stroke2State.Intro -> drawNetflixNStroke2(strokeWidth = strokeWidth, drawPercent = s.drawPercent)
            is Stroke2State.Outro -> TODO()
        }
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

// Storing drawing objects as singletons, like this, is safe as long as we're in drawing
// environments limited to a single thread. This may create a race condition while drawing
// Compose to images in testing environments where multiple threads can potentially be used.
private val stroke2Path = Path()
private val shadowPaint = Paint().apply {
    color = NetflixLogo.COLOR_SHADOW
    blendMode = BlendMode.SrcAtop
    isAntiAlias = true
    style = PaintingStyle.Stroke
}

private fun DrawScope.drawNetflixNStroke2(
    strokeWidth: Float,
    drawPercent: Float,
) {
    val drawHeight = size.height * drawPercent

    // Stroke 2 shadow
    // - The blend mode only works if the calling DrawScope is being drawn in a separate
    //   layer, via the modifier call:
    //   graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    drawIntoCanvas { canvas ->
        // It's important that all of our moving values scale based on the size of the drawing.
        // All of their values should be based on the strokeWidth.
        val shadowXInset = strokeWidth * 0.15f
        val shadowXOutset = strokeWidth * 0.15f
        val shadowStrokeWidth = strokeWidth * 0.2f
        val shadowBlurRadius = strokeWidth * 0.15f

        shadowPaint.strokeWidth = shadowStrokeWidth
        // BlurMaskFilter is read-only, so we need to set it every frame in case the blur radius has changed
        shadowPaint.asFrameworkPaint().maskFilter =
            BlurMaskFilter(shadowBlurRadius, BlurMaskFilter.Blur.NORMAL)

        // Stroke 1 Shadow
        run {
            val x = 0f + ((size.width - strokeWidth - shadowXOutset) * drawPercent)
            canvas.drawLine(
                p1 = Offset(x = 0f + shadowXInset, y = 0f), // Top left
                p2 = Offset(x = x, y = drawHeight), // Bottom left
                paint = shadowPaint,
            )
        }

        // Stroke 3 Shadow
        run {
            val topRight = Offset(x = strokeWidth + shadowXOutset, y = 0f)
            val bottomRightX = topRight.x + ((size.width - topRight.x) * drawPercent) - shadowXInset
            canvas.drawLine(
                p1 = topRight,
                p2 = Offset(x = bottomRightX, y = drawHeight),
                paint = shadowPaint
            )
        }
    }

    drawPath(
        path = stroke2Path.apply {
            val drawWidth = (size.width - strokeWidth) * drawPercent

            moveTo(x = 0f, y = 0f) // Top left
            lineTo(x = strokeWidth, y = 0f) // Top right
            lineTo(x = drawWidth + strokeWidth, y = drawHeight) // Bottom right
            lineTo(x = drawWidth, y = drawHeight) // Bottom left
            close() // Close the path to form a rhombus
        },
        color = NetflixLogo.COLOR_RED,
    )
    stroke2Path.reset()
}
