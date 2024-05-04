package dev.scottpierce.netflix.logo

import androidx.compose.runtime.Immutable

@Immutable
internal data class DrawState(
    val stroke1: Stroke1State,
    val stroke2: Stroke2State,
    val stroke3: Stroke3State,
)

@Immutable
internal sealed interface Stroke1State {
    @Immutable
    data object Off : Stroke1State

    @Immutable
    @JvmInline
    value class Intro(
        val drawPercent: Float,
    ) : Stroke1State

    @Immutable
    @JvmInline
    value class Outro(
        val drawPercent: Float,
    ) : Stroke1State
}

@Immutable
internal sealed interface Stroke2State {
    @Immutable
    data object Off : Stroke2State

    @Immutable
    @JvmInline
    value class Intro(
        val drawPercent: Float,
    ) : Stroke2State

    @Immutable
    @JvmInline
    value class Outro(
        val drawPercent: Float,
    ) : Stroke2State
}

@Immutable
internal sealed interface Stroke3State {
    @Immutable
    data object Off : Stroke3State

    @Immutable
    @JvmInline
    value class Intro(
        val drawPercent: Float,
    ) : Stroke3State

    @Immutable
    @JvmInline
    value class Outro(
        val drawPercent: Float,
    ) : Stroke3State
}
