package com.vanguard.classifiadmin.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithOptions
import com.vanguard.classifiadmin.ui.components.DashboardMenuScreen
import com.vanguard.classifiadmin.ui.components.StagedItemIcon
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val MANAGE_CLASS_ADMIN_DETAIL_SCREEN =
    "manage_class_admin_detail_screen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ManageClassAdminDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    val selectedManageClassSubsectionItem by viewModel.selectedManageClassSubsectionItem.collectAsState()
    val selectedClassManageClassAdmin by viewModel.selectedClassManageClassAdmin.collectAsState()
    val optionState: MutableState<Boolean> = remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithOptions(
                    onBack = onBack,
                    iconTint = MaterialTheme.colors.primary,
                    heading = selectedClassManageClassAdmin?.className.orEmpty(),
                    onOptions = {
                        optionState.value = !optionState.value
                    },
                )
            },
            content = {
                ManageClassAdminDetailScreenContent(
                    modifier = modifier.padding(it),
                    viewModel = viewModel,
                    onBack = onBack,
                )
            }
        )


        AnimatedVisibility(
            visible = optionState.value,
            enter = scaleIn(
                initialScale = 0.8f, animationSpec = tween(
                    durationMillis = 50, easing = FastOutLinearInEasing
                )
            ),
            exit = scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(
                    durationMillis = 50, easing = FastOutLinearInEasing
                ),
            ),
        ) {
            Popup(alignment = Alignment.TopEnd,
                offset = IntOffset(0, 100),
                onDismissRequest = { optionState.value = false }) {
                when (selectedManageClassSubsectionItem) {
                    ManageClassSubsectionItem.Teachers -> {
                        ManageClassDetailPopupTeacherOptionsScreen(
                            onSelectOption = {
                                optionState.value = false
                            }
                        )
                    }

                    ManageClassSubsectionItem.Subjects -> {
                        ManageClassDetailPopupSubjectOptionsScreen(onSelectOption = {
                            optionState.value = false
                        })
                    }

                    ManageClassSubsectionItem.Students -> {
                        ManageClassDetailPopupStudentOptionsScreen(onSelectOption = {
                            optionState.value = false
                        })
                    }
                }
            }
        }
    }
}


