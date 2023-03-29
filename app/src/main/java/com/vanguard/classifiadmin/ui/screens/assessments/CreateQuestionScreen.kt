package com.vanguard.classifiadmin.ui.screens.assessments

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.QuestionModel
import com.vanguard.classifiadmin.domain.extensions.orZeroString
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithCloseButtonOnly
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val CREATE_QUESTION_SCREEN = "create_question_screen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateQuestionScreen(
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
                    CreateQuestionScreenBottomSheetContent(
                        viewModel = viewModel,
                        onSelectQuestionType = {

                        },
                        onSelectQuestionDifficulty = {

                        },
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
                    Scaffold(
                        modifier = modifier,
                        topBar = {
                            ChildTopBarWithCloseButtonOnly(
                                onClose = onBack,
                            )
                        },
                        content = {
                            CreateQuestionScreenContent(
                                modifier = modifier.padding(it),
                                onConfigQuestionType = {
                                    viewModel.onCreateQuestionBottomSheetModeChanged(
                                        CreateQuestionBottomSheetMode.QuestionType
                                    )
                                    coroutineScope.launch {
                                        showModalSheet.value = true
                                        sheetState.show()
                                    }
                                },
                                onConfigQuestionDifficulty = {
                                    viewModel.onCreateQuestionBottomSheetModeChanged(
                                        CreateQuestionBottomSheetMode.Difficulty
                                    )
                                    coroutineScope.launch {
                                        showModalSheet.value = true
                                        sheetState.show()
                                    }
                                },
                                onConfigQuestionScore = {
                                    viewModel.onCreateQuestionBottomSheetModeChanged(
                                        CreateQuestionBottomSheetMode.Score
                                    )
                                    coroutineScope.launch {
                                        showModalSheet.value = true
                                        sheetState.show()
                                    }
                                },
                                onConfigAssessmentDuration = {
                                    viewModel.onCreateQuestionBottomSheetModeChanged(
                                        CreateQuestionBottomSheetMode.Duration
                                    )
                                    coroutineScope.launch {
                                        showModalSheet.value = true
                                        sheetState.show()
                                    }
                                },
                                viewModel = viewModel,
                            )
                        },
                        bottomBar = {
                            CreateQuestionBottomBar(viewModel = viewModel)
                        }
                    )
                }
            )
        }
    }
}


