package dev.scottpierce.netflix.logo.stroke3

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.scottpierce.netflix.logo.NetflixLogo
import dev.scottpierce.netflix.logo.Stroke3State

internal fun DrawScope.drawStroke3(
    strokeWidth: Float,
    state: Stroke3State,
) {
    when (state) {
        Stroke3State.Off -> {}
        is Stroke3State.Intro -> {
            drawStroke3Intro(
                strokeWidth = strokeWidth,
                drawPercent = state.drawPercent,
            )
        }
        is Stroke3State.Outro -> {
            drawStroke3Outro(strokeWidth, state)
        }
    }
}

private fun DrawScope.drawStroke3Intro(
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
