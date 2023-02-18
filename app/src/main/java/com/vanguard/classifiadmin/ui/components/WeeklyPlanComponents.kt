package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.SchoolWeek
import com.vanguard.classifiadmin.ui.theme.Black100
import java.util.UUID

@Composable
fun WeeklyPlanGrid(
    modifier: Modifier = Modifier,
    onTap: (String) -> Unit,
    onEdit: (String) -> Unit,
    isLocked: Boolean = false,
    headingColor: Color,
    headingBackgroundColor: Color,
    daysOfWeek: List<SchoolWeek> = SchoolWeek.values().toList(),
    plans: List<WeeklyPlanModel>,
    headings: List<String>,
    header: @Composable (heading: String, cellWidth: Dp, textPaddingX: Dp, color: Color, backgroundColor: Color) -> Unit = { h, m, t, c, b ->
        WeeklyPlanHeaderItem(
            heading = h,
            cellWidth = m,
            backgroundColor = b,
            color = c,
            textPaddingX = t,
        )
    },
    side: @Composable (color: Color, backgroundColor: Color, dayOfWeek: String, height: Dp, width: Dp) -> Unit = { _color, _backgroundColor, _dayOfWeek, _height, _width ->
        WeeklyPlanSideItem(
            color = _color,
            backgroundColor = _backgroundColor,
            dayOfWeekString = _dayOfWeek,
            height = _height,
            width = _width,
        )
    },
    row: @Composable (
        data: String,
        isLocked: Boolean,
        textPadding: Dp,
        onTap: (String) -> Unit,
        onEdit: (String) -> Unit
    ) -> Unit = { _data, _isLocked, _textPadding, _onTap, _onEdit ->
        WeeklyPlanEntryCell(
            data = _data,
            isLocked = _isLocked,
            onTap = _onTap,
            onEdit = _onEdit,
            textPadding = _textPadding,
        )
    }
) {
    var dayIndex = 0
    val numRows = 8
    val rowMinHeight = 24.dp
    val headerMinHeight = 28.dp
    val cellWidth = 200.dp
    val rowMinWidth = 120.dp
    val textPadding = 16.dp
    var dataIndex = 0
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val sideBarWidth = 42.dp
    val sideBarItemHeight = 400.dp



    Column(modifier = Modifier) {
        WeeklyPlanHeader(
            cellWidth = cellWidth,
            headingColor = headingColor,
            headingBackgroundColor = headingBackgroundColor,
            headings = headings,
            content = header,
            textPaddingX = textPadding,
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
                .padding(
                    start = with(LocalDensity.current) {
                        52.dp
                    }
                )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(verticalScrollState)
        ) {
            daysOfWeek.forEach { day ->
                Row(
                    modifier = Modifier
                ) {
                    WeeklyPlanSide(
                        height = sideBarItemHeight,
                        width = sideBarWidth,
                        dayOfWeek = day,
                        content = side,
                        modifier = Modifier
                    )

                    /**

                    Column(modifier = Modifier) {
                    repeat(numRows) { rowIndex ->
                    WeeklyPlanRow(
                    minHeight = rowMinHeight,
                    minWidth = rowMinWidth,
                    isLocked = isLocked,
                    textPadding = textPadding,
                    weeklyPlan = plans[dataIndex],
                    onTap = onTap,
                    onEdit = onEdit,
                    content = row,
                    )
                    dataIndex++
                    }
                    }

                     */
                }

                dayIndex++
            }
        }
    }
}

