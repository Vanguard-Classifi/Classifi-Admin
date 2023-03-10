package com.vanguard.classifiadmin.ui.screens.importations

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithDone
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminDetailFeature
import com.vanguard.classifiadmin.ui.screens.admin.VerifiedStudentItem
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch

const val IMPORT_STUDENT_SCREEN = "import_student_screen"

@Composable
fun ImportStudentScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
    onEnrollStudent: () -> Unit,
    onStudentImported: () -> Unit,
) {
    val TAG = "ImportStudentScreen"
    val importStudentBuffer by viewModel.importStudentBuffer.collectAsState()
    val importStudentBufferListener by viewModel.importStudentBufferListener.collectAsState()
    val showDone: MutableState<Boolean> =
        remember(importStudentBufferListener) { mutableStateOf(importStudentBuffer.isNotEmpty()) }

    LaunchedEffect(Unit) {
        viewModel.onIncImportStudentBufferListener()
    }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithDone(
                    onBack = onBack,
                    heading = stringResource(id = R.string.import_students),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    showDone = showDone.value,
                    onDone = {
                        // close screen , go back to manage class detail
                        viewModel.onManageClassAdminDetailFeatureChanged(
                            ManageClassAdminDetailFeature.ImportStudent
                        )
                        onStudentImported()
                    }
                )
            },
            content = {
                ImportStudentScreenContent(
                    modifier = Modifier.padding(it),
                    viewModel = viewModel,
                    onEnrollStudent = onEnrollStudent,
                    maxHeight = maxHeight,
                    onBack = onBack,
                    onStudentImported = onStudentImported,
                )
            }
        )
    }
}


@Composable
fun ImportStudentScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onEnrollStudent: () -> Unit,
    maxHeight: Dp,
    onBack: () -> Unit,
    onStudentImported: () -> Unit,
) {
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedStudentsNetwork by viewModel.verifiedStudentsNetwork.collectAsState()
    val importStudentBufferListener by viewModel.importStudentBufferListener.collectAsState()
    val importStudentBuffer by viewModel.importStudentBuffer.collectAsState()
    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = Unit, block = {
        viewModel.clearImportStudentBuffer()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getVerifiedStudentsNetwork(currentSchoolIdPref.orEmpty())
    })

    LaunchedEffect(importStudentBufferListener) {
        viewModel.getVerifiedStudentsNetwork(currentSchoolIdPref.orEmpty())
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
            when (verifiedStudentsNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedStudentsNetwork.data?.isNotEmpty() == true) {
                        //do your thing
                        LazyColumn(
                            modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                            state = rememberLazyListState(),
                        ) {
                            items(verifiedStudentsNetwork.data!!) { student ->
                                VerifiedStudentItem(
                                    student = student.toLocal(),
                                    onTap = { selectedStudent ->
                                        if (importStudentBuffer.isEmpty()) {
                                            scope.launch {
                                                onStudentImported()
                                                viewModel.onManageClassAdminDetailFeatureChanged(
                                                    ManageClassAdminDetailFeature.ImportStudent
                                                )
                                            }.invokeOnCompletion {
                                                runnableBlock {
                                                    //add to buffer
                                                    viewModel.onAddToImportStudentBuffer(
                                                        selectedStudent.userId
                                                    )
                                                }
                                            }
                                        } else {
                                            //add to buffer
                                            if (importStudentBuffer.contains(selectedStudent.userId)) {
                                                viewModel.onRemoveFromImportStudentBuffer(
                                                    selectedStudent.userId
                                                )
                                            } else {
                                                viewModel.onAddToImportStudentBuffer(selectedStudent.userId)
                                            }
                                            viewModel.onIncImportStudentBufferListener()
                                        }
                                    },
                                    onHold = { selectedStudent ->
                                        //add to buffer
                                        viewModel.onAddToImportStudentBuffer(selectedStudent.userId)
                                        viewModel.onIncImportStudentBufferListener()
                                    },
                                    selected = importStudentBuffer.contains(student.userId)
                                )
                            }
                        }

                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_students),
                            buttonLabel = stringResource(id = R.string.enroll_students),
                            onClick = onEnrollStudent
                        )
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight,
                        message = stringResource(id = R.string.error_occurred_students),
                        buttonLabel = stringResource(id = R.string.go_back),
                        onClick = onBack,
                    )
                }
            }
        }
    }

}