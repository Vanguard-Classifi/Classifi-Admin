package com.vanguard.classifiadmin.ui.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.domain.extensions.frequencies
import com.vanguard.classifiadmin.domain.extensions.toGrade
import com.vanguard.classifiadmin.domain.extensions.toPercentage
import com.vanguard.classifiadmin.domain.helpers.generateColorFromGrade
import java.text.DecimalFormat
import kotlin.math.roundToLong


@Composable
fun GradePreviewBar(
    modifier: Modifier = Modifier,
    grades: List<Char> = listOf('A','B','C','D','E','F')
) {
    Row(modifier = modifier) {
        grades.forEach {
            GradePreviewBarItem(grade = it, percentage = 0.67f)
        }
    }
}


@Composable
fun GradePreviewBarItem(
    modifier: Modifier = Modifier,
    grade: Char,
    percentage: Float,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .background(
                    color = Color(generateColorFromGrade(grade)),
                )
                .size(42.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = grade.toString(),
                color = MaterialTheme.colors.onPrimary,
                fontSize = 12.sp,
                modifier = modifier.padding(8.dp)
            )
        }

        Text(
            text = percentage.toPercentage(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(generateColorFromGrade(grade))
        )
    }
}


@Composable
fun ChartValueItem(
    modifier: Modifier = Modifier,
    heading: String,
    subtitle: String,
    color: Color = MaterialTheme.colors.primary
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = heading,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color,
            modifier = modifier,
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = color.copy(0.5f),
            modifier = modifier,
        )
    }
}


val DividerLengthInDegrees = 1.8f

@Composable
fun PerformanceCircle(
    modifier: Modifier = Modifier,
    values: List<Float>,
) {
    val uniqueValues = values.toSet()
    val dataSize = uniqueValues.size
    val frequencyMap = values.frequencies()

    val currentState = remember {
        MutableTransitionState(PerformanceCircleProgress.START)
            .apply { targetState = PerformanceCircleProgress.END }
    }
    val stroke = with(LocalDensity.current) {
        Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
    }

    val transition = updateTransition(currentState)
    val angleOffset by transition.animateFloat(
        label = "",
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 900,
                easing = LinearOutSlowInEasing
            )
        }
    ) { progress ->
        if (progress == PerformanceCircleProgress.START) 0f else 360f
    }

    val shift by transition.animateFloat(
        label = "",
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 900,
                easing = CubicBezierEasing(0f, 0.75f, 0.35f, 0.85f)
            )
        }
    ) { progress ->
        if (progress == PerformanceCircleProgress.START) 0f else 360f
    }

    var sweep by remember { mutableStateOf(0f) }
    var startAngle by remember { mutableStateOf(0f) }

    Canvas(
        modifier = modifier
            .height(400.dp)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius,
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        startAngle = shift - 45f

        val sorted = values.sorted()
        sorted.forEachIndexed { _, value ->
            //sweep = (frequencyMap[value]?.div(dataSize))?.times(angleOffset) ?: 0.0f
            sweep = (value / sorted.sum()) * angleOffset

            drawArc(
                color = Color(generateColorFromGrade(value.toGrade())),
                startAngle = startAngle + DividerLengthInDegrees / 2,
                sweepAngle = sweep - DividerLengthInDegrees,
                topLeft = topLeft,
                size = size,
                useCenter = false,
                style = stroke,
            )
            startAngle += sweep

        }
    }
}

enum class PerformanceCircleProgress {
    START, END
}


@Composable
@Preview
private fun PerformanceCirclePreview() {
    PerformanceCircle(
        values = listOf(
            0.2f,
            0.5f,
            0.67f,
            0.90f,
            0.96f,
            0.56f,
            0.23f,
        )
    )
}

@Composable
@Preview
private fun ChartValueItemPreview() {
    ChartValueItem(
        heading = "76%",
        subtitle = "AVG SCORE",
    )
}

@Preview
@Composable
private fun GradePreviewBarItemPreview() {
    GradePreviewBarItem(
        grade = 'A',
        percentage = 0.97f
    )
}