package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.vanguard.classifiadmin.data.local.models.SubjectModel
import com.vanguard.classifiadmin.ui.screens.admin.CreateSubjectClassItem
import com.vanguard.classifiadmin.ui.screens.admin.StagedSubjectItem
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentType
import com.vanguard.classifiadmin.ui.screens.dashboard.DashboardBottomSheetFlavor
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateAssessmentBox(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClose: () -> Unit,
    parentWidth: Dp,
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier) {
            val maxHeight = maxHeight

            ModalBottomSheetLayout(
                modifier = modifier
                    .width(parentWidth)
                    .height(maxHeight),
                sheetState = sheetState,
                scrimColor = MaterialTheme.colors.primary.copy(0.3f),
                sheetElevation = 8.dp,
                sheetBackgroundColor = MaterialTheme.colors.onPrimary,
                sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                sheetContent = {
                    AssessmentBottomSheetContent(
                        subjects = emptyList(), //subjects assigned to teacher only
                    )
                },
                content = {
                    CreateAssessmentBoxContent(
                        modifier = modifier,
                        viewModel = viewModel,
                        onClose = onClose,
                        onSelectSubjectForAssessment = {
                            coroutineScope.launch {
                                showModalSheet.value = true
                                sheetState.show()
                            }
                        }
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
) {
    val innerModifier = Modifier
    val constraints = CreateAssessmentBoxConstraints(8.dp)
    val assessmentNameCreateAssessment by viewModel.assessmentNameCreateAssessment.collectAsState()

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
                    value = "",
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
                        .clickable { },
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
                        .clickable { },
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
        val classField = createRefFor("classField")
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
        constrain(classField) {
            top.linkTo(endPeriodField.bottom, 8.dp)
            start.linkTo(endPeriodField.start, 0.dp)
            end.linkTo(endPeriodField.end, 0.dp)
            width = Dimension.fillToConstraints
        }
        constrain(studentField) {
            top.linkTo(classField.bottom, 8.dp)
            start.linkTo(classField.start, 0.dp)
            end.linkTo(classField.end, 0.dp)
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
    subjects: List<SubjectModel>,
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


        if (subjects.isEmpty()) {
            //no items screen
            NoDataInline(message = stringResource(id = R.string.subjects_not_available))
        } else {
            LazyColumn(
                modifier = modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                state = rememberLazyListState()
            ) {
                items(subjects) { each ->
                    StagedSubjectItem(
                        subject = each,
                        onRemove = {}
                    )
                }
            }
        }

    }
}


