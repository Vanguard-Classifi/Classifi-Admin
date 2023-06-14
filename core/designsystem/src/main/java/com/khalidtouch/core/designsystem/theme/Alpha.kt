package com.khalidtouch.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf


data class ContentAlpha(
    val alpha: Float = 0f,
)

val LocalContentAlpha = staticCompositionLocalOf { ContentAlpha() }