@Composable
fun CreateQuestionScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onConfigQuestionType: () -> Unit,
    onConfigQuestionDifficulty: () -> Unit,
    onConfigAssessmentDuration: () -> Unit,
    onConfigQuestionScore: () -> Unit,
    questionOptions: List<QuestionOption> = QuestionOption.values().toList(),
    questionOptionsTrueFalse: List<QuestionOptionTrueFalse> = QuestionOptionTrueFalse.values()
        .toList(),
) {
    val verticalScroll = rememberScrollState()
    val correctQuestionOption by viewModel.correctQuestionOption.collectAsState()
    val questionBodyCreateQuestion by viewModel.questionBodyCreateQuestion.collectAsState()
    val questionOptionACreateQuestion by viewModel.questionOptionACreateQuestion.collectAsState()
    val questionOptionBCreateQuestion by viewModel.questionOptionBCreateQuestion.collectAsState()
    val questionOptionCCreateQuestion by viewModel.questionOptionCCreateQuestion.collectAsState()
    val questionOptionDCreateQuestion by viewModel.questionOptionDCreateQuestion.collectAsState()
    val questionType by viewModel.questionTypeCreateQuestion.collectAsState()
    val questionDifficulty by viewModel.questionDifficultyCreateQuestion.collectAsState()
    val questionScoreCreateQuestion by viewModel.questionScoreCreateQuestion.collectAsState()
    val questionDurationCreateQuestion by viewModel.questionDurationCreateQuestion.collectAsState()

    val localModifier = Modifier
    val stagedAssessmentsNetwork by viewModel.stagedAssessmentsNetwork.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val questionAnswersCreateQuestion by viewModel.questionAnswersCreateQuestion.collectAsState()
    val rowWidthTrueFalse = remember { mutableStateOf(0) }
    val correctAnswerTrueFalse by viewModel.correctAnswerTrueFalse.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentSchoolIdPref()
        delay(1000)
        //find the staged assessment
        viewModel.getStagedAssessmentsNetwork(
            currentUserIdPref.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
    }

    BoxWithConstraints(modifier = Modifier) {
        Column(
            modifier = localModifier
                .verticalScroll(verticalScroll)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = stringResource(id = R.string.create_questions),
                color = Black100,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = stringResource(id = R.string.feel_free_to_create_questions),
                color = Black100,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )

            Spacer(modifier = localModifier.height(16.dp))

            Row(
                modifier = localModifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                QuestionConfigBox(
                    label = stringResource(id = R.string.type),
                    onConfig = { onConfigQuestionType() },
                    value = questionType.title,
                )
            }

            Spacer(modifier = localModifier.height(2.dp))
            Row(
                modifier = localModifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuestionConfigBox(
                    label = stringResource(id = R.string.difficulty),
                    onConfig = {
                        onConfigQuestionDifficulty()
                    },
                    value = questionDifficulty.name,
                )

                QuestionConfigBox(
                    label = stringResource(id = R.string.score),
                    onConfig = {
                        onConfigQuestionScore()
                    },
                    value = questionScoreCreateQuestion.orZeroString(),
                )
            }
            Spacer(modifier = localModifier.height(2.dp))

            Row(
                modifier = localModifier
                    .fillMaxWidth()
                    .padding(0.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                QuestionConfigBox(
                    label = stringResource(id = R.string.duration),
                    onConfig = {
                        onConfigAssessmentDuration()
                    },
                    value = questionDurationCreateQuestion.orZeroString(),
                )
            }

            Spacer(modifier = localModifier.height(8.dp))

            Card(
                modifier = localModifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 8.dp, bottom = 8.dp, start = 0.dp, end = 0.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 2.dp,
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    QuestionBox(
                        isOption = false,
                        selected = false,
                        value = questionBodyCreateQuestion.orEmpty(),
                        onValueChange = viewModel::onQuestionBodyCreateQuestionChanged,
                        onMark = {},
                        label = stringResource(id = R.string.question_body)
                    )
                    Spacer(modifier = modifier.height(8.dp))

                    //based on question type
                    when (questionType) {
                        QuestionType.MultiChoice -> {
                            questionOptions.forEach { option ->
                                QuestionBox(
                                    isOption = true,
                                    selected = correctQuestionOption == option,
                                    value = when (option) {
                                        QuestionOption.OptionA -> questionOptionACreateQuestion.orEmpty()
                                        QuestionOption.OptionB -> questionOptionBCreateQuestion.orEmpty()
                                        QuestionOption.OptionC -> questionOptionCCreateQuestion.orEmpty()
                                        QuestionOption.OptionD -> questionOptionDCreateQuestion.orEmpty()
                                    },
                                    onValueChange = {
                                        when (option) {
                                            QuestionOption.OptionA -> {
                                                viewModel.onQuestionOptionACreateQuestionChanged(it)
                                            }

                                            QuestionOption.OptionB -> {
                                                viewModel.onQuestionOptionBCreateQuestionChanged(it)
                                            }

                                            QuestionOption.OptionC -> {
                                                viewModel.onQuestionOptionCCreateQuestionChanged(it)
                                            }

                                            QuestionOption.OptionD -> {
                                                viewModel.onQuestionOptionDCreateQuestionChanged(it)
                                            }
                                        }
                                    },
                                    onMark = {
                                        viewModel.onCorrectQuestionOptionChanged(option)
                                    },
                                    label = option.title
                                )

                                Spacer(modifier = modifier.height(8.dp))
                            }
                        }

                        QuestionType.Essay -> {

                        }

                        QuestionType.Short -> {

                        }

                        QuestionType.TrueFalse -> {
                            Row(modifier = localModifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .onGloballyPositioned {
                                    rowWidthTrueFalse.value = it.size.width
                                }) {
                                questionOptionsTrueFalse.forEach { option ->
                                    QuestionBoxTrueFalse(
                                        option = option,
                                        onSelect = viewModel::onCorrectAnswerTrueFalseChanged,
                                        rowWidth = with(LocalDensity.current) {
                                            rowWidthTrueFalse.value.toDp()
                                        },
                                        isLeft = option == QuestionOptionTrueFalse.False,
                                        selected = correctAnswerTrueFalse == option,
                                    )
                                }
                            }
                            Spacer(modifier = modifier.height(8.dp))
                        }
                    }

                    //save button
                    PrimaryTextButtonFillWidth(
                        label = stringResource(id = R.string.save_changes),
                        onClick = {
                            val answers = ArrayList<String>()
                            questionAnswersCreateQuestion.map { answers.add(it) }

                            val question = QuestionModel(
                                questionId = UUID.randomUUID().toString(),
                                parentAssessmentId =
                                if (stagedAssessmentsNetwork is Resource.Success &&
                                    stagedAssessmentsNetwork.data?.isNotEmpty() == true
                                ) {
                                    stagedAssessmentsNetwork.data?.first()?.assessmentId.orEmpty()
                                } else "",
                                type = questionType.title,
                                difficulty = questionDifficulty.name,
                                maxScore = questionScoreCreateQuestion.orZeroString().toInt(),
                                text = questionBodyCreateQuestion.orEmpty(),
                                optionA = questionOptionACreateQuestion.orEmpty(),
                                optionB = questionOptionBCreateQuestion.orEmpty(),
                                optionC = questionOptionCCreateQuestion.orEmpty(),
                                optionD = questionOptionDCreateQuestion.orEmpty(),
                                answers = answers,
                            )

                            //todo: on save question
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionBoxTrueFalse(
    modifier: Modifier = Modifier,
    option: QuestionOptionTrueFalse,
    onSelect: (QuestionOptionTrueFalse) -> Unit,
    isLeft: Boolean = false,
    selected: Boolean = false,
    rowWidth: Dp,
) {
    Surface(
        modifier = modifier
            .padding(0.dp)
            .width(rowWidth.times(0.45f)),
        shape = RoundedCornerShape(
            topStartPercent = if (isLeft) 50 else 0,
            bottomStartPercent = if (isLeft) 50 else 0,
            topEndPercent = if (isLeft) 0 else 50,
            bottomEndPercent = if (isLeft) 0 else 50,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.5f),
        ),
        color =
        animateColorAsState(
            targetValue =
            if (selected) MaterialTheme.colors.primary else Color.Transparent
        ).value
    ) {
        TextButton(
            onClick = { onSelect(option) },
            modifier = modifier
                .padding(0.dp)
                .width(rowWidth.times(0.45f)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selected) MaterialTheme.colors.primary else Color.Transparent,
            )
        ) {
            if (isLeft) {
                if (selected) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_mark),
                        contentDescription = stringResource(id = R.string.mark_as_answer),
                        tint = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                            0.5f
                        )
                    )
                    Spacer(modifier = modifier.width(16.dp))
                }
                Spacer(modifier = modifier.width(16.dp))
                Text(
                    text = option.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                        0.5f
                    ),
                )
                Spacer(modifier = modifier.weight(1f))
            } else {
                Spacer(modifier = modifier.weight(1f))
                Text(
                    text = option.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                        0.5f
                    ),
                )
                Spacer(modifier = modifier.width(16.dp))
                if (selected) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_mark),
                        contentDescription = stringResource(id = R.string.mark_as_answer),
                        tint = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                            0.5f
                        )
                    )
                    Spacer(modifier = modifier.width(16.dp))
                }
            }
        }
    }
}


