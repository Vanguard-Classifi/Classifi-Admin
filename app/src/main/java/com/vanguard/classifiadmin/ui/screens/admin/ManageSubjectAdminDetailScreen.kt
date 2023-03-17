package com.vanguard.classifiadmin.ui.screens.admin

import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.data.local.models.SubjectModel
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.helpers.todayComputational
import com.vanguard.classifiadmin.domain.services.SubjectCreationService
import com.vanguard.classifiadmin.domain.services.SubjectCreationServiceActions
import com.vanguard.classifiadmin.domain.services.SubjectCreationServiceExtras
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.NoDataInline
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val MANAGE_SUBJECT_ADMIN_DETAIL_SCREEN = "manage_subject_admin_detail_screen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManageSubjectAdminDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedClassesNetwork by viewModel.verifiedClassesNetwork.collectAsState()
    val selectedSubjectManageSubjectAdmin by viewModel.selectedSubjectManageSubjectAdmin.collectAsState()
    val message by viewModel.manageSubjectAdminDetailMessage.collectAsState()


    LaunchedEffect(Unit) {
        delay(1000)
        viewModel.getCurrentSchoolIdPref()
        delay(2000)
        viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
        viewModel.onManageSubjectAdminDetailMessageChanged(
            ManageSubjectAdminDetailMessage.NoMessage
        )
    }


    LaunchedEffect(message) {
        if (message !is ManageSubjectAdminDetailMessage.NoMessage) {
            delay(3000)
            viewModel.onManageSubjectAdminDetailMessageChanged(
                ManageSubjectAdminDetailMessage.NoMessage
            )
        }
    }


    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier) {
            val maxWidth = maxWidth
            val maxHeight = maxHeight

            ModalBottomSheetLayout(
                modifier = modifier
                    .width(maxWidth)
                    .height(maxHeight),
                sheetState = sheetState,
                scrimColor = MaterialTheme.colors.primary.copy(0.3f),
                sheetElevation = 8.dp,
                sheetBackgroundColor = MaterialTheme.colors.onPrimary,
                sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                sheetContent = {
                    ManageSubjectAdminDetailBottomSheetContent(
                        myClasses = verifiedClassesNetwork.data?.map { it.toLocal() }
                            ?: emptyList<ClassModel>(),
                        onSelectClass = {
                            //on select class
                            viewModel.onSelectedClassManageSubjectAdminDetailChanged(it)
                            coroutineScope.launch {
                                delay(400)
                                sheetState.hide()
                                showModalSheet.value = false
                            }
                        }
                    )
                },
                content = {
                    Scaffold(
                        modifier = modifier,
                        topBar = {
                            ChildTopBar(
                                onBack = onBack,
                                elevation = 0.dp,
                                heading = selectedSubjectManageSubjectAdmin?.subjectName.orEmpty(),
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary,
                            )
                        },
                        content = {
                            ManageSubjectAdminDetailScreenContent(
                                modifier = modifier.padding(it),
                                viewModel = viewModel,
                                onBack = onBack,
                                onSelectSubjectClass = {
                                    //show bottom sheet
                                    coroutineScope.launch {
                                        sheetState.show()
                                        showModalSheet.value = true
                                    }
                                }
                            )
                        }
                    )
                }
            )

            //message
            if (message !is ManageSubjectAdminDetailMessage.NoMessage) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    MessageBar(
                        message = message.message,
                        icon = R.drawable.icon_info,
                        onClose = {
                            viewModel.onManageSubjectAdminDetailMessageChanged(
                                ManageSubjectAdminDetailMessage.NoMessage
                            )
                        },
                        maxWidth = maxWidth,
                        modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ManageSubjectAdminDetailScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSelectSubjectClass: () -> Unit,
) {

    val TAG = "ManageSubjectAdminDetailScreenContent"
    val selectedSubjectManageSubjectAdmin by viewModel.selectedSubjectManageSubjectAdmin.collectAsState()
    val selectedClassManageSubjectAdminDetail by viewModel.selectedClassManageSubjectAdminDetail.collectAsState()
    val verticalScroll = rememberScrollState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val subjectNameManageSubjectAdminDetail by viewModel.subjectNameManageSubjectAdminDetail.collectAsState()
    val subjectCodeManageSubjectAdminDetail by viewModel.subjectCodeManageSubjectAdminDetail.collectAsState()
    val classByIdNetwork by viewModel.classByIdNetwork.collectAsState()
    val exception: MutableState<ManageSubjectAdminDetailException> =
        remember { mutableStateOf(ManageSubjectAdminDetailException.NoException) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val subjectByCodeNetwork by viewModel.subjectByCodeNetwork.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.getCurrentSchoolIdPref()
        viewModel.getClassByIdNetwork(
            selectedSubjectManageSubjectAdmin?.classId.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
        viewModel.onSubjectNameManageSubjectAdminDetailChanged(
            selectedSubjectManageSubjectAdmin?.subjectName.orEmpty()
        )
        viewModel.onSubjectCodeManageSubjectAdminDetailChanged(
            selectedSubjectManageSubjectAdmin?.subjectCode.orEmpty()
        )

        viewModel.onSelectedClassManageSubjectAdminDetailChanged(
            classByIdNetwork.data?.toLocal()
        )
    }

    LaunchedEffect(subjectCodeManageSubjectAdminDetail) {
        if (subjectCodeManageSubjectAdminDetail != null) {
            viewModel.getSubjectByCodeNetwork(
                subjectCodeManageSubjectAdminDetail.orEmpty(),
                currentSchoolIdPref.orEmpty()
            )
        }
    }

    Log.e(
        TAG,
        "ManageSubjectAdminDetailScreenContent: subject name $subjectNameManageSubjectAdminDetail"
    )
    Log.e(
        TAG,
        "ManageSubjectAdminDetailScreenContent: subject name $subjectCodeManageSubjectAdminDetail"
    )

    Column(modifier = Modifier.verticalScroll(verticalScroll)) {
        Card(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 8.dp, bottom = 64.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 2.dp,
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = subjectNameManageSubjectAdminDetail.orEmpty(),
                    onValueChange = viewModel::onSubjectNameManageSubjectAdminDetailChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.subject_name),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    isError = exception.value is ManageSubjectAdminDetailException.SubjectName
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = subjectCodeManageSubjectAdminDetail.orEmpty(),
                    onValueChange = viewModel::onSubjectCodeManageSubjectAdminDetailChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.subject_code),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Characters,
                    ),
                    isError = exception.value is ManageSubjectAdminDetailException.SubjectCode
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    enabled = false,
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectSubjectClass() },
                    value = selectedClassManageSubjectAdminDetail?.classCode.orEmpty(),
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(id = R.string.class_code),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_down),
                            contentDescription = stringResource(id = R.string.arrow_dropdown),
                            tint = Black100
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Characters,
                    ),
                    isError = exception.value is ManageSubjectAdminDetailException.SubjectClass
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    //save button
                    PrimaryTextButtonFillWidth(
                        modifier = Modifier.padding(
                            top = 8.dp, bottom = 8.dp, start = 8.dp
                        ),
                        label = stringResource(id = R.string.save_and_exit).uppercase(),
                        onClick = {
                            keyboardController?.hide()

                            if (subjectNameManageSubjectAdminDetail == null || subjectNameManageSubjectAdminDetail!!.isBlank()
                            ) {
                                //subject name cannot be blank
                                viewModel.onManageSubjectAdminDetailMessageChanged(
                                    ManageSubjectAdminDetailMessage.SubjectNameCannotBeBlank
                                )
                                return@PrimaryTextButtonFillWidth
                            }

                            if (subjectCodeManageSubjectAdminDetail == null
                                || subjectCodeManageSubjectAdminDetail!!.isBlank()
                            ) {
                                //subject code can't be blank
                                viewModel.onManageSubjectAdminDetailMessageChanged(
                                    ManageSubjectAdminDetailMessage.SubjectCodeCannotBeBlank
                                )
                                return@PrimaryTextButtonFillWidth
                            }

                            if ((subjectNameManageSubjectAdminDetail?.isNotBlank() == true &&
                                        subjectCodeManageSubjectAdminDetail?.isNotBlank() == true)
                            ) {
                                scope.launch {
                                    //save updated subject
                                    selectedSubjectManageSubjectAdmin?.subjectName =
                                        subjectNameManageSubjectAdminDetail
                                    selectedSubjectManageSubjectAdmin?.subjectCode =
                                        subjectCodeManageSubjectAdminDetail
                                    selectedSubjectManageSubjectAdmin?.classId =
                                        selectedClassManageSubjectAdminDetail?.classId
                                    selectedSubjectManageSubjectAdmin?.className =
                                        selectedClassManageSubjectAdminDetail?.className.orEmpty()

                                    Log.e(
                                        TAG,
                                        "ManageSubjectAdminDetailScreenContent: subject new name is ${selectedSubjectManageSubjectAdmin?.subjectName}"
                                    )

                                    Log.e(
                                        TAG,
                                        "ManageSubjectAdminDetailScreenContent: subject new code is ${selectedSubjectManageSubjectAdmin?.subjectCode}"
                                    )

                                    viewModel.saveSubjectAsVerifiedNetwork(
                                        selectedSubjectManageSubjectAdmin?.toNetwork()!!
                                    ) {
                                        Log.e(
                                            TAG,
                                            "ManageSubjectAdminDetailScreenContent: subject save state is $it"
                                        )
                                    }
                                }.invokeOnCompletion {
                                    runnableBlock {
                                        viewModel.onManageSubjectAdminDetailMessageChanged(
                                            ManageSubjectAdminDetailMessage.UpdatedSubject
                                        )
                                        //close screen
                                        onBack()
                                    }
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}


@Composable
fun ManageSubjectAdminDetailBottomSheetContent(
    modifier: Modifier = Modifier,
    myClasses: List<ClassModel>,
    onSelectClass: (ClassModel) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                color = Black100.copy(0.5f)
            ) {
                Box(
                    modifier = modifier
                        .width(102.dp)
                        .height(3.dp)
                )
            }
        }


        if (myClasses.isEmpty()) {
            //no items screen
            NoDataInline(message = stringResource(id = R.string.classes_not_available))
        } else {
            LazyColumn(
                modifier = modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                state = rememberLazyListState()
            ) {
                items(myClasses) { each ->
                    CreateSubjectClassItem(
                        myClass = each,
                        onSelect = onSelectClass,
                    )
                }
            }
        }

    }
}


sealed class ManageSubjectAdminDetailException(val message: String) {
    class SubjectName : ManageSubjectAdminDetailException("Please enter the subject name")
    class SubjectCode : ManageSubjectAdminDetailException("Please enter the subject code")
    class SubjectClass : ManageSubjectAdminDetailException("Please enter the subject class")
    object NoException : ManageSubjectAdminDetailException("")

}

sealed class ManageSubjectAdminDetailMessage(val message: String) {
    object SubjectAlreadyExist : ManageSubjectAdminDetailMessage("Subject already exists!")
    object UpdatedSubject : ManageSubjectAdminDetailMessage("Successfully updated subject!")
    object SubjectNameCannotBeBlank :
        ManageSubjectAdminDetailMessage("Subject name cannot be blank")

    object SubjectCodeCannotBeBlank :
        ManageSubjectAdminDetailMessage("Subject code cannot be blank")

    object NoMessage : ManageSubjectAdminDetailMessage("")
}