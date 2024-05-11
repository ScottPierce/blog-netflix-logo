package dev.scottpierce.netflix.logo.ui

import androidx.compose.ui.graphics.Color

internal val WHITE_1 = Color(red = 137, green = 44, blue = 44)

internal val RED_1 = Color(red = 68, green = 8, blue = 13)
internal val RED_2 = Color(red = 84, green = 13, blue = 11)
internal val RED_3 = Color(red = 100, green = 16, blue = 13)
internal val RED_4 = Color(red = 120, green = 19, blue = 16)
internal val RED_5 = Color(red = 142, green = 28, blue = 22)
internal val RED_6 = Color(red = 191, green = 49, blue = 35)
internal val RED_7 = Color(red = 227, green = 92, blue = 53)

internal fun Color.toArgbInt(): Int {
    val alpha = (this.alpha * 255).toInt() shl 24
    val red = (this.red * 255).toInt() shl 16
    val green = (this.green * 255).toInt() shl 8
    val blue = (this.blue * 255).toInt()
    return alpha or red or green or blue
}
