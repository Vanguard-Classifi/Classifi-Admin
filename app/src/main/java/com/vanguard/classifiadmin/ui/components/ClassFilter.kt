package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.UserRole
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun ClassFilterScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClose: () -> Unit,
    onManageClass: (ClassModel) -> Unit,
    onAddClass: () -> Unit,
) {
    val constraints = classFilterScreenConstraints(16.dp)
    val innerModifier = Modifier
    val classFilterBufferFeeds by viewModel.classFilterBufferFeeds.collectAsState()
    val classFilterBufferFeedsListener by viewModel.classFilterBufferFeedsListener.collectAsState()
    val currentUserRolePref by viewModel.currentUserRolePref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val verifiedClassesNetwork by viewModel.verifiedClassesNetwork.collectAsState()
    val verifiedClassesGivenTeacherNetwork by viewModel.verifiedClassesGivenTeacherNetwork.collectAsState()
    val verifiedClassesGivenStudentNetwork by viewModel.verifiedClassesGivenStudentNetwork.collectAsState()


    LaunchedEffect(classFilterBufferFeedsListener) {
        when (currentUserRolePref) {
            UserRole.Student.name -> {
                viewModel.getVerifiedClassesGivenStudentNetwork(
                    currentUserIdPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
            }

            UserRole.Teacher.name -> {
                viewModel.getVerifiedClassesGivenTeacherNetwork(
                    currentUserIdPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
            }

            UserRole.Admin.name -> {
                viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
            }

            UserRole.SuperAdmin.name -> {
                viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
            }

            else -> {}
        }

    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentUserRolePref()
        viewModel.getCurrentUserIdPref()

        when (currentUserRolePref) {
            UserRole.Student.name -> {
                viewModel.getVerifiedClassesGivenStudentNetwork(
                    currentUserIdPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
            }

            UserRole.Teacher.name -> {
                viewModel.getVerifiedClassesGivenTeacherNetwork(
                    currentUserIdPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
            }

            UserRole.Admin.name -> {
                viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
            }

            UserRole.SuperAdmin.name -> {
                viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
            }

            else -> {}
        }
    }


    Card(
        modifier = modifier.padding(horizontal = 16.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        BoxWithConstraints(modifier = modifier) {
            val maxHeight = maxHeight
            val maxWidth = maxWidth

            ConstraintLayout(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                constraintSet = constraints,
            ) {
                Text(
                    text = stringResource(id = R.string.my_classes),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = innerModifier.layoutId("title"),
                )

                Text(
                    text = stringResource(id = R.string.classes_you_are_part_of),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colors.primary,
                    modifier = innerModifier.layoutId("subtitle"),
                )

                RoundedIconButton(
                    onClick = onClose,
                    modifier = innerModifier.layoutId("close"),
                    icon = R.drawable.icon_close,
                    size = 18.dp,
                )

                LazyColumn(
                    modifier = innerModifier
                        .height(maxHeight / 2)
                        .layoutId("classesColumn"),
                    state = rememberLazyListState()
                ) {
                    when (currentUserRolePref) {
                        UserRole.Student.name -> {
                            when (verifiedClassesGivenStudentNetwork) {
                                is Resource.Loading -> {
                                    item {
                                        LoadingScreen(maxHeight = maxHeight)
                                    }
                                }

                                is Resource.Success -> {
                                    if (verifiedClassesGivenStudentNetwork.data?.isNotEmpty() == true) {
                                        // a student can only be assigned to one class
                                        items(verifiedClassesGivenStudentNetwork.data!!) { each ->
                                            ClassFilterItem(
                                                myClass = each.toLocal(),
                                                onSelectClass = {
                                                    if (!classFilterBufferFeeds.contains(each.classId)) {
                                                        viewModel.onAddToClassFilterBufferFeeds(each.classId.orEmpty())
                                                        viewModel.onIncClassFilterBufferFeedsListener()
                                                    } else {
                                                        viewModel.onRemoveFromClassFilterBufferFeeds(
                                                            each.classId.orEmpty()
                                                        )
                                                        viewModel.onDecClassFilterBufferFeedsListener()
                                                    }
                                                },
                                                onManageClass = {
                                                    viewModel.onSelectedClassManageClassChanged(it)
                                                    onManageClass(it)
                                                },
                                                onLongSelectClass = {
                                                    viewModel.onAddToClassFilterBufferFeeds(it.classId)
                                                    viewModel.onIncClassFilterBufferFeedsListener()
                                                },
                                                selected = false,
                                            )
                                        }
                                    } else {
                                        item {
                                            NoDataScreen(
                                                maxHeight = maxHeight,
                                                message = stringResource(id = R.string.not_belong_to_any_class),
                                                buttonLabel = "",
                                                onClick = {},
                                                showButton = false,
                                            )
                                        }
                                    }
                                }

                                is Resource.Error -> {
                                    item {
                                        NoDataScreen(
                                            maxHeight = maxHeight,
                                            message = stringResource(id = R.string.something_went_wrong),
                                            buttonLabel = "",
                                            onClick = {},
                                            showButton = false,
                                        )
                                    }
                                }
                            }
                        }

                        UserRole.Teacher.name -> {
                            when (verifiedClassesGivenTeacherNetwork) {
                                is Resource.Loading -> {
                                    item {
                                        LoadingScreen(maxHeight = maxHeight)
                                    }
                                }

                                is Resource.Success -> {
                                    if (verifiedClassesGivenTeacherNetwork.data?.isNotEmpty() == true) {
                                        // a student can only be assigned to one class
                                        items(verifiedClassesGivenTeacherNetwork.data!!) { each ->
                                            ClassFilterItem(
                                                myClass = each.toLocal(),
                                                onSelectClass = {
                                                    if (!classFilterBufferFeeds.contains(each.classId)) {
                                                        viewModel.onAddToClassFilterBufferFeeds(each.classId.orEmpty())
                                                        viewModel.onIncClassFilterBufferFeedsListener()
                                                    } else {
                                                        viewModel.onRemoveFromClassFilterBufferFeeds(
                                                            each.classId.orEmpty()
                                                        )
                                                        viewModel.onDecClassFilterBufferFeedsListener()
                                                    }
                                                },
                                                onManageClass = {
                                                    viewModel.onSelectedClassManageClassChanged(it)
                                                    onManageClass(it)
                                                },
                                                onLongSelectClass = {
                                                    viewModel.onAddToClassFilterBufferFeeds(it.classId)
                                                    viewModel.onIncClassFilterBufferFeedsListener()
                                                },
                                                selected = classFilterBufferFeeds.contains(each.classId),
                                            )
                                        }
                                    } else {
                                        item {
                                            NoDataScreen(
                                                maxHeight = maxHeight,
                                                message = stringResource(id = R.string.not_belong_to_any_class),
                                                buttonLabel = "",
                                                onClick = {},
                                                showButton = false,
                                            )
                                        }
                                    }
                                }

                                is Resource.Error -> {
                                    item {
                                        NoDataScreen(
                                            maxHeight = maxHeight,
                                            message = stringResource(id = R.string.something_went_wrong),
                                            buttonLabel = "",
                                            onClick = {},
                                            showButton = false,
                                        )
                                    }
                                }
                            }
                        }

                        UserRole.Admin.name -> {
                            when (verifiedClassesNetwork) {
                                is Resource.Loading -> {
                                    item {
                                        LoadingScreen(maxHeight = maxHeight)
                                    }
                                }

                                is Resource.Success -> {
                                    if (verifiedClassesNetwork.data?.isNotEmpty() == true) {
                                        // a student can only be assigned to one class
                                        items(verifiedClassesNetwork.data!!) { each ->
                                            ClassFilterItem(
                                                myClass = each.toLocal(),
                                                onSelectClass = {
                                                    if (!classFilterBufferFeeds.contains(each.classId)) {
                                                        viewModel.onAddToClassFilterBufferFeeds(each.classId.orEmpty())
                                                        viewModel.onIncClassFilterBufferFeedsListener()
                                                    } else {
                                                        viewModel.onRemoveFromClassFilterBufferFeeds(
                                                            each.classId.orEmpty()
                                                        )
                                                        viewModel.onDecClassFilterBufferFeedsListener()
                                                    }
                                                },
                                                onManageClass = {
                                                    viewModel.onSelectedClassManageClassChanged(it)
                                                    onManageClass(it)
                                                },
                                                onLongSelectClass = {
                                                    viewModel.onAddToClassFilterBufferFeeds(it.classId)
                                                    viewModel.onIncClassFilterBufferFeedsListener()
                                                },
                                                selected = classFilterBufferFeeds.contains(each.classId),
                                            )
                                        }
                                    } else {
                                        item {
                                            NoDataScreen(
                                                maxHeight = maxHeight,
                                                message = stringResource(id = R.string.not_belong_to_any_class),
                                                buttonLabel = "",
                                                onClick = {},
                                                showButton = false,
                                            )
                                        }
                                    }
                                }

                                is Resource.Error -> {
                                    item {
                                        NoDataScreen(
                                            maxHeight = maxHeight,
                                            message = stringResource(id = R.string.something_went_wrong),
                                            buttonLabel = "",
                                            onClick = {},
                                            showButton = false,
                                        )
                                    }
                                }
                            }
                        }

                        UserRole.SuperAdmin.name -> {
                            when (verifiedClassesNetwork) {
                                is Resource.Loading -> {
                                    item {
                                        LoadingScreen(maxHeight = maxHeight)
                                    }
                                }

                                is Resource.Success -> {
                                    if (verifiedClassesNetwork.data?.isNotEmpty() == true) {
                                        // a student can only be assigned to one class
                                        items(verifiedClassesNetwork.data!!) { each ->
                                            ClassFilterItem(
                                                myClass = each.toLocal(),
                                                onSelectClass = {
                                                    if (!classFilterBufferFeeds.contains(each.classId)) {
                                                        viewModel.onAddToClassFilterBufferFeeds(each.classId.orEmpty())
                                                        viewModel.onIncClassFilterBufferFeedsListener()
                                                    } else {
                                                        viewModel.onRemoveFromClassFilterBufferFeeds(
                                                            each.classId.orEmpty()
                                                        )
                                                        viewModel.onDecClassFilterBufferFeedsListener()
                                                    }
                                                },
                                                onManageClass = {
                                                    viewModel.onSelectedClassManageClassChanged(it)
                                                    onManageClass(it)
                                                },
                                                onLongSelectClass = {
                                                    viewModel.onAddToClassFilterBufferFeeds(it.classId)
                                                    viewModel.onIncClassFilterBufferFeedsListener()
                                                },
                                                selected = classFilterBufferFeeds.contains(each.classId),
                                            )
                                        }
                                    } else {
                                        item {
                                            NoDataScreen(
                                                maxHeight = maxHeight,
                                                message = stringResource(id = R.string.not_belong_to_any_class),
                                                buttonLabel = "",
                                                onClick = {},
                                                showButton = false,
                                            )
                                        }
                                    }
                                }

                                is Resource.Error -> {
                                    item {
                                        NoDataScreen(
                                            maxHeight = maxHeight,
                                            message = stringResource(id = R.string.something_went_wrong),
                                            buttonLabel = "",
                                            onClick = {},
                                            showButton = false,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                PrimaryTextButtonFillWidth(
                    label = stringResource(id = R.string.done),
                    onClick = {
                        //close class filter dialog
                        onAddClass()
                    },
                    modifier = innerModifier.layoutId("addClass"),
                )
            }
        }
    }
}

private fun classFilterScreenConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val title = createRefFor("title")
        val subtitle = createRefFor("subtitle")
        val classesColumn = createRefFor("classesColumn")
        val addClass = createRefFor("addClass")
        val close = createRefFor("close")

        constrain(title) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(parent.start, margin = margin)
        }

        constrain(subtitle) {
            start.linkTo(title.start, margin = 0.dp)
            top.linkTo(title.bottom, margin = 4.dp)
        }

        constrain(close) {
            top.linkTo(parent.top, margin = 8.dp)
            end.linkTo(parent.end, margin = 8.dp)
        }

        constrain(classesColumn) {
            top.linkTo(subtitle.bottom, margin = 12.dp)
            start.linkTo(parent.start, margin = 8.dp)
            end.linkTo(parent.end, margin = 8.dp)
            width = Dimension.fillToConstraints
        }

        constrain(addClass) {
            top.linkTo(classesColumn.bottom, margin = 16.dp)
            start.linkTo(classesColumn.start, margin = 0.dp)
            end.linkTo(classesColumn.end, margin = 0.dp)
            width = Dimension.fillToConstraints
        }
    }
}


@Composable
fun AddClassButton(
    modifier: Modifier = Modifier,
    onAddClass: () -> Unit,
) {
    val constraints = addClassButtonConstraints(8.dp)
    val innerModifier = Modifier

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onAddClass() },
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
            RoundedIconButton(
                onClick = onAddClass,
                modifier = innerModifier.layoutId("icon"),
                icon = R.drawable.icon_add,
            )

            Text(
                modifier = innerModifier.layoutId("className"),
                text = stringResource(id = R.string.add_another_class),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
            )

            Text(
                modifier = innerModifier.layoutId("code"),
                text = stringResource(id = R.string.create_join_class),
                fontSize = 12.sp,
                color = Black100.copy(0.8f)
            )
        }
    }
}


private fun addClassButtonConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val className = createRefFor("className")
        val code = createRefFor("code")

        constrain(icon) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(className.top, margin = 0.dp)
            bottom.linkTo(code.bottom, margin = 0.dp)
        }

        constrain(className) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(icon.end, margin = 8.dp)
        }

        constrain(code) {
            top.linkTo(className.bottom, margin = 4.dp)
            start.linkTo(className.start, margin = 0.dp)
        }

    }
}


@Composable
fun ClassFilterItem(
    modifier: Modifier = Modifier,
    myClass: ClassModel,
    selected: Boolean = false,
    onManageClass: (ClassModel) -> Unit,
    onSelectClass: (ClassModel) -> Unit,
    onLongSelectClass: (ClassModel) -> Unit,
) {
    val constraints = classFilterItemConstraints(8.dp)
    val innerModifier = Modifier

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        onLongSelectClass(myClass)
                    },
                    onLongPress = {
                        onLongSelectClass(myClass)
                    },
                    onTap = {
                        onSelectClass(myClass)
                    }
                )
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
                0.5f
            )
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            StagedItemIcon(
                modifier = innerModifier.layoutId("icon"),
                color = Color(generateColorFromClassName(myClass.className.orEmpty())),
            )

            Text(
                modifier = innerModifier.layoutId("className"),
                text = myClass.className.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(generateColorFromClassName(myClass.className.orEmpty()))
            )

            Text(
                modifier = innerModifier.layoutId("code"),
                text = myClass.classCode.orEmpty(),
                fontSize = 12.sp,
                color = Black100.copy(0.8f)
            )


            ClassFilterManageButton(
                modifier = innerModifier.layoutId("manage"),
                icon = R.drawable.icon_settings,
                label = stringResource(id = R.string.manage),
                className = myClass.className.orEmpty(),
                onSelect = { onManageClass(myClass) },
            )

        }
    }
}

