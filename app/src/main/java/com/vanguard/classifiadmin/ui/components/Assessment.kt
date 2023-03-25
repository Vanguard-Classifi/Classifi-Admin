package com.vanguard.classifiadmin.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.data.local.models.SubjectModel
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.domain.extensions.orStudent
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.UserRole
import com.vanguard.classifiadmin.ui.screens.admin.CreateSubjectClassItem
import com.vanguard.classifiadmin.ui.screens.admin.StagedSubjectItem
import com.vanguard.classifiadmin.ui.screens.admin.VerifiedStudentItem
import com.vanguard.classifiadmin.ui.screens.admin.VerifiedSubjectItem
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentType
import com.vanguard.classifiadmin.ui.screens.dashboard.DashboardBottomSheetFlavor
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateAssessmentBox(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClose: () -> Unit,
    parentWidth: Dp,
) {
    val TAG = "CreateAssessmentBox"
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetMode by viewModel.assessmentBottomSheetMode.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentUserRolePref by viewModel.currentUserRolePref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedSubjectsNetwork by viewModel.verifiedSubjectsNetwork.collectAsState()
    val verifiedSubjectsGivenTeacherNetwork by viewModel.verifiedSubjectsGivenTeacherNetwork.collectAsState()
    val selectedSubjectCreateAssessment by viewModel.selectedSubjectCreateAssessment.collectAsState()
    val verifiedStudentsUnderClassNetwork by viewModel.verifiedStudentsUnderClassNetwork.collectAsState()
    val studentsBufferCreateAssessment by viewModel.studentsBufferCreateAssessment.collectAsState()
    val studentsBufferCreateAssessmentListener by viewModel.studentsBufferCreateAssessmentListener.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentUserRolePref()
        delay(1000)
        //get subjects assigned to teacher
        when (currentUserRolePref) {
            UserRole.Teacher.name -> {
                viewModel.getVerifiedSubjectsGivenTeacherNetwork(
                    currentUserIdPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
            }

            UserRole.Admin.name -> {
                viewModel.getVerifiedSubjectsNetwork(currentSchoolIdPref.orEmpty())
            }

            UserRole.SuperAdmin.name -> {
                viewModel.getVerifiedSubjectsNetwork(currentSchoolIdPref.orEmpty())
            }

            else -> {}
        }
        if(selectedSubjectCreateAssessment != null) {
            viewModel.getVerifiedStudentsUnderClassNetwork(
                selectedSubjectCreateAssessment?.classId.orEmpty(),
                currentSchoolIdPref.orEmpty()
            )
        }
    }

    LaunchedEffect(showModalSheet.value) {
        if(selectedSubjectCreateAssessment != null) {
            viewModel.getVerifiedStudentsUnderClassNetwork(
                selectedSubjectCreateAssessment?.classId.orEmpty(),
                currentSchoolIdPref.orEmpty()
            )
        }
    }

    LaunchedEffect(
        studentsBufferCreateAssessment.size,
        studentsBufferCreateAssessmentListener
    ) {
        Log.e(
            TAG,
            "CreateAssessmentBox: current student buffer size is ${studentsBufferCreateAssessment.size}"
        )
        if(selectedSubjectCreateAssessment != null) {
            viewModel.getVerifiedStudentsUnderClassNetwork(
                selectedSubjectCreateAssessment?.classId.orEmpty(),
                currentSchoolIdPref.orEmpty()
            )
        }
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            val maxHeight = maxHeight

            ModalBottomSheetLayout(
                modifier = Modifier
                    .width(parentWidth)
                    .height(maxHeight),
                sheetState = sheetState,
                scrimColor = MaterialTheme.colors.primary.copy(0.3f),
                sheetElevation = 8.dp,
                sheetBackgroundColor = MaterialTheme.colors.onPrimary,
                sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                sheetContent = {
                    AssessmentBottomSheetContent(
                        subjects = when (currentUserRolePref) {
                            UserRole.Teacher.name -> {
                                if (verifiedSubjectsGivenTeacherNetwork is Resource.Success)
                                    verifiedSubjectsGivenTeacherNetwork.data?.map { it.toLocal() }!!
                                else emptyList()
                            }

                            UserRole.Admin.name -> {
                                if (verifiedSubjectsNetwork is Resource.Success)
                                    verifiedSubjectsNetwork.data?.map { it.toLocal() }!!
                                else emptyList()
                            }

                            UserRole.SuperAdmin.name -> {
                                if (verifiedSubjectsNetwork is Resource.Success)
                                    verifiedSubjectsNetwork.data?.map { it.toLocal() }!!
                                else emptyList()
                            }

                            else -> emptyList()
                        }, //subjects assigned to teacher only
                        students = if(verifiedStudentsUnderClassNetwork is Resource.Success)
                                verifiedStudentsUnderClassNetwork.data?.map { it.toLocal() }!! else emptyList(),
                        viewModel = viewModel,
                        onClose = {
                            coroutineScope.launch {
                                delay(400)
                                showModalSheet.value = false
                                sheetState.hide()
                            }
                        }
                    )
                },
                content = {
                    CreateAssessmentBoxContent(
                        modifier = Modifier,
                        viewModel = viewModel,
                        onClose = onClose,
                        onSelectSubjectForAssessment = {
                            viewModel.onAssessmentBottomSheetModeChanged(
                                AssessmentBottomSheetMode.Subjects
                            )
                            coroutineScope.launch {
                                showModalSheet.value = true
                                sheetState.show()
                            }
                        },
                        onSelectEndPeriod = {
                            viewModel.onAssessmentBottomSheetModeChanged(
                                AssessmentBottomSheetMode.EndPeriod
                            )
                            coroutineScope.launch {
                                showModalSheet.value = true
                                sheetState.show()
                            }
                        },
                        onSelectStartPeriod = {
                            viewModel.onAssessmentBottomSheetModeChanged(
                                AssessmentBottomSheetMode.StartPeriod
                            )
                            coroutineScope.launch {
                                showModalSheet.value = true
                                sheetState.show()
                            }
                        },
                        onSelectStudentForAssessment = {
                            viewModel.onAssessmentBottomSheetModeChanged(
                                AssessmentBottomSheetMode.Students
                            )
                            coroutineScope.launch {
                                showModalSheet.value = true
                                sheetState.show()
                            }
                        },
                    )
                }
            )
        }
    }
}


