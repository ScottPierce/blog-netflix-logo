package dev.scottpierce.netflix.logo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import dev.scottpierce.netflix.logo.state.DrawManager

object NetflixLogo {
    const val ASPECT_RATIO = 1377 / 2500f // Measurements taken off of an image

    val COLOR_RED = Color(0xFFE50914)
    val COLOR_RED_DARK = Color(0xFFB20710)
    val COLOR_SHADOW = Color(0x66000000)
}

/**
 * Draws the Netflix Logo, enforcing an aspect ratio of [NetflixLogo.ASPECT_RATIO].
 *
 * Control the size of this Composable by setting a `width` or `height`.
 */
@Composable
fun NetflixLogo(
    modifier: Modifier = Modifier,
    animation: AnimationMode = AnimationMode.NONE,
) {
    val drawState = DrawManager.calculateDrawState(animation)

    Canvas(
        modifier = modifier
            .aspectRatio(NetflixLogo.ASPECT_RATIO)
            // Needs to be set so that the canvas is rendered in a separate layer,
            // otherwise the clipped shadow is visible on the background as BlendMode won't work.
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            },
    ) {
        drawNetflixN(drawState)
    }
}

enum class AnimationMode {
    NONE,
    INTRO,
    INTRO_AND_OUTRO,
}
