package dev.scottpierce.netflix.logo.util

/**
 * Allows you to zoom in on a window of a percent value.
 *
 * i.e. For the value of 0.2, and you retrieved the window with a start of 0.1 and an end of 0.3,
 * the window would return 0.5.
 */
fun Float.percentWindow(
    start: Float,
    end: Float,
    min: Float = 0f,
    max: Float = 1f,
): Float {
    return ((this - start) / (end - start))
        .coerceIn(minimumValue = min, maximumValue = max)
}

/**
 * Allows you to zoom in on a window of a percent value.
 *
 * i.e. For the value of 0.2, and you retrieved the window with a start of 0.1 and an end of 0.3,
 * the window would return 0.5.
 *
 * @param clamp when true, clamps the returned values from 0 to 1, otherwise allows values above 1f and below 0.
 */
fun Float.percentWindow(
    start: Float,
    end: Float,
    clamp: Boolean
): Float {
    return percentWindow(
        start = start,
        end = end,
        min = if (clamp) 0f else -Float.MAX_VALUE,
        max = if (clamp) 1f else Float.MAX_VALUE,
    )
}