@Composable
fun ManageClassAdminDetailScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val verticalScroll = rememberScrollState()
    val selectedClassManageClass by viewModel.selectedClassManageClassAdmin.collectAsState()
    val selectedManageClassSubsectionItem by viewModel.selectedManageClassSubsectionItem.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(verticalScroll)
    ) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp, vertical = 8.dp),
            elevation = 2.dp, shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                ClassBadge(
                    myClass = selectedClassManageClass ?: ClassModel.Default,
                )

                Spacer(modifier = Modifier.height(32.dp))

                ManageClassSubSectionRow(
                    viewModel = viewModel,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                when (selectedManageClassSubsectionItem) {
                    ManageClassSubsectionItem.Students -> {
                        ManageClassAdminDetailScreenContentStudents()
                    }

                    ManageClassSubsectionItem.Teachers -> {
                        ManageClassAdminDetailScreenContentTeachers()
                    }

                    ManageClassSubsectionItem.Subjects -> {
                        ManageClassAdminDetailScreenContentSubjects(
                            viewModel = viewModel,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ManageClassAdminDetailScreenContentStudents(
    modifier: Modifier = Modifier
) {
    Text(
        text = "students"
    )
}


@Composable
fun ManageClassAdminDetailScreenContentSubjects(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    Text(
        text = "subjects"
    )
}


@Composable
fun ManageClassAdminDetailScreenContentTeachers(
    modifier: Modifier = Modifier
) {
    Text(
        text = "teachers"
    )
}


@Composable
fun ClassBadge(
    modifier: Modifier = Modifier,
    myClass: ClassModel,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        StagedItemIcon(
            iconSize = 52.dp,
            surfaceSize = 60.dp,
            color = Color(generateColorFromClassName(myClass.className.orEmpty()))
        )

        Text(
            text = myClass.className.orEmpty(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(generateColorFromClassName(myClass.className.orEmpty()))
        )

        Text(
            text = myClass.classCode.orEmpty(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Black100.copy(0.3f)
        )
    }

}

@Composable
fun ManageClassSubSectionRow(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    items: List<ManageClassSubsectionItem> = ManageClassSubsectionItem.values().toList(),
) {
    val rowWidth: MutableState<Int> = remember { mutableStateOf(0) }
    val selectedManageClassSubsectionItem by viewModel.selectedManageClassSubsectionItem.collectAsState()


    Surface(
        modifier = modifier,
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .onGloballyPositioned { rowWidth.value = it.size.width },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { each ->
                ManageClassSubsectionItemButton(
                    item = each,
                    maxWidth = with(LocalDensity.current) {
                        rowWidth.value.toDp()
                    },
                    selected = selectedManageClassSubsectionItem == each,
                    onSelect = {
                        viewModel.onSelectedManageClassSubsectionItemChanged(it)
                    }
                )
            }
        }
    }

}

@Composable
fun ManageClassSubsectionItemButton(
    modifier: Modifier = Modifier,
    item: ManageClassSubsectionItem,
    selected: Boolean = false,
    maxWidth: Dp,
    onSelect: (ManageClassSubsectionItem) -> Unit,
) {
    Surface(
        modifier = Modifier
            .width(maxWidth.div(3f))
            .clickable { onSelect(item) },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) MaterialTheme.colors.primary else Color.Transparent
        ),
        color = if (selected) MaterialTheme.colors.primary.copy(0.1f) else Color.Transparent,
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.name.uppercase(),
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
                    0.5f
                ),
                modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

enum class ManageClassSubsectionItem {
    Students, Subjects, Teachers
}

enum class ManageClassDetailPopupTeacherOption(val title: String) {
    Enroll("Enroll"),
    MakeFormTeacher("Make Form Teacher"),
    Import("Import"),
    Export("Export"),
    Remove("Remove")
}

enum class ManageClassDetailPopupStudentOption(val title: String) {
    Enroll("Enroll"),
    MakePrefect("Make Prefect"),
    Import("Import"),
    Export("Export"),
    Remove("Remove")
}

enum class ManageClassDetailPopupSubjectOption(val title: String) {
    Import("Import"),
    Export("Export"),
    Remove("Remove")
}

@Composable
fun ManageClassDetailPopupTeacherOptionsScreen(
    modifier: Modifier = Modifier,
    onSelectOption: (ManageClassDetailPopupTeacherOption) -> Unit,
    options: List<ManageClassDetailPopupTeacherOption> = ManageClassDetailPopupTeacherOption.values()
        .toList(),
) {
    Card(
        modifier = modifier.padding(start = 92.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.onPrimary
    ) {
        Column(modifier = modifier) {
            Spacer(modifier = modifier.height(16.dp))
            options.forEach {
                ManageClassDetailTeacherOptionItem(
                    option = it,
                    onSelect = onSelectOption
                )
            }
            Spacer(modifier = modifier.height(16.dp))
        }
    }
}

@Composable
fun ManageClassDetailPopupStudentOptionsScreen(
    modifier: Modifier = Modifier,
    onSelectOption: (ManageClassDetailPopupStudentOption) -> Unit,
    options: List<ManageClassDetailPopupStudentOption> = ManageClassDetailPopupStudentOption.values()
        .toList(),
) {
    Card(
        modifier = modifier.padding(start = 92.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.onPrimary
    ) {
        Column(modifier = modifier) {
            Spacer(modifier = modifier.height(16.dp))
            options.forEach {
                ManageClassDetailStudentOptionItem(
                    option = it,
                    onSelect = onSelectOption
                )
            }
            Spacer(modifier = modifier.height(16.dp))
        }
    }
}


@Composable
fun ManageClassDetailPopupSubjectOptionsScreen(
    modifier: Modifier = Modifier,
    onSelectOption: (ManageClassDetailPopupSubjectOption) -> Unit,
    options: List<ManageClassDetailPopupSubjectOption> = ManageClassDetailPopupSubjectOption.values()
        .toList(),
) {
    Card(
        modifier = modifier.padding(start = 92.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.onPrimary
    ) {
        Column(modifier = modifier) {
            Spacer(modifier = modifier.height(16.dp))
            options.forEach {
                ManageClassDetailSubjectOptionItem(
                    option = it,
                    onSelect = onSelectOption
                )
            }
            Spacer(modifier = modifier.height(16.dp))
        }
    }
}


@Composable
fun ManageClassDetailTeacherOptionItem(
    modifier: Modifier = Modifier,
    option: ManageClassDetailPopupTeacherOption,
    onSelect: (ManageClassDetailPopupTeacherOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .clickable { onSelect(option) }
            .clip(RoundedCornerShape(2.dp)),
        shape = RoundedCornerShape(2.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = option.title,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun ManageClassDetailStudentOptionItem(
    modifier: Modifier = Modifier,
    option: ManageClassDetailPopupStudentOption,
    onSelect: (ManageClassDetailPopupStudentOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .clickable { onSelect(option) }
            .clip(RoundedCornerShape(2.dp)),
        shape = RoundedCornerShape(2.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = option.title,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun ManageClassDetailSubjectOptionItem(
    modifier: Modifier = Modifier,
    option: ManageClassDetailPopupSubjectOption,
    onSelect: (ManageClassDetailPopupSubjectOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .clickable { onSelect(option) }
            .clip(RoundedCornerShape(2.dp)),
        shape = RoundedCornerShape(2.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = option.title,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


@Composable
@Preview
private fun ClassBadgePreview() {
    ClassBadge(
        myClass = ClassModel(
            classId = "",
            className = "Grade 2",
            classCode = "GRD2"
        )
    )
}

@Preview
@Composable
private fun ManageClassSubsectionItemButtonPreview() {
    ManageClassSubsectionItemButton(
        item = ManageClassSubsectionItem.Students,
        selected = true,
        maxWidth = 400.dp,
        onSelect = {}
    )
}

