package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WeeklyPlanContent(
    modifier: Modifier = Modifier,
) {
    Column(modifier = Modifier) {
        //header
        Row(modifier = Modifier.weight(1f)) {
            //body
        }
    }

}


@Composable
fun WeeklyPlanSideItem(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.surface,
    dayOfWeekString: String,
     minHeight: Dp,
) {
    Box(modifier = Modifier
        .heightIn(min = minHeight)
        .border(color = color, width = 1.dp)
        .background(color = backgroundColor), contentAlignment = Alignment.Center) {
        Text(
            text = dayOfWeekString,
            fontSize = 12.sp,
            color = color,
            modifier = Modifier.padding(16.dp)
        )
    }
}



@Composable
fun WeeklyPlanHeader(
    modifier: Modifier = Modifier,
    minWidth: Dp,
    headingColor: Color,
    headingBackgroundColor: Color,
    headings: List<String>,
    content: @Composable (heading: String, minWidth: Dp, color: Color, backgroundColor: Color) -> Unit = { h, m, c, b ->
        WeeklyPlanHeaderItem(
            heading = h,
            minWidth = m,
            backgroundColor = b,
            color = c,
        )
    }
) {
    Row(
        modifier = Modifier.padding(4.dp)
            .border(
                width = 1.dp,
                color = headingColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        headings.forEach { heading ->
            Box(modifier = Modifier.widthIn(min = minWidth), contentAlignment = Alignment.Center) {
                content(
                    heading = heading,
                    minWidth = minWidth,
                    color = headingColor,
                    backgroundColor = headingBackgroundColor,
                )
            }
        }
    }
}

@Composable
fun WeeklyPlanHeaderItem(
    modifier: Modifier = Modifier,
    heading: String,
    minWidth: Dp,
    backgroundColor: Color = MaterialTheme.colors.surface,
    color: Color = MaterialTheme.colors.primary,
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .widthIn(min = minWidth)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(0.dp)
            )
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(0.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = heading,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = color,
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 24.dp
            )
        )
    }
}

@Composable
@Preview
private fun WeeklyPlanHeaderItemPreview() {
    WeeklyPlanHeaderItem(
        heading = "Class",
        minWidth = 72.dp,
    )
}

@Composable
@Preview
private fun WeeklyPlanSideItemPreview() {
    WeeklyPlanSideItem(
        dayOfWeekString = "Monday",
        minHeight = 72.dp
    )
}