@Composable
fun CreateAssessmentBoxContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClose: () -> Unit,
    onSelectSubjectForAssessment: () -> Unit,
    onSelectStudentForAssessment: () -> Unit,
    onSelectStartPeriod: () -> Unit,
    onSelectEndPeriod: () -> Unit,
) {
    val innerModifier = Modifier
    val currentUserRolePref by viewModel.currentUserRolePref.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val constraints = CreateAssessmentBoxConstraints(8.dp)
    val assessmentNameCreateAssessment by viewModel.assessmentNameCreateAssessment.collectAsState()
    val selectedSubjectCreateAssessment by viewModel.selectedSubjectCreateAssessment.collectAsState()
    val studentsBufferCreateAssessment by viewModel.studentsBufferCreateAssessment.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUserRolePref()
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentSchoolIdPref()
        delay(1000)
        when (currentUserRolePref) {
            UserRole.Teacher.name -> {
                //get subjects assigned to teacher

                //get classes assigned to teacher
                viewModel.getVerifiedClassesGivenTeacherNetwork(
                    currentUserIdPref.orEmpty(),
                    currentSchoolIdPref.orEmpty(),
                )
                //get students assigned to teacher
            }

            UserRole.SuperAdmin.name -> {
                //get all subjects
                viewModel.getVerifiedSubjectsNetwork(currentSchoolIdPref.orEmpty())
                //get all classes
                viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
                //get all students
                viewModel.getVerifiedStudentsNetwork(currentSchoolIdPref.orEmpty())
            }

            UserRole.Admin.name -> {
                //get all subjects
                viewModel.getVerifiedSubjectsNetwork(currentSchoolIdPref.orEmpty())
                //get all classes
                viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
                //get all students
                viewModel.getVerifiedStudentsNetwork(currentSchoolIdPref.orEmpty())
            }

            else -> {}
        }

    }

    Card(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 32.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
            val maxHeight = maxHeight
            val maxWidth = maxWidth

            ConstraintLayout(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                constraintSet = constraints,
            ) {
                Text(
                    text = stringResource(id = R.string.create_assessment).uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                    modifier = innerModifier.layoutId("heading")
                )

                RoundedIconButton(
                    onClick = {
                        /*  todo: on abort create question */
                        onClose()
                    },
                    icon = R.drawable.icon_close,
                    modifier = innerModifier.layoutId("close")
                )

                AssessmentTypeSelector(
                    viewModel = viewModel,
                    modifier = innerModifier.layoutId("selectionRow")
                )

                OutlinedTextField(
                    modifier = innerModifier.layoutId("nameField"),
                    value = assessmentNameCreateAssessment.orEmpty(),
                    onValueChange = viewModel::onAssessmentNameCreateAssessmentChanged,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.give_assessment_a_name),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Black100.copy(0.5f),
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
                    ),
                    isError = false
                )


                OutlinedTextField(
                    enabled = false,
                    readOnly = true,
                    modifier = innerModifier
                        .layoutId("subjectField")
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectSubjectForAssessment() },
                    value = selectedSubjectCreateAssessment?.subjectCode.orEmpty(),
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.select_a_subject),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Black100.copy(0.5f),
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_down),
                            contentDescription = stringResource(id = R.string.arrow_dropdown),
                            tint = Black100.copy(0.5f)
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
                    ),
                    isError = false,
                )


                OutlinedTextField(
                    enabled = false,
                    readOnly = true,
                    modifier = innerModifier
                        .layoutId("startPeriodField")
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectStartPeriod() },
                    value = "",
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(id = R.string.start_period),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Black100.copy(0.5f),
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_down),
                            contentDescription = stringResource(id = R.string.arrow_dropdown),
                            tint = Black100.copy(0.5f)
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
                    ),
                    isError = false,
                )


                OutlinedTextField(
                    enabled = false,
                    readOnly = true,
                    modifier = innerModifier
                        .layoutId("endPeriodField")
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectEndPeriod() },
                    value = "",
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(id = R.string.end_period),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Black100.copy(0.5f),
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_down),
                            contentDescription = stringResource(id = R.string.arrow_dropdown),
                            tint = Black100.copy(0.5f)
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
                    ),
                    isError = false,
                )

                AssignedStudentSectionAssessment(
                    modifier = innerModifier.layoutId("studentField"),
                    students = studentsBufferCreateAssessment,
                    onRemoveStudent = {
                        viewModel.onRemoveStudentFromAssessment(it)
                        viewModel.onDecStudentsBufferCreateAssessmentListener()
                    },
                    onAssignStudent = { onSelectStudentForAssessment() }
                )


                PrimaryTextButton(
                    label = stringResource(id = R.string.continue_text),
                    onClick = { /*TODO: on continue */ },
                    modifier = innerModifier.layoutId("continueButton")
                )
            }
        }
    }
}


private fun CreateAssessmentBoxConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val heading = createRefFor("heading")
        val close = createRefFor("close")
        val selectionRow = createRefFor("selectionRow")
        val nameField = createRefFor("nameField")
        val subjectField = createRefFor("subjectField")
        val startPeriodField = createRefFor("startPeriodField")
        val endPeriodField = createRefFor("endPeriodField")
        val studentField = createRefFor("studentField")
        val continueButton = createRefFor("continueButton")

        constrain(heading) {
            top.linkTo(close.top, 0.dp)
            bottom.linkTo(close.bottom, 0.dp)
            start.linkTo(parent.start, 8.dp)
        }
        constrain(close) {
            top.linkTo(parent.top, margin)
            end.linkTo(parent.end, margin)
        }
        constrain(selectionRow) {
            top.linkTo(heading.bottom, 16.dp)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
        }
        constrain(nameField) {
            top.linkTo(selectionRow.bottom, 16.dp)
            start.linkTo(parent.start, 8.dp)
            end.linkTo(parent.end, 8.dp)
            width = Dimension.fillToConstraints
        }
        constrain(subjectField) {
            top.linkTo(nameField.bottom, 12.dp)
            start.linkTo(nameField.start, 0.dp)
            end.linkTo(nameField.end, 0.dp)
            width = Dimension.fillToConstraints
        }
        constrain(startPeriodField) {
            top.linkTo(subjectField.bottom, 8.dp)
            start.linkTo(subjectField.start, 0.dp)
            end.linkTo(subjectField.end, 0.dp)
            width = Dimension.fillToConstraints
        }
        constrain(endPeriodField) {
            top.linkTo(startPeriodField.bottom, 8.dp)
            start.linkTo(startPeriodField.start, 0.dp)
            end.linkTo(startPeriodField.end, 0.dp)
            width = Dimension.fillToConstraints
        }
        constrain(studentField) {
            top.linkTo(endPeriodField.bottom, 8.dp)
            start.linkTo(endPeriodField.start, 0.dp)
            end.linkTo(endPeriodField.end, 0.dp)
            width = Dimension.fillToConstraints
        }
        constrain(continueButton) {
            top.linkTo(studentField.bottom, 32.dp)
            bottom.linkTo(parent.bottom, margin)
            end.linkTo(parent.end, margin)
        }
    }
}

