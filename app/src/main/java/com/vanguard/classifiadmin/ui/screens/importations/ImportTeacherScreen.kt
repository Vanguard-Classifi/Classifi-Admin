package com.vanguard.classifiadmin.ui.screens.importations

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithDone
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminDetailFeature
import com.vanguard.classifiadmin.ui.screens.admin.VerifiedTeacherItem
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch

const val IMPORT_TEACHER_SCREEN = "import_teacher_screen"

@Composable
fun ImportTeacherScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
    onEnrollTeacher: () -> Unit,
    onTeacherImported: () -> Unit,
) {
    val importTeacherBuffer by viewModel.importTeacherBuffer.collectAsState()
    val importTeacherBufferListener by viewModel.importTeacherBufferListener.collectAsState()
    val showDone: MutableState<Boolean> =
        remember(importTeacherBufferListener) { mutableStateOf(importTeacherBuffer.isNotEmpty()) }

    LaunchedEffect(Unit) {
        viewModel.onIncImportTeacherBufferListener()
    }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithDone(
                    onBack = onBack,
                    heading = stringResource(id = R.string.import_teachers),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    showDone = showDone.value,
                    onDone = {
                        // close screen , go back to manage class detail
                        viewModel.onManageClassAdminDetailFeatureChanged(
                            ManageClassAdminDetailFeature.ImportTeacher
                        )
                        onTeacherImported()
                    }
                )
            },
            content = {
                ImportTeacherScreenContent(
                    modifier = modifier.padding(it),
                    viewModel = viewModel,
                    onEnrollTeacher = onEnrollTeacher,
                    maxHeight = maxHeight,
                    onBack = onBack,
                    onTeacherImported = onTeacherImported,
                )
            }
        )
    }
}


@Composable
fun ImportTeacherScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onEnrollTeacher: () -> Unit,
    maxHeight: Dp,
    onBack: () -> Unit,
    onTeacherImported: () -> Unit,
) {

    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedTeachersNetwork by viewModel.verifiedTeachersNetwork.collectAsState()
    val importTeacherBuffer by viewModel.importTeacherBuffer.collectAsState()
    val importTeacherBufferListener by viewModel.importTeacherBufferListener.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.clearImportTeacherBuffer()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getVerifiedTeachersNetwork(currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(importTeacherBufferListener) {
        viewModel.getVerifiedTeachersNetwork(currentSchoolIdPref.orEmpty())
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
            when (verifiedTeachersNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedTeachersNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                            state = rememberLazyListState(),
                        ) {
                            items(verifiedTeachersNetwork.data!!) { teacher ->
                                VerifiedTeacherItem(
                                    teacher = teacher.toLocal(),
                                    selected = importTeacherBuffer.contains(teacher.userId),
                                    onTap = { selectedTeacher ->
                                        if (importTeacherBuffer.isEmpty()) {
                                            scope.launch {
                                                onTeacherImported()
                                                viewModel.onManageClassAdminDetailFeatureChanged(
                                                    ManageClassAdminDetailFeature.ImportTeacher
                                                )
                                            }.invokeOnCompletion {
                                                runnableBlock {
                                                    viewModel.onAddToImportTeacherBuffer(
                                                        selectedTeacher.userId
                                                    )
                                                }
                                            }
                                        } else {
                                            if (importTeacherBuffer.contains(selectedTeacher.userId)) {
                                                viewModel.onRemoveFromImportTeacherBuffer(
                                                    selectedTeacher.userId
                                                )
                                            } else {
                                                viewModel.onAddToImportTeacherBuffer(selectedTeacher.userId)
                                            }
                                            viewModel.onIncImportTeacherBufferListener()
                                        }
                                    },
                                    onHold = { selectedTeacher ->
                                        viewModel.onAddToImportTeacherBuffer(selectedTeacher.userId)
                                        viewModel.onIncImportTeacherBufferListener()
                                    }
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight,
                        message = stringResource(id = R.string.error_occurred_teachers),
                        buttonLabel = stringResource(id = R.string.go_back),
                        onClick = onBack,
                    )
                }
            }
        }
    }
}