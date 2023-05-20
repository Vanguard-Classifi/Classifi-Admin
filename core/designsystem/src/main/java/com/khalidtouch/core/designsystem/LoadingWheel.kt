package com.khalidtouch.core.designsystem

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ClassifiLoadingWheel(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color,
        strokeWidth = ClassifiLoadingDefaults.strokeWidth,
        trackColor = color.copy(0.1f),
        strokeCap = StrokeCap.Round,
    )
}

object ClassifiLoadingDefaults {
    val strokeWidth = 8.dp
}

@Composable
@Preview
private fun ClassifiLoadingWheelPreview() {
    ClassifiLoadingWheel()
}