@Composable
fun AssignedClassSectionAssessment(
    modifier: Modifier = Modifier,
    myClasses: List<ClassModel>,
    onRemoveClass: (ClassModel) -> Unit,
    onAssignClass: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            enabled = false,
            readOnly = true,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { onAssignClass() },
            value = "",
            onValueChange = {},
            label = {
                Text(
                    text = stringResource(id = R.string.assigned_class),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100.copy(0.5f),
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_arrow_down),
                    contentDescription = stringResource(id = R.string.arrow_dropdown),
                    tint = Black100.copy(0.5f)
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
            ),
            isError = false,
        )

        Spacer(modifier = modifier.height(8.dp))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            myClasses.forEach { myClass ->
                SecondaryButtonWithIconRight(
                    label = myClass.classCode.orEmpty(),
                    icon = R.drawable.icon_close,
                    onClick = { onRemoveClass(myClass) },
                )
            }
        }
    }
}

@Composable
fun AssignedStudentSectionAssessment(
    modifier: Modifier = Modifier,
    students: List<UserModel>,
    onRemoveStudent: (UserModel) -> Unit,
    onAssignStudent: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            enabled = false,
            readOnly = true,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { onAssignStudent() },
            value = "",
            onValueChange = {},
            label = {
                Text(
                    text = stringResource(id = R.string.assigned_student),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100.copy(0.5f),
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_arrow_down),
                    contentDescription = stringResource(id = R.string.arrow_dropdown),
                    tint = Black100.copy(0.5f)
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
            ),
            isError = false,
        )

        Spacer(modifier = modifier.height(16.dp))

        Row(
            modifier = modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Start,
        ) {
            students.forEach { student ->
                SecondaryButtonWithIconRight(
                    label = student.fullname.orStudent(),
                    icon = R.drawable.icon_close,
                    onClick = { onRemoveStudent(student) },
                )
                Spacer(modifier = modifier.width(8.dp))
            }
        }
    }
}


