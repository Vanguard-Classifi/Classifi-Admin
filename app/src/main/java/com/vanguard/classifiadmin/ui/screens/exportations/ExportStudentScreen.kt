package com.vanguard.classifiadmin.ui.screens.exportations

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
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithDone
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminDetailFeature
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch

const val EXPORT_STUDENT_SCREEN = "export_student_screen"

@Composable
fun ExportStudentScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onStudentExported: () -> Unit,
) {
    val TAG = "ExportStudentScreen"
    val exportStudentBuffer by viewModel.exportStudentBuffer.collectAsState()
    val exportStudentBufferListener by viewModel.exportStudentBuffer.collectAsState()
    val showDone: MutableState<Boolean> =
        remember(exportStudentBufferListener) { mutableStateOf(exportStudentBuffer.isNotEmpty()) }

    LaunchedEffect(Unit) {
        viewModel.onIncExportStudentBufferListener()
    }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithDone(
                    onBack = onBack,
                    heading = stringResource(id = R.string.export_students),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    showDone = false,
                    onDone = {
                        // close screen , go back to manage class detail
                        viewModel.onManageClassAdminDetailFeatureChanged(
                            ManageClassAdminDetailFeature.ExportStudent
                        )
                        onStudentExported()
                    }
                )
            },
            content = {
                ExportStudentScreenContent(
                    modifier = modifier.padding(it),
                    viewModel = viewModel,
                    onBack = onBack,
                    maxHeight = maxHeight,
                    onStudentExported = onStudentExported
                )
            }
        )
    }
}


@Composable
fun ExportStudentScreenContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
    maxHeight: Dp,
    onStudentExported: () -> Unit,
) {
    val TAG = "ExportStudentScreenContent"
    // list of all classes
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedClassesNetwork by viewModel.verifiedClassesNetwork.collectAsState()
    val exportStudentBuffer by viewModel.exportStudentBuffer.collectAsState()
    val exportStudentBufferListener by viewModel.exportStudentBuffer.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.clearExportStudentBuffer()
        viewModel.onIncExportStudentBufferListener()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(exportStudentBufferListener) {
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
            when(verifiedClassesNetwork) {
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
                                    onHold = {},
                                    onTap = { selectedClass ->
                                        if (exportStudentBuffer.isEmpty()) {
                                            scope.launch {
                                                onStudentExported()
                                                viewModel.onManageClassAdminDetailFeatureChanged(
                                                    ManageClassAdminDetailFeature.ExportStudent
                                                )
                                            }.invokeOnCompletion {
                                                runnableBlock {
                                                    viewModel.onAddToExportStudentBuffer(
                                                        myClass.classId.orEmpty()
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    selected = exportStudentBuffer.contains(myClass.classId),
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