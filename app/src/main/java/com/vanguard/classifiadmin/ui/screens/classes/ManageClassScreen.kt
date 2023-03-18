package com.vanguard.classifiadmin.ui.screens.classes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.StagedItemIcon
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val MANAGE_CLASS_SCREEN = "manage_class_screen"


@Composable
fun ManageClassScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

    }
}

/**
@Composable
fun ManageClassScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    maxHeight: Dp,
    onManageSubjects: () -> Unit,
) {
    val constraints = manageClassScreenContentConstraints(16.dp)
    val innerModifier = Modifier
    val verticalScroll = rememberScrollState()
    val selectedClassManageClass by viewModel.selectedClassManageClass.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(verticalScroll)
    ) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp, vertical = 32.dp),
            elevation = 2.dp, shape = RoundedCornerShape(16.dp)
        ) {
            BoxWithConstraints(modifier = modifier) {
                ConstraintLayout(
                    modifier = modifier.fillMaxWidth(),
                    constraintSet = constraints,
                ) {
                    StagedItemIcon(
                        modifier = innerModifier.layoutId("classIcon"),
                        iconSize = 52.dp,
                        surfaceSize = 60.dp,
                        color = Color(generateColorFromClassName(selectedClassManageClass ?: ""))
                    )

                    Text(
                        modifier = innerModifier.layoutId("className"),
                        text = selectedClassManageClass ?: "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(generateColorFromClassName(selectedClassManageClass ?: ""))
                    )

                    Text(
                        modifier = innerModifier.layoutId("classCode"),
                        text = selectedClassManageClass ?: "",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Black100.copy(0.3f)
                    )

                    Text(
                        modifier = innerModifier.layoutId("classSubjects"),
                        text = stringResource(id = R.string.class_subjects).uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )

                    ManageSubjectBox(
                        modifier = innerModifier
                            .padding(16.dp)
                            .layoutId("manageSubjectBox"),
                        onManageSubjects = onManageSubjects,
                        subjects = listOf(
                            "English",
                            "Science",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics Education",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics",
                            "Physics Education Science",
                            "Physics",
                            "Physics",
                        )
                    )

                    Text(
                        modifier = innerModifier.layoutId("classInfo"),
                        text = stringResource(id = R.string.class_information).uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )

                    ManageClassItem(
                        modifier = innerModifier.layoutId("classExtra"),
                        title = selectedClassManageClass ?: "",
                        subtitle = stringResource(id = R.string.current_class),
                        extra = stringResource(id = R.string.leave_class),
                        onExtra = {},
                        icon = R.drawable.icon_cap
                    )

                    ManageClassItem(
                        modifier = innerModifier.layoutId("schoolExtra"),
                        title = stringResource(id = R.string.future_leaders),
                        subtitle = stringResource(id = R.string.current_school),
                        extra = stringResource(id = R.string.leave_school),
                        onExtra = {},
                        icon = R.drawable.icon_school
                    )


                }
            }
        }
    }
}

*/


private fun manageClassScreenContentConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val classIcon = createRefFor("classIcon")
        val className = createRefFor("className")
        val classCode = createRefFor("classCode")
        val classSubjects = createRefFor("classSubjects")
        val manageSubjectBox = createRefFor("manageSubjectBox")
        val classInfo = createRefFor("classInfo")
        val classExtra = createRefFor("classExtra")
        val schoolExtra = createRefFor("schoolExtra")

        constrain(classIcon) {
            top.linkTo(parent.top, margin = 32.dp)
            start.linkTo(parent.start, margin = 0.dp)
            end.linkTo(parent.end, margin = 0.dp)
        }

        constrain(className) {
            top.linkTo(classIcon.bottom, 8.dp)
            start.linkTo(classIcon.start, 0.dp)
            end.linkTo(classIcon.end, 0.dp)
        }

        constrain(classCode) {
            top.linkTo(className.bottom, 8.dp)
            start.linkTo(className.start, 0.dp)
            end.linkTo(className.end, 0.dp)
        }

        constrain(classSubjects) {
            top.linkTo(classCode.bottom, 32.dp)
            start.linkTo(parent.start, margin)
        }

        constrain(manageSubjectBox) {
            top.linkTo(classSubjects.bottom, 8.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            width = Dimension.fillToConstraints
        }

        constrain(classInfo) {
            top.linkTo(manageSubjectBox.bottom, 16.dp)
            start.linkTo(parent.start, margin)
        }

        constrain(classExtra) {
            top.linkTo(classInfo.bottom, 8.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            width = Dimension.fillToConstraints
        }

        constrain(schoolExtra) {
            top.linkTo(classExtra.bottom, 0.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            width = Dimension.fillToConstraints
        }
    }
}


@Composable
fun ManageClassItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    extra: String,
    icon: Int,
    onExtra: () -> Unit,
) {
    val constraints = manageClassItemConstraints(8.dp)
    val innerModifier = Modifier

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.1f)
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            ManageClassItemIcon(icon = icon, modifier = innerModifier.layoutId("icon"))

            Text(
                modifier = innerModifier
                    .widthIn(max = 120.dp)
                    .layoutId("className"),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
            )

            Text(
                modifier = innerModifier
                    .widthIn(max = 120.dp)
                    .layoutId("code"),
                text = subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                color = Black100.copy(0.8f)
            )

            TextButton(onClick = onExtra, modifier = innerModifier.layoutId("manage")) {
                Text(
                    text = extra.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.error,
                )
            }
        }
    }

}