@Composable
fun AssessmentTypeSelector(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    types: List<AssessmentType> = AssessmentType.values().toList(),
) {
    val currentAssessmentType by viewModel.currentAssessmentType.collectAsState()

    Surface(
        modifier = modifier.clip(RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = Black100.copy(0.1f)
        )
    ) {
        Row(
            modifier = modifier
                .wrapContentWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            types.forEach { each ->
                AssessmentTypeButton(
                    onSelect = viewModel::onCurrentAssessmentTypeChanged,
                    selected = each == currentAssessmentType,
                    type = each,
                )
            }
        }
    }
}


@Composable
fun AssessmentTypeButton(
    modifier: Modifier = Modifier,
    onSelect: (AssessmentType) -> Unit,
    type: AssessmentType,
    selected: Boolean = false,
) {
    TextButton(
        onClick = { onSelect(type) },
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .clip(RoundedCornerShape(28.dp)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (selected) MaterialTheme.colors.primary.copy(0.3f) else Color.Transparent,
            contentColor = if (selected) MaterialTheme.colors.primary else Black100.copy(0.3f),
            disabledBackgroundColor = Black100.copy(0.1f)
        )
    ) {
        Text(
            text = type.title,
            fontSize = 12.sp,
            color = if (selected) MaterialTheme.colors.primary else Black100.copy(0.3f),
        )
    }
}

@Composable
fun AssessmentBottomSheetContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    subjects: List<SubjectModel>,
    students: List<UserModel>,
    onClose: () -> Unit,
) {
    val mode by viewModel.assessmentBottomSheetMode.collectAsState()
    val studentsBufferCreateAssessment by viewModel.studentsBufferCreateAssessment.collectAsState()

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

        when (mode) {
            is AssessmentBottomSheetMode.Subjects -> {
                if (subjects.isEmpty()) {
                    //no items screen
                    NoDataInline(message = stringResource(id = R.string.subjects_not_available))
                } else {
                    LazyColumn(
                        modifier = modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                        state = rememberLazyListState()
                    ) {
                        items(subjects) { each ->
                            VerifiedSubjectItem(
                                subject = each,
                                selected = false,
                                onTap = {
                                    //on select subject
                                    viewModel.onSelectedSubjectCreateAssessmentChanged(it)
                                    onClose()
                                },
                                onHold = {}
                            )
                        }
                    }
                }
            }

            is AssessmentBottomSheetMode.Students -> {
                if (subjects.isEmpty()) {
                    //no items screen
                    NoDataInline(message = stringResource(id = R.string.students_not_available))
                } else {
                    LazyColumn(
                        modifier = modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                        state = rememberLazyListState()
                    ) {
                        items(students) { each ->
                            VerifiedStudentItem(
                                student = each,
                                selected = studentsBufferCreateAssessment.contains(each),
                                onTap = {
                                    //add to student list
                                    if(studentsBufferCreateAssessment.isEmpty()) {
                                        viewModel.onAddStudentToAssessment(it)
                                        onClose()
                                    } else {
                                        if(!studentsBufferCreateAssessment.contains(it)) {
                                            viewModel.onAddStudentToAssessment(it)
                                        } else {
                                            viewModel.onRemoveStudentFromAssessment(it)
                                        }
                                    }
                                    viewModel.onIncStudentsBufferCreateAssessmentListener()
                                },
                                onHold = {
                                    viewModel.onAddStudentToAssessment(it)
                                    viewModel.onIncStudentsBufferCreateAssessmentListener()
                                }
                            )
                        }

                        item {
                            PrimaryTextButtonFillWidth(
                                label = stringResource(id = R.string.done),
                                onClick = onClose
                            )
                        }
                    }
                }
            }

            is AssessmentBottomSheetMode.StartPeriod -> {
                /*todo : date and time widgets */
            }

            is AssessmentBottomSheetMode.EndPeriod -> {
                /*todo : date and time widgets */
            }
        }
    }
}


sealed class AssessmentBottomSheetMode {
    object Subjects : AssessmentBottomSheetMode()
    object Students : AssessmentBottomSheetMode()
    object StartPeriod : AssessmentBottomSheetMode()
    object EndPeriod : AssessmentBottomSheetMode()
}