private fun classFilterItemConstraints(margin: Dp): ConstraintSet {
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
            end.linkTo(parent.end, margin = margin)
        }
    }
}


@Composable
fun ClassFilterManageButton(
    modifier: Modifier = Modifier,
    icon: Int,
    label: String,
    className: String,
    onSelect: (String) -> Unit,
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colors.primary.copy(0.1f)
    ) {
        TextButton(
            onClick = { onSelect(className) },
            modifier = modifier
                .padding(0.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Icon(
                modifier = modifier
                    .size(24.dp)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = MaterialTheme.colors.primary,
            )

            Text(
                text = label,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun StagedItemIcon(
    modifier: Modifier = Modifier,
    color: Color,
    iconSize: Dp = 24.dp,
    surfaceSize: Dp = 38.dp,
    icon: Int = R.drawable.icon_cap,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = color
    ) {
        Box(
            modifier = modifier
                .padding(0.dp)
                .height(surfaceSize)
                .width(surfaceSize), contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = modifier
                    .size(iconSize)
                    .padding(4.dp),
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.class_icon),
                tint = MaterialTheme.colors.onPrimary,
            )
        }
    }
}

@Composable
fun RoundedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    size: Dp = 24.dp,
    surfaceSize: Dp = 28.dp,
    icon: Int,
    tint: Color = MaterialTheme.colors.primary,
    surfaceColor: Color = MaterialTheme.colors.primary.copy(0.1f)
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .size(surfaceSize),
        shape = CircleShape,
        color = surfaceColor,
    ) {
        IconButton(onClick = onClick, modifier = Modifier.clip(CircleShape)) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.add),
                modifier = Modifier.size(size),
                tint = tint,
            )
        }
    }
}


@Preview
@Composable
private fun ClassFilterManageButtonPreview() {
    ClassFilterManageButton(
        icon = R.drawable.icon_settings,
        label = "Manage",
        className = "",
        onSelect = {}
    )
}

@Composable
@Preview
private fun ClassIconPreview() {
    StagedItemIcon(
        color = Color(0xff000000)
    )
}

@Preview
@Composable
private fun RoundedIconButtonPreview() {
    RoundedIconButton(
        onClick = {},
        icon = R.drawable.icon_add
    )
}

data class Level(
    val name: String,
    val code: String
)