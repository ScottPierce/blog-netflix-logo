package dev.scottpierce.netflix.logo.ui

import androidx.compose.ui.graphics.Color

internal sealed interface DrawItem {
    val w: Float
}

/**
 * A line to be drawn in the logo
 */
internal data class Line(
    /** The relative width of the line */
    override val w: Float,
    /** Height of the line as a percent */
    val h: Float = 1f,
    val color: Color,
    val fadeStart: Float = 1f,
    val fadeEnd: Float = 1f,
) : DrawItem

/**
 * A space between other draw items
 */
internal data class Space(
    /** Width of the space as a percent */
    override val w: Float,
) : DrawItem

/**
 * Sum all the [DrawItem] widths.
 *
 * There is no sumOf method for [Float], so we'll provide this since we'll do this in a few places
 */
internal fun List<DrawItem>.sumOfW(): Float {
    var sum = 0f
    for (item in this) {
        sum += item.w
    }
    return sum
}
