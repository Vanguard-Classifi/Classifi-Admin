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
import com.vanguard.classifiadmin.ui.screens.admin.VerifiedSubjectItem
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch

const val IMPORT_SUBJECT_SCREEN = "import_subject_screen"

@Composable
fun ImportSubjectScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
    onSubjectImported: () -> Unit,
    onAddSubject: () -> Unit,
) {
    val importSubjectBufferListener by viewModel.importSubjectBufferListener.collectAsState()
    val importSubjectBuffer by viewModel.importSubjectBuffer.collectAsState()
    val showDone: MutableState<Boolean> =
        remember(importSubjectBufferListener) { mutableStateOf(importSubjectBuffer.isNotEmpty()) }

    LaunchedEffect(Unit) {
        viewModel.onIncImportSubjectBufferListener()
    }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithDone(
                    onBack = onBack,
                    heading = stringResource(id = R.string.import_subjects),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    showDone = showDone.value,
                    onDone = {
                        // close screen , go back to manage class detail
                        viewModel.onManageClassAdminDetailFeatureChanged(
                            ManageClassAdminDetailFeature.ImportSubject
                        )
                        onSubjectImported()
                    }
                )
            },
            content = {
                ImportSubjectScreenContent(
                    modifier = modifier.padding(it),
                    viewModel = viewModel,
                    onBack = onBack,
                    onSubjectImported = onSubjectImported,
                    onAddSubject = onAddSubject,
                    maxHeight = maxHeight,
                )
            }
        )
    }
}


@Composable
fun ImportSubjectScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSubjectImported: () -> Unit,
    onAddSubject: () -> Unit,
    maxHeight: Dp,
) {
    val verifiedSubjectsNetwork by viewModel.verifiedSubjectsNetwork.collectAsState()
    val importSubjectBuffer by viewModel.importSubjectBuffer.collectAsState()
    val importSubjectBufferListener by viewModel.importSubjectBufferListener.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.clearImportSubjectBuffer()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getVerifiedSubjectsNetwork(currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(importSubjectBufferListener) {
        viewModel.getVerifiedSubjectsNetwork(currentSchoolIdPref.orEmpty())
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

            when (verifiedSubjectsNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedSubjectsNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                            state = rememberLazyListState()
                        ) {
                            items(verifiedSubjectsNetwork.data!!) { subject ->
                                VerifiedSubjectItem(
                                    subject = subject.toLocal(),
                                    selected = importSubjectBuffer.contains(subject.subjectCode),
                                    onHold = { selectedSubject ->
                                        viewModel.onAddToImportSubjectBuffer(selectedSubject.subjectCode.orEmpty())
                                        viewModel.onIncImportSubjectBufferListener()
                                    },
                                    onTap = { selectedSubject ->
                                        if (importSubjectBuffer.isEmpty()) {
                                            scope.launch {
                                                onSubjectImported()
                                                viewModel.onManageClassAdminDetailFeatureChanged(
                                                    ManageClassAdminDetailFeature.ImportSubject
                                                )
                                            }.invokeOnCompletion {
                                                runnableBlock {
                                                    viewModel.onAddToImportSubjectBuffer(
                                                        selectedSubject.subjectCode.orEmpty()
                                                    )
                                                }
                                            }
                                        } else {
                                            //add to buffer
                                            if (importSubjectBuffer.contains(selectedSubject.subjectCode)) {
                                                viewModel.onRemoveFromImportSubjectBuffer(
                                                    selectedSubject.subjectCode.orEmpty()
                                                )
                                            } else {
                                                viewModel.onAddToImportSubjectBuffer(selectedSubject.subjectCode.orEmpty())
                                            }
                                            viewModel.onIncImportSubjectBufferListener()
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_subjects),
                            buttonLabel = stringResource(id = R.string.add_subjects),
                            onClick = onAddSubject
                        )
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight,
                        message = stringResource(id = R.string.error_occurred_subjects),
                        buttonLabel = stringResource(id = R.string.go_back),
                        onClick = onBack,
                    )
                }
            }

        }
    }
}