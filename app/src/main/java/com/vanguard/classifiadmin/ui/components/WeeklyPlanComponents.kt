package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.constraintlayout.compose.Dimension
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
    cellSelected: Boolean = false,
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
        selected: Boolean,
        textPadding: Dp,
        cellWidth: Dp,
        cellHeight: Dp,
        onTap: (String) -> Unit,
        onEdit: (String) -> Unit
    ) -> Unit = { _data, _isLocked, _selected, _textPadding, _cellWidth, _cellHeight, _onTap, _onEdit ->
        WeeklyPlanEntryCell(
            data = _data,
            isLocked = _isLocked,
            selected = _selected,
            onTap = _onTap,
            onEdit = _onEdit,
            textPadding = _textPadding,
            cellWidth = _cellWidth,
            cellHeight = _cellHeight,
        )
    }
) {
    var dayIndex = 0
    val numRows = 8
    val numCols = headings.size
    val cellWidth = 200.dp
    val textPadding = 16.dp
    var dataIndex = 0
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val sideBarWidth = 42.dp
    val sideBarItemHeight = 400.dp
    val rowHeight = sideBarItemHeight.div(numRows)
    val rowWidth = cellWidth.times(numCols)
    val numDays = daysOfWeek.size



    Column(modifier = Modifier.fillMaxSize()) {
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
                        sideBarWidth
                    }
                )
        )

        Row(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
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
                    }

                    dayIndex++
                }
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
            ) {
                repeat(numRows * numDays) { rowIndex ->
                    WeeklyPlanRow(
                        cellWidth = cellWidth,
                        cellHeight = rowHeight,
                        isLocked = isLocked,
                        textPadding = textPadding,
                        weeklyPlan = plans[rowIndex],
                        onTap = onTap,
                        onEdit = onEdit,
                        content = row,
                        selected = cellSelected,
                    )
                }
            }
        }

    }
}

@Composable
fun WeeklyPlanRow(
    modifier: Modifier = Modifier,
    cellWidth: Dp,
    cellHeight: Dp,
    isLocked: Boolean,
    selected: Boolean,
    textPadding: Dp,
    weeklyPlan: WeeklyPlanModel,
    onTap: (String) -> Unit,
    onEdit: (String) -> Unit,
    color: Color = MaterialTheme.colors.primary,
    content: @Composable (
        data: String,
        isLocked: Boolean,
        selected: Boolean,
        textPadding: Dp,
        cellWidth: Dp,
        cellHeight: Dp,
        onTap: (String) -> Unit,
        onEdit: (String) -> Unit
    ) -> Unit = { _data, _isLocked, _selected,  _cellWidth, _cellHeight, _textPadding, _onTap, _onEdit ->
        WeeklyPlanEntryCell(
            data = _data,
            isLocked = _isLocked,
            selected = _selected,
            onTap = _onTap,
            onEdit = _onEdit,
            textPadding = _textPadding,
            cellWidth = _cellWidth,
            cellHeight = _cellHeight,
        )
    }
) {
    val numCols = 6
    Row(modifier = Modifier.padding(end = 8.dp)) {
        repeat(numCols) { itemIndex ->
            val field = when (itemIndex) {
                0 -> weeklyPlan.period
                1 -> weeklyPlan.subject
                2 -> weeklyPlan.pages
                3 -> weeklyPlan.topic
                4 -> weeklyPlan.homework
                5 -> weeklyPlan.notes
                else -> weeklyPlan.notes
            }

            content(
                data = field ?: "",
                isLocked = isLocked,
                textPadding = textPadding,
                cellWidth = cellWidth,
                cellHeight = cellHeight,
                onTap = onTap,
                onEdit = onEdit,
                selected = selected,
            )
        }
    }

}


@Composable
fun WeeklyPlanEntryCell(
    modifier: Modifier = Modifier,
    data: String = "",
    cellWidth: Dp,
    cellHeight: Dp,
    isLocked: Boolean = false,
    selected: Boolean = false,
    textPadding: Dp,
    onTap: (String) -> Unit,
    onEdit: (String) -> Unit,
) {
    val constraints = weeklyPlanEntryCellConstraints(8.dp)
    val innerModifier = Modifier

    Box(modifier = modifier
        .width(cellWidth)
        .height(cellHeight)
        .border(
            width = if(selected) 2.dp else 1.dp,
            color = if(selected) Black100 else Black100.copy(alpha = 0.3f),
            shape = RoundedCornerShape(0.dp)
        )
        .pointerInput(Unit) {
            detectTapGestures(
                onLongPress = {
                    if (!isLocked) onEdit(data)
                },
                onDoubleTap = {
                    if (!isLocked) onEdit(data)
                },
                onTap = {
                    if (!isLocked) onTap(data)
                }
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth(),
            constraintSet = constraints,
        ) {
            Text(
                text = data,
                color = Black100,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = innerModifier
                    .layoutId("text")
                    .padding(textPadding)
            )

            if (isLocked) {
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
            width = Dimension.fillToConstraints
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
        contentAlignment = Alignment.Center
    ) {
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
            .padding(end = 8.dp,top = 8.dp)
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
        cellHeight = 48.dp,
        cellWidth = 200.dp
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
