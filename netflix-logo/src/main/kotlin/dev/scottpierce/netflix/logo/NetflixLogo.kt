package dev.scottpierce.netflix.logo

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer

object NetflixLogo {
    const val ASPECT_RATIO = 1377 / 2500f // Measurements taken off of an image

    val COLOR_RED = Color(0xFFE50914)
    internal val COLOR_RED_DARK = Color(0xFFB20710)
    internal val COLOR_SHADOW = Color(0x21000000)
    internal const val INTRO_ANIMATION_MILLIS = 550
}

@Composable
fun NetflixLogo(
    animated: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .aspectRatio(NetflixLogo.ASPECT_RATIO)
            // Needs to be set so that the canvas is rendered in a separate layer,
            // otherwise the clipped shadow is visible on the background.
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            },
    ) {
        drawNetflixN(
            drawPercent = 1f, // TODO replace with animating percent later
            drawShadow = true,
        )
    }
}
