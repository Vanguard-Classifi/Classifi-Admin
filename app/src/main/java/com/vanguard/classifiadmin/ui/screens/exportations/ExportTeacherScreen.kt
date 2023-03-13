package com.vanguard.classifiadmin.ui.screens.exportations

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithDone
import com.vanguard.classifiadmin.ui.components.ClassFilterManageButton
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.components.StagedItemIcon
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminDetailFeature
import com.vanguard.classifiadmin.ui.screens.importations.ImportTeacherScreenContent
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch

const val EXPORT_TEACHER_SCREEN = "export_teacher_screen"

@Composable
fun ExportTeacherScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
    onTeacherExported: () -> Unit,
) {
    val TAG = "ExportTeacherScreen"
    val exportTeacherBuffer by viewModel.exportTeacherBuffer.collectAsState()
    val exportTeacherBufferListener by viewModel.exportTeacherBufferListener.collectAsState()
    val showDone: MutableState<Boolean> =
        remember(exportTeacherBufferListener) { mutableStateOf(exportTeacherBuffer.isNotEmpty()) }

    LaunchedEffect(Unit) {
        viewModel.onIncExportTeacherBufferListener()
    }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithDone(
                    onBack = onBack,
                    heading = stringResource(id = R.string.export_teachers),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    showDone = showDone.value,
                    onDone = {
                        // close screen , go back to manage class detail
                        viewModel.onManageClassAdminDetailFeatureChanged(
                            ManageClassAdminDetailFeature.ExportTeacher
                        )
                        onTeacherExported()
                    }
                )
            },
            content = {
                ExportTeacherScreenContent(
                    viewModel = viewModel,
                    onBack = onBack,
                    modifier = modifier.padding(it),
                    maxHeight = maxHeight,
                    onTeacherExported = onTeacherExported
                )
            }
        )
    }
}


@Composable
fun ExportTeacherScreenContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
    maxHeight: Dp,
    onTeacherExported: () -> Unit,
) {
    // list of all classes
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedClassesNetwork by viewModel.verifiedClassesNetwork.collectAsState()
    val exportTeacherBuffer by viewModel.exportTeacherBuffer.collectAsState()
    val exportTeacherBufferListener by viewModel.exportTeacherBufferListener.collectAsState()
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        viewModel.clearExportTeacherBuffer()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(exportTeacherBufferListener) {
        viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
    }


    Column(modifier = Modifier) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 2.dp,
        ) {
            when (verifiedClassesNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedClassesNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                            state = rememberLazyListState(),
                        ) {
                            items(verifiedClassesNetwork.data!!) { myClass ->
                                ClassListExportItemAdmin(
                                    myClass = myClass.toLocal(),
                                    onHold = { selectedClass ->
                                        viewModel.onAddToExportTeacherBuffer(myClass.classId.orEmpty())
                                        viewModel.onIncExportTeacherBufferListener()
                                    },
                                    onTap = { selectedClass ->
                                        if (exportTeacherBuffer.isEmpty()) {
                                            scope.launch {
                                                onTeacherExported()
                                                viewModel.onManageClassAdminDetailFeatureChanged(
                                                    ManageClassAdminDetailFeature.ExportTeacher
                                                )
                                            }.invokeOnCompletion {
                                                runnableBlock {
                                                    viewModel.onAddToExportTeacherBuffer(
                                                        myClass.classId.orEmpty()
                                                    )
                                                }
                                            }
                                        } else {
                                            if (exportTeacherBuffer.contains(myClass.classId)) {
                                                viewModel.onRemoveFromExportTeacherBuffer(myClass.classId.orEmpty())
                                            } else {
                                                viewModel.onAddToExportTeacherBuffer(myClass.classId.orEmpty())
                                            }
                                            viewModel.onIncExportTeacherBufferListener()
                                        }
                                    },
                                    selected = exportTeacherBuffer.contains(myClass.classId),
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight,
                        message = stringResource(id = R.string.error_occurred_classes),
                        buttonLabel = stringResource(id = R.string.go_back),
                        onClick = onBack,
                    )
                }
            }
        }
    }
}


@Composable
fun ClassListExportItemAdmin(
    modifier: Modifier = Modifier,
    myClass: ClassModel,
    selected: Boolean = false,
    onHold: (ClassModel) -> Unit,
    onTap: (ClassModel) -> Unit,
) {
    val constraints = ClassListItemConstraints(8.dp)
    val innerModifier = Modifier

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onHold(myClass)
                    },
                    onTap = {
                        onTap(myClass)
                    }
                )
            }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
                0.1f
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
                color = Color(generateColorFromClassName(myClass.className ?: "")),
            )

            Text(
                modifier = innerModifier.layoutId("className"),
                text = myClass.className ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(generateColorFromClassName(myClass.className ?: ""))
            )

            Text(
                modifier = innerModifier.layoutId("code"),
                text = myClass.classCode ?: "",
                fontSize = 12.sp,
                color = Black100.copy(0.8f)
            )

        }
    }
}

private fun ClassListItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val className = createRefFor("className")
        val code = createRefFor("code")

        constrain(icon) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(parent.top, margin = 0.dp)
            bottom.linkTo(parent.bottom, margin = 0.dp)
        }

        constrain(className) {
            top.linkTo(icon.top, margin = 0.dp)
            start.linkTo(icon.end, margin = 8.dp)
        }

        constrain(code) {
            top.linkTo(className.bottom, margin = 4.dp)
            start.linkTo(className.start, margin = 0.dp)
            bottom.linkTo(icon.bottom, 0.dp)
        }
    }
}