@Composable
fun WeeklyPlanRow(
    modifier: Modifier = Modifier,
    minHeight: Dp,
    minWidth: Dp,
    isLocked: Boolean,
    textPadding: Dp,
    weeklyPlan: WeeklyPlanModel,
    onTap: (String) -> Unit,
    onEdit: (String) -> Unit,
    color: Color = MaterialTheme.colors.primary,
    content: @Composable (
        data: String,
        isLocked: Boolean,
        textPadding: Dp,
        onTap: (String) -> Unit,
        onEdit: (String) -> Unit
    ) -> Unit = { _data, _isLocked, _textPadding, _onTap, _onEdit ->
        WeeklyPlanEntryCell(
            data = _data,
            isLocked = _isLocked,
            onTap = _onTap,
            onEdit = _onEdit,
            textPadding = _textPadding,
        )
    }
) {
    val numCols = 6
    val numLines = 2
    val height = minHeight + textPadding.times(2)
    val width = minWidth + textPadding.times(2)

    val dividerColor = color.copy(0.5f)

    Layout(
        modifier = Modifier
            .drawBehind {
                //horizontal
                repeat(numLines) { rowIndex ->
                    drawLine(
                        color = dividerColor,
                        start = Offset(
                            0f, rowIndex * height.toPx()
                        ),
                        end = Offset(
                            size.width, rowIndex * height.toPx()
                        ),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                //vertical
                repeat(numCols) { colIndex ->
                    drawLine(
                        color = dividerColor,
                        start = Offset(
                            colIndex * width.toPx(), 0f
                        ),
                        end = Offset(
                            colIndex * width.toPx(), size.height
                        ),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            },
        content = {
            //spread out the weekly plan model across the row
            repeat(numCols) { col ->
                val field = when (col) {
                    0 -> weeklyPlan.period.toString()
                    1 -> weeklyPlan.subject
                    2 -> weeklyPlan.pages
                    3 -> weeklyPlan.topic
                    4 -> weeklyPlan.homework
                    5 -> weeklyPlan.notes
                    else -> weeklyPlan.notes
                }
                Box(modifier = Modifier.weeklyPlanModelData(field ?: "")) {
                    content(
                        data = field ?: "",
                        isLocked = isLocked,
                        onTap = onTap,
                        onEdit = onEdit,
                        textPadding = textPadding,
                    )
                }
            }
        }
    ) { measurables, constraints ->

        val placeables = measurables.map { measurable ->
            val field = measurable.parentData as String
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = width.roundToPx(),
                    maxWidth = width.roundToPx(),
                    minHeight = height.roundToPx(),
                    maxHeight = height.roundToPx(),
                )
            )

            Pair(placeable, field)
        }

        layout(width.roundToPx(), height.roundToPx()) {
            var start = 0
            placeables.forEach { (placeable, field) ->
                val positionX = start * width.roundToPx()
                val positionY = 0
                placeable.place(positionX, positionY)
                start++
            }
        }

    }

}


@Composable
fun WeeklyPlanEntryCell(
    modifier: Modifier = Modifier,
    data: String = "",
    isLocked: Boolean = false,
    textPadding: Dp,
    onTap: (String) -> Unit,
    onEdit: (String) -> Unit,
) {
    val constraints = weeklyPlanEntryCellConstraints(8.dp)
    val innerModifier = Modifier

    Box(modifier = modifier
        .border(
            width = 1.dp,
            color = Black100.copy(alpha = 0.5f),
            shape = RoundedCornerShape(0.dp)
        )
        .pointerInput(Unit) {
            detectTapGestures(
                onLongPress = {
                    onEdit(data)
                },
                onDoubleTap = {
                    onEdit(data)
                },
                onTap = {
                    onTap(data)
                }
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier,
            constraintSet = constraints,
        ) {
            Text(
                text = data,
                color = Black100,
                fontSize = 12.sp,
                modifier = innerModifier
                    .layoutId("text")
                    .padding(textPadding)
            )

            Icon(
                painter = painterResource(R.drawable.icon_lock),
                contentDescription = stringResource(id = R.string.locked),
                modifier = innerModifier
                    .layoutId("locked")
                    .size(8.dp),
                tint = Black100.copy(0.5f)
            )
        }
    }
}

private fun weeklyPlanEntryCellConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val text = createRefFor("text")
        val locked = createRefFor("locked")

        constrain(text) {
            top.linkTo(parent.top, 0.dp)
            bottom.linkTo(parent.bottom, 0.dp)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
        }

        constrain(locked) {
            end.linkTo(parent.end, 2.dp)
            top.linkTo(parent.top, 2.dp)
        }
    }
}

@Composable
fun WeeklyPlanSide(
    modifier: Modifier = Modifier,
    height: Dp,
    width: Dp,
    color: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.surface,
    dayOfWeek: SchoolWeek,
    content: @Composable (color: Color, backgroundColor: Color, dayOfWeek: String, height: Dp, width: Dp) -> Unit = { _color, _backgroundColor, _dayOfWeek, _height, _width ->
        WeeklyPlanSideItem(
            color = _color,
            backgroundColor = _backgroundColor,
            dayOfWeekString = _dayOfWeek,
            height = _height,
            width = _width,
        )
    }
) {
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
            )
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(0.dp)
            ),
        contentAlignment = Alignment.Center) {
        content(
            color = color,
            backgroundColor = backgroundColor,
            dayOfWeek = dayOfWeek.name,
            height = height,
            width = width,
        )
    }
}


@Composable
fun WeeklyPlanSideItem(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.surface,
    dayOfWeekString: String,
    height: Dp,
    width: Dp,
) {
    Box(
        modifier = Modifier
            .height(height)
            .width(width)
            .border(color = color, width = 1.dp)
            .background(color = backgroundColor), contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayOfWeekString,
            fontSize = 14.sp,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .rotate(-90f)
        )
    }
}


@Composable
fun WeeklyPlanHeader(
    modifier: Modifier = Modifier,
    cellWidth: Dp,
    textPaddingX: Dp,
    headingColor: Color,
    headingBackgroundColor: Color,
    headings: List<String>,
    content: @Composable (heading: String, cellWidth: Dp, textPaddingX: Dp, color: Color, backgroundColor: Color) -> Unit = { h, m, t, c, b ->
        WeeklyPlanHeaderItem(
            heading = h,
            cellWidth = m,
            backgroundColor = b,
            color = c,
            textPaddingX = t,
        )
    }
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .border(
                width = 1.dp,
                color = headingColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ),
    ) {
        headings.forEach { heading ->
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                content(
                    heading = heading,
                    cellWidth = cellWidth,
                    textPaddingX = textPaddingX,
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
    textPaddingX: Dp,
    cellWidth: Dp,
    backgroundColor: Color = MaterialTheme.colors.surface,
    color: Color = MaterialTheme.colors.primary,
) {
    Box(
        modifier = Modifier
            .width(cellWidth)
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
                horizontal = textPaddingX
            )
        )
    }
}


@Composable
@Preview
private fun WeeklyPlanHeaderItemPreview() {
    WeeklyPlanHeaderItem(
        heading = "Class",
        cellWidth = 72.dp,
        textPaddingX = 16.dp
    )
}

@Composable
@Preview
private fun WeeklyPlanSideItemPreview() {
    WeeklyPlanSideItem(
        dayOfWeekString = "Mon",
        height = 400.dp,
        width = 58.dp,
    )
}

@Preview
@Composable
private fun WeeklyPlanEntryCellPreview() {
    WeeklyPlanEntryCell(
        data = "Mathematics",
        onTap = {},
        onEdit = {},
        textPadding = 16.dp,
    )
}


class WeeklyPlanModelDataModifier(
    private val weeklyPlanField: String,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any? = weeklyPlanField
}

fun Modifier.weeklyPlanModelData(weeklyPlanField: String) =
    then(WeeklyPlanModelDataModifier(weeklyPlanField))

data class WeeklyPlanModel(
    val planId: String,
    var period: String? = null,
    var subject: String? = null,
    var pages: String? = null,
    var topic: String? = null,
    var homework: String? = null,
    var notes: String? = null,
    var editedBy: String? = null,
    var date: String? = null,
    var lastModified: String? = null,
) {
    companion object {
        fun init(): List<WeeklyPlanModel> {
            val plans = mutableListOf<WeeklyPlanModel>()
            var periodCount = 0
            val entries = 40

            for (i in 0 until entries) {
                if (periodCount > 7) periodCount = 0
                val period = when (periodCount) {
                    0 -> "${periodCount + 1}st"
                    1 -> "${periodCount + 1}nd"
                    2 -> "${periodCount + 1}rd"
                    else -> "${periodCount + 1}th"
                }
                plans.add(
                    WeeklyPlanModel(
                        planId = UUID.randomUUID().toString(),
                        period = period,
                    )
                )
                periodCount++
            }

            return plans
        }
    }
}
