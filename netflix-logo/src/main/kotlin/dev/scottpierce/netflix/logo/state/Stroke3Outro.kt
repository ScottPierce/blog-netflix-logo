package dev.scottpierce.netflix.logo.state

import dev.scottpierce.netflix.logo.util.percentWindow

internal fun calculateOutroDrawState(drawPercent: Float): DrawState {
    return DRAW_STATE_INTRO_COMPLETED.copy(
        stroke3 = calculateOutroStroke3State(drawPercent),
    )
}

/** Takes 6 frames at 24 fps. */
private const val DECAY_REVEAL_DURATION_STROKE_3_MILLIS = ((6 / 24f) * 1000).toInt()
/** The size of the decay gradient, as a percent of the height. */
private const val DECAY_REVEAL_SIZE_STROKE_3 = 0.4f
private const val DECAY_REVEAL_TOP_STROKE_3 = 1f + DECAY_REVEAL_SIZE_STROKE_3
private const val DECAY_REVEAL_WINDOW_STROKE_3_START = 0.1f
private const val DECAY_REVEAL_WINDOW_STROKE_3_END: Float = DECAY_REVEAL_WINDOW_STROKE_3_START +
        (DECAY_REVEAL_DURATION_STROKE_3_MILLIS / OUTRO_ANIMATION_MILLIS.toFloat())

private fun calculateOutroStroke3State(drawPercent: Float): Stroke3State.Outro {
    val stroke3DrawPercent = drawPercent.percentWindow(
        start = DECAY_REVEAL_WINDOW_STROKE_3_START,
        end = DECAY_REVEAL_WINDOW_STROKE_3_END,
    )
    val decayRevealTop = DECAY_REVEAL_TOP_STROKE_3 - (stroke3DrawPercent * DECAY_REVEAL_TOP_STROKE_3)

    return Stroke3State.Outro(
        drawPercent = stroke3DrawPercent,
        decayRevealTop = decayRevealTop,
        decayRevealBottom = decayRevealTop - DECAY_REVEAL_SIZE_STROKE_3,
    )
}