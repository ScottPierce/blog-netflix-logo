package dev.scottpierce.netflix.logo.util

import org.junit.Test
import kotlin.test.assertTrue

class PercentUtilTest {
    @Test
    fun percentWindow_WithinWindow() {
        assertEqualsInexact(expected = 0f, actual = 0f.percentWindow(start = 0f, end = 1f))
        assertEqualsInexact(expected = 0f, actual = 0f.percentWindow(start = 0.5f, end = 0.6f))
        assertEqualsInexact(expected = 0.5f, actual = 0.2f.percentWindow(start = 0.1f, end = 0.3f))
        assertEqualsInexact(expected = 1f, actual = 0.3f.percentWindow(start = 0.1f, end = 0.3f))
        assertEqualsInexact(expected = 1f, actual = 1f.percentWindow(start = 0f, end = 1f))
    }

    @Test
    fun percentWindow_BelowWindow() {
        assertEqualsInexact(expected = 0f, actual = 0f.percentWindow(start = 1f, end = 2f))
        assertEqualsInexact(expected = -1f, actual = 0f.percentWindow(start = 1f, end = 2f, clamp = false))
        assertEqualsInexact(expected = -0.5f, actual = 0.5f.percentWindow(start = 0.6f, end = 0.8f, clamp = false))
        assertEqualsInexact(expected = -2f, actual = 0.4f.percentWindow(start = 0.6f, end = 0.7f, clamp = false))
    }

    @Test
    fun percentWindow_AboveWindow() {
        assertEqualsInexact(expected = 1f, actual = 0.3f.percentWindow(start = 0.1f, end = 0.2f))
        assertEqualsInexact(expected = 2f, actual = 0.3f.percentWindow(start = 0.1f, end = 0.2f, clamp = false))
        assertEqualsInexact(expected = 1f, actual = 3f.percentWindow(start = 1f, end = 2f))
        assertEqualsInexact(expected = 2f, actual = 3f.percentWindow(start = 1f, end = 2f, clamp = false))
    }
}

private fun assertEqualsInexact(
    expected: Float,
    actual: Float,
    within: Float = 0.001f,
    message: String? = null
) {
    assertTrue(
        actual = (expected - within) <= actual && (expected + within) >= actual,
        message = message ?: "Expected value $expected, but was $actual."
    )
}
