package dev.scottpierce.netflix.logo.state

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.scottpierce.netflix.logo.AnimationMode
import dev.scottpierce.netflix.logo.util.percentWindow

private const val INTRO_ANIMATION_MILLIS = 550
private const val OUTRO_ANIMATION_MILLIS = 1500
private const val INTRO_OUTRO_ANIMATION_MILLIS = INTRO_ANIMATION_MILLIS + OUTRO_ANIMATION_MILLIS

/** The percent of the intro animation of the whole intro, outro animation */
private const val INTRO_ANIMATION_PERCENT: Float = INTRO_ANIMATION_MILLIS / INTRO_OUTRO_ANIMATION_MILLIS.toFloat()
/** The percent of the outro animation of the whole intro, outro animation */
private const val OUTRO_ANIMATION_PERCENT: Float = 1f - INTRO_ANIMATION_PERCENT

private const val INTRO_STROKE_1_PERCENT_COMPLETE = 1 / 3f // 1/3 of the intro animation
private const val INTRO_STROKE_2_PERCENT_COMPLETE = 2 / 3f // 2/3 of the intro animation
private const val INTRO_STROKE_3_PERCENT_COMPLETE = 1f // 3/3 of the intro animation

private val DRAW_STATE_NONE = DrawState(
    stroke1 = Stroke1State.Off,
    stroke2 = Stroke2State.Off,
    stroke3 = Stroke3State.Off,
)

private val DRAW_STATE_INTRO_COMPLETED = DrawState(
    stroke1 = Stroke1State.Intro(1f),
    stroke2 = Stroke2State.Intro(1f),
    stroke3 = Stroke3State.Intro(1f),
)

internal object DrawManager {
    @Composable
    fun calculateDrawState(animation: AnimationMode): DrawState {
        val durationMillis: Int = when (animation) {
            AnimationMode.NONE -> return DRAW_STATE_INTRO_COMPLETED
            AnimationMode.INTRO -> {
                INTRO_ANIMATION_MILLIS
            }

            AnimationMode.INTRO_AND_OUTRO -> {
                INTRO_OUTRO_ANIMATION_MILLIS
            }
        }

        var animateToPercent by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(Unit) {
            animateToPercent = 1f
        }

        val drawPercent by animateFloatAsState(
            targetValue = animateToPercent,
            label = "Netflix Logo",
            animationSpec = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
        )

        return when (animation) {
            AnimationMode.NONE -> {
                // Not necessary to have this branch in Kotlin 2.0
                error("Not reachable")
            }
            AnimationMode.INTRO -> {
                calculateIntroDrawState(drawPercent = drawPercent)
            }
            AnimationMode.INTRO_AND_OUTRO -> {
                if (drawPercent <= INTRO_ANIMATION_PERCENT) {
                    val introDrawPercent = drawPercent / INTRO_ANIMATION_PERCENT
                    calculateIntroDrawState(drawPercent = introDrawPercent)
                } else {
                    val outroDrawPercent = (drawPercent - INTRO_ANIMATION_PERCENT) / OUTRO_ANIMATION_PERCENT
                    println("OUTRO PERCENT $outroDrawPercent")
                    calculateOutroDrawState(drawPercent = outroDrawPercent)
                }
            }
        }
    }
}

private fun calculateIntroDrawState(drawPercent: Float): DrawState {
    if (drawPercent == 0f) {
        return DRAW_STATE_NONE
    }

    val stroke1DrawPercent: Float = (drawPercent / INTRO_STROKE_1_PERCENT_COMPLETE)
        .coerceIn(0f, 1f)
    val stroke2DrawPercent: Float = run {
        val amountToDraw = (drawPercent - INTRO_STROKE_1_PERCENT_COMPLETE)
        val stroke2Size = INTRO_STROKE_2_PERCENT_COMPLETE - INTRO_STROKE_1_PERCENT_COMPLETE
        (amountToDraw / stroke2Size).coerceIn(0f, 1f)
    }
    val stroke3DrawPercent: Float = run {
        val amountToDraw = (drawPercent - INTRO_STROKE_2_PERCENT_COMPLETE)
        val stroke3Size = INTRO_STROKE_3_PERCENT_COMPLETE - INTRO_STROKE_2_PERCENT_COMPLETE
        (amountToDraw / stroke3Size).coerceIn(0f, 1f)
    }

    return DrawState(
        stroke1 = if (stroke1DrawPercent == 0f) Stroke1State.Off else Stroke1State.Intro(stroke1DrawPercent),
        stroke2 = if (stroke2DrawPercent == 0f) Stroke2State.Off else Stroke2State.Intro(stroke2DrawPercent),
        stroke3 = if (stroke3DrawPercent == 0f) Stroke3State.Off else Stroke3State.Intro(stroke3DrawPercent),
    )
}

private fun calculateOutroDrawState(drawPercent: Float): DrawState {
    return DRAW_STATE_INTRO_COMPLETED.copy(
        stroke3 = calculateOutroStroke3State(drawPercent),
    )
}

/** Takes 6 frames on a 24 fps video. */
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