private fun manageClassItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val className = createRefFor("className")
        val code = createRefFor("code")
        val manage = createRefFor("manage")

        constrain(icon) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(className.top, margin = 0.dp)
        }

        constrain(className) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(icon.end, margin = 8.dp)
        }

        constrain(code) {
            top.linkTo(className.bottom, margin = 4.dp)
            start.linkTo(className.start, margin = 0.dp)
        }

        constrain(manage) {
            top.linkTo(className.top, margin = 0.dp)
            bottom.linkTo(code.bottom, margin = 0.dp)
            end.linkTo(parent.end, margin = 8.dp)
        }
    }
}

@Composable
fun ManageClassItemIcon(
    modifier: Modifier = Modifier,
    icon: Int,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colors.primary.copy(0.3f)
    ) {
        Box(
            modifier = modifier
                .padding(0.dp)
                .height(38.dp)
                .width(38.dp), contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = modifier
                    .size(24.dp)
                    .padding(4.dp),
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.manage_class_item_icon),
                tint = MaterialTheme.colors.onPrimary,
            )
        }
    }
}

@Composable
fun SubjectChip(
    modifier: Modifier = Modifier,
    subjectName: String,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        ),
        color = MaterialTheme.colors.primary.copy(0.1f)
    ) {
        Box(modifier = modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
            Text(
                text = subjectName,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                modifier = modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                maxLines = 1,
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManageSubjectBox(
    modifier: Modifier = Modifier,
    onManageSubjects: () -> Unit,
    subjects: List<String>,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.3f),
        )
    ) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ManageSubjectBoxContent(
                    modifier = modifier.fillMaxWidth(),
                    padding = 22,
                    subjects = subjects,
                )

                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onManageSubjects,
                        modifier = modifier
                            .clip(RoundedCornerShape(18.dp))
                            .padding(4.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.manage_subjects).uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ManageSubjectBoxContent(
    modifier: Modifier = Modifier,
    padding: Int,
    subjects: List<String>,
    content: @Composable (subjectName: String) -> Unit = { subjectName ->
        SubjectChip(subjectName = subjectName)
    }
) {
    var offsetX = padding
    var offsetY = padding

    Layout(
        modifier = Modifier.size(100.dp),
        content = {
            subjects.forEach { subject ->
                Box(modifier = Modifier.subjectData(subject), contentAlignment = Alignment.Center) {
                    content(subject)
                }
            }
        }
    ) { measurables, constraints ->

        val placeablesWithSubjects = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var containerWidth = 0
        var containerHeight = 0

        layout(containerWidth, containerHeight) {
            placeablesWithSubjects.forEach { placeable ->
                if (constraints.maxWidth - offsetX <= (placeable.width + 2 * padding)) {
                    offsetY += padding + placeable.height
                    offsetX = padding
                }
                placeable.placeRelative(x = offsetX, y = offsetY)
                offsetX += padding + placeable.width
                containerWidth += (2 * padding) + offsetX
                containerHeight += (2 * padding) + offsetY
            }
        }
    }
}


@Preview
@Composable
private fun ManageClassItemIconPreview() {
    ManageClassItemIcon(
        icon = R.drawable.icon_close
    )
}

@Preview
@Composable
private fun ManageClassItemPreview() {
    ManageClassItem(
        title = "YEAR 11 ICT",
        subtitle = "Current class",
        extra = "leave class",
        icon = R.drawable.icon_reports,
        onExtra = {}
    )
}

@Preview
@Composable
private fun SubjectChipPreview() {
    SubjectChip(subjectName = "Mathematics")
}

@Preview
@Composable
private fun ManageSubjectBoxPreview() {
    ManageSubjectBox(
        onManageSubjects = {},
        subjects = listOf()
    )
}

class SubjectDataModifier(
    private val subject: String,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any? = subject
}

fun Modifier.subjectData(subject: String) = then(SubjectDataModifier(subject))