@Composable
fun QuestionBox(
    modifier: Modifier = Modifier,
    isOption: Boolean = true,
    selected: Boolean = false,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onMark: () -> Unit
) {
    val headerOffset = remember { mutableStateOf(0) }
    val headerWidth = remember { mutableStateOf(0) }
    //question body
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = with(LocalDensity.current) {
                    headerOffset.value.toDp()
                })
                .heightIn(
                    min =
                    if (isOption) 100.dp else 180.dp
                )
                .clip(
                    RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp,
                    )
                ),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100.copy(0.5f),
                )
            },
            shape = RoundedCornerShape(
                bottomStart = 16.dp,
                bottomEnd = 16.dp,
            ),
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

        //top
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .onGloballyPositioned {
                        headerOffset.value = it.size.height
                        headerWidth.value = it.size.width
                    },
                shape = RoundedCornerShape(
                    topStart = animateDpAsState(
                        targetValue = when {
                            isOption && selected -> 16.dp
                            !isOption -> 16.dp
                            else -> 0.dp
                        }
                    ).value,
                    topEnd = animateDpAsState(
                        targetValue = when {
                            isOption && selected -> 16.dp
                            !isOption -> 16.dp
                            else -> 0.dp
                        }
                    ).value,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary.copy(0.5f),
                ),
                color =
                animateColorAsState(
                    targetValue =
                    if (selected) MaterialTheme.colors.primary else Color.Transparent
                ).value
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = animateColorAsState(
                                targetValue =
                                if (selected) MaterialTheme.colors.onPrimary else Black100
                            ).value,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    if (isOption) {
                        TextButton(
                            onClick = onMark,
                            modifier = modifier
                                .padding(0.dp)
                                .width(
                                    with(LocalDensity.current) {
                                        headerWidth.value
                                            .toDp()
                                            .times(0.45f)
                                    }
                                ),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (selected) MaterialTheme.colors.primary else Color.Transparent,
                            )
                        ) {
                            Spacer(modifier = modifier.weight(1f))
                            Text(
                                text = if (selected) stringResource(id = R.string.correct_answer) else
                                    stringResource(id = R.string.mark_as_answer),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                                    0.5f
                                ),
                            )
                            Spacer(modifier = modifier.width(8.dp))
                            if (selected) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_mark),
                                    contentDescription = stringResource(id = R.string.mark_as_answer),
                                    tint = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                                        0.5f
                                    )
                                )
                                Spacer(modifier = modifier.width(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CreateQuestionBottomBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val questionSidePanelState by viewModel.questionSidePanelState.collectAsState()
    val icon =
        if (questionSidePanelState == true) R.drawable.icon_double_arrow_down else R.drawable.icon_double_arrow_up

    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        color = MaterialTheme.colors.onPrimary,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.3f)
        ),
        shape = CircleShape,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = modifier.width(8.dp))
            RoundedIconButton(
                onClick = {
                    //toggle question side panel
                    viewModel.onQuestionSidePanelStateChanged(
                        questionSidePanelState != true
                    )
                },
                icon = icon,
            )

            Spacer(modifier = modifier.weight(1f))

            PrimaryTextButton(
                label = "Add to Homework",
                onClick = {
                    /*todo: on Add question to homework*/
                }
            )

            Spacer(modifier = modifier.width(4.dp))
        }
    }
}


@Composable
fun CreateQuestionScreenBottomSheetContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onSelectQuestionType: (QuestionType) -> Unit,
    onSelectQuestionDifficulty: (QuestionDifficulty) -> Unit,
    onClose: () -> Unit,
) {
    val mode by viewModel.createQuestionBottomSheetMode.collectAsState()

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
            is CreateQuestionBottomSheetMode.QuestionType -> {
                QuestionTypeBottomSheetContent(
                    onSelect = onSelectQuestionType,
                    viewModel = viewModel,
                    onClose = onClose
                )
            }

            is CreateQuestionBottomSheetMode.Difficulty -> {
                QuestionDifficultyBottomSheetContent(
                    onSelect = onSelectQuestionDifficulty,
                    onClose = onClose,
                    viewModel = viewModel,
                )
            }

            is CreateQuestionBottomSheetMode.Score -> {
                //score screen
                QuestionScoreBottomSheetContent(
                    viewModel = viewModel,
                    onClose = onClose,
                )
            }

            is CreateQuestionBottomSheetMode.Duration -> {
                QuestionDurationBottomSheetContent(
                    viewModel = viewModel,
                    onClose = onClose,
                )
            }
        }

    }
}


@Composable
fun QuestionTypeBottomSheetContent(
    modifier: Modifier = Modifier,
    questionTypes: List<QuestionType> = QuestionType.values().toList(),
    onSelect: (QuestionType) -> Unit,
    onClose: () -> Unit,
    viewModel: MainViewModel,
) {
    Surface(modifier = modifier) {
        Column(modifier = modifier) {
            questionTypes.forEach { questionType ->
                QuestionTypeItem(type = questionType, onSelect = {
                    viewModel.onQuestionTypeChanged(it)
                    onSelect(it)
                    onClose()
                })
            }
        }
    }
}


@Composable
fun QuestionDifficultyBottomSheetContent(
    modifier: Modifier = Modifier,
    difficulties: List<QuestionDifficulty> = QuestionDifficulty.values().toList(),
    onSelect: (QuestionDifficulty) -> Unit,
    onClose: () -> Unit,
    viewModel: MainViewModel,
) {
    Surface(modifier = modifier) {
        Column(modifier = modifier) {
            difficulties.forEach { difficulty ->
                QuestionDifficultyItem(difficulty = difficulty, onSelect = {
                    viewModel.onQuestionDifficultyChanged(it)
                    onSelect(it)
                    onClose()
                })
            }
        }
    }
}


@Composable
fun QuestionDurationBottomSheetContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClose: () -> Unit,
) {
    val questionDurationCreateQuestion by viewModel.questionDurationCreateQuestion.collectAsState()

    Surface(modifier = modifier) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = 32.dp
                    )
                    .clip(
                        RoundedCornerShape(8.dp)
                    ),
                value = questionDurationCreateQuestion.orZeroString(),
                onValueChange = viewModel::onQuestionDurationCreateQuestionChanged,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.assessment_duration),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Black100.copy(0.5f),
                    )
                },
                shape = RoundedCornerShape(8.dp),
                textStyle = TextStyle(
                    color = Black100,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                isError = false,
            )

            Spacer(modifier = modifier.height(16.dp))

            PrimaryTextButtonFillWidth(
                label = stringResource(id = R.string.done), onClick = { onClose() })
        }
    }
}


@Composable
fun QuestionScoreBottomSheetContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClose: () -> Unit,
) {
    val questionScoreCreateQuestion by viewModel.questionScoreCreateQuestion.collectAsState()

    Surface(modifier = modifier) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = 32.dp
                    )
                    .clip(
                        RoundedCornerShape(8.dp)
                    ),
                value = questionScoreCreateQuestion.orZeroString(),
                onValueChange = viewModel::onQuestionScoreCreateQuestionChanged,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.question_score),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Black100.copy(0.5f),
                    )
                },
                shape = RoundedCornerShape(8.dp),
                textStyle = TextStyle(
                    color = Black100,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                isError = false,
            )

            Spacer(modifier = modifier.height(16.dp))

            PrimaryTextButtonFillWidth(
                label = stringResource(id = R.string.done), onClick = { onClose() })
        }
    }
}


@Composable
fun QuestionTypeItem(
    modifier: Modifier = Modifier,
    type: QuestionType,
    onSelect: (QuestionType) -> Unit,
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect(type) },
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.8f),
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = type.title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                modifier = modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun QuestionDifficultyItem(
    modifier: Modifier = Modifier,
    onSelect: (QuestionDifficulty) -> Unit,
    difficulty: QuestionDifficulty,
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect(difficulty) },
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.8f),
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = difficulty.name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                modifier = modifier.padding(16.dp)
            )
        }
    }
}


sealed class CreateQuestionBottomSheetMode {
    object QuestionType : CreateQuestionBottomSheetMode()
    object Difficulty : CreateQuestionBottomSheetMode()
    object Duration : CreateQuestionBottomSheetMode()
    object Score : CreateQuestionBottomSheetMode()
}


enum class QuestionDifficulty {
    Easy,
    Medium,
    Hard,
}


enum class QuestionType(val title: String) {
    TrueFalse("True/False"),
    MultiChoice("Multiple Choice"),
    Short("Short Answer"),
    Essay("Essay")
}


@Composable
fun QuestionConfigBox(
    modifier: Modifier = Modifier,
    label: String,
    fieldEnabled: Boolean = false,
    onConfig: () -> Unit,
    value: String,
) {
    val innerModifier = Modifier
    val constraints = QuestionConfigBoxConstraints(8.dp)
    val rowHeight = remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onConfig() },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colors.onPrimary,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        )
    ) {
        Box(modifier = Modifier
            .padding(2.dp)
            .onGloballyPositioned { rowHeight.value = it.size.height }) {
            ConstraintLayout(
                modifier = Modifier,
                constraintSet = constraints
            ) {
                Text(
                    text = label.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                    modifier = innerModifier.layoutId("label")
                )

                Divider(
                    modifier = innerModifier
                        .layoutId("divider")
                        .width(1.dp)
                        .height(
                            with(LocalDensity.current) {
                                rowHeight.value
                                    .toDp()
                                    .times(0.7f)
                            }
                        ),
                )

                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                    modifier = innerModifier.layoutId("selection")
                )

                if (!fieldEnabled) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_down),
                        contentDescription = stringResource(id = R.string.icon),
                        tint = Black100.copy(0.8f),
                        modifier = innerModifier.layoutId("arrow")
                    )
                }
            }
        }
    }
}

private fun QuestionConfigBoxConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val label = createRefFor("label")
        val divider = createRefFor("divider")
        val selection = createRefFor("selection")
        val arrow = createRefFor("arrow")

        constrain(label) {
            start.linkTo(parent.start, margin)
            top.linkTo(parent.top, margin)
            bottom.linkTo(parent.bottom, margin)
        }
        constrain(divider) {
            top.linkTo(label.top, 0.dp)
            bottom.linkTo(label.bottom, 0.dp)
            start.linkTo(label.end, 8.dp)
        }
        constrain(selection) {
            top.linkTo(label.top, 0.dp)
            bottom.linkTo(label.bottom, 0.dp)
            start.linkTo(divider.end, 12.dp)
        }
        constrain(arrow) {
            top.linkTo(label.top, 0.dp)
            bottom.linkTo(label.bottom, 0.dp)
            start.linkTo(selection.end, margin)
            end.linkTo(parent.end, margin)
        }
    }
}

enum class QuestionOption(val title: String) {
    OptionA("Option A"),
    OptionB("Option B"),
    OptionC("Option C"),
    OptionD("Option D")
}

enum class QuestionOptionTrueFalse {
    False, True
}

@Composable
@Preview
private fun QuestionConfigBoxPreview() {
    QuestionConfigBox(
        label = "duration",
        fieldEnabled = false,
        onConfig = {},
        value = "60",
    )
}

@Composable
@Preview
private fun QuestionBoxTrueFalsePreview() {
    QuestionBoxTrueFalse(
        option = QuestionOptionTrueFalse.False,
        onSelect = {},
        rowWidth = 400.dp,
        isLeft = true,
        selected = true,
    )
}