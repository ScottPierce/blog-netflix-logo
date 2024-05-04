package dev.scottpierce.netflix.logo

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private const val INTRO_ANIMATION_MILLIS = 550
private const val OUTRO_ANIMATION_MILLIS = 550
private const val INTRO_OUTRO_ANIMATION_MILLIS = INTRO_ANIMATION_MILLIS + OUTRO_ANIMATION_MILLIS

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
                val animationMillis = INTRO_OUTRO_ANIMATION_MILLIS * drawPercent

                if (animationMillis <= INTRO_ANIMATION_MILLIS) {
                    val introAnimationPercent = INTRO_ANIMATION_MILLIS / INTRO_OUTRO_ANIMATION_MILLIS.toFloat()
                    calculateIntroDrawState(drawPercent = drawPercent / introAnimationPercent)
                } else {
                    calculateOutroDrawState(drawPercent = drawPercent)
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
    return DRAW_STATE_NONE
}
