package dev.scottpierce.netflix.logo.ui

import android.graphics.BlurMaskFilter
import androidx.compose.ui.graphics.Color

internal val WHITE_1 = Color(red = 137, green = 44, blue = 44)

/**
 * When the lines are drawn with a [BlurMaskFilter], the blur makes the colors look much dimmer.
 * I'm using this to brighten the colors to offset the dimming effect.
 */
private const val BRIGHTEN_COLORS_BY = 0.3f

internal val RED_1 = Color(red = 68, green = 8, blue = 21).brighten(BRIGHTEN_COLORS_BY)
internal val RED_2 = Color(red = 84, green = 13, blue = 11).brighten(BRIGHTEN_COLORS_BY)
internal val RED_3 = Color(red = 100, green = 16, blue = 13).brighten(BRIGHTEN_COLORS_BY)
internal val RED_4 = Color(red = 120, green = 19, blue = 16).brighten(BRIGHTEN_COLORS_BY)
internal val RED_5 = Color(red = 139, green = 10, blue = 3).brighten(BRIGHTEN_COLORS_BY)
internal val RED_6 = Color(red = 196, green = 41, blue = 37).brighten(BRIGHTEN_COLORS_BY)
internal val RED_7 = Color(red = 227, green = 92, blue = 53).brighten(BRIGHTEN_COLORS_BY)

internal fun Color.toArgbInt(): Int {
    val alpha = (this.alpha * 255).toInt() shl 24
    val red = (this.red * 255).toInt() shl 16
    val green = (this.green * 255).toInt() shl 8
    val blue = (this.blue * 255).toInt()
    return alpha or red or green or blue
}

private fun Color.brighten(percent: Float): Color {
    if (percent == 0f) return this
    val factor = 1 + percent

    return Color(
        red = (red * factor).coerceAtMost(1f),
        green = green,
        blue = blue,
        alpha = alpha,
    )
}
