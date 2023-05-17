package com.khalidtouch.core.designsystem.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ClassifiSidePane(
    modifier: Modifier = Modifier,
    totalWidth: Dp,
    items: @Composable ColumnScope.() -> Unit,
    verticalScroll: ScrollState = rememberScrollState(),
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .widthIn(max = totalWidth.times(0.25f)),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        tonalElevation = ClassifiSidePaneDefaults.tonalElevation,
    ) {
        Column(modifier.verticalScroll(verticalScroll)) {
            items()
        }
    }
}


object ClassifiSidePaneDefaults {
    val tonalElevation = 2.dp
}


@Preview
@Composable
private fun ClassifiSidePanePreview() {
    ClassifiSidePane(
        totalWidth = 1440.dp,
        items = {
            Text("This is it")
        }
    )
}