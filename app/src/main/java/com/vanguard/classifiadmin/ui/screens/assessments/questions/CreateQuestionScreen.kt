package com.vanguard.classifiadmin.ui.screens.assessments.questions

import android.util.Log
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.helpers.todayComputational
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithCloseButtonOnly
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.SuccessBar
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentCreationOpenMode
import com.vanguard.classifiadmin.ui.screens.assessments.questions.bottomsheet.CreateQuestionScreenBottomSheetContent
import com.vanguard.classifiadmin.ui.screens.assessments.questions.boxes.QuestionBox
import com.vanguard.classifiadmin.ui.screens.assessments.questions.boxes.QuestionBoxTrueFalse
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val CREATE_QUESTION_SCREEN = "create_question_screen"

/**
 * A screen where each question is created.
 * Here you get to compose the question body,
 * the type of question whether Multiple choice,
 * True/False, Short Answer or Essay question
 */

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
    val message by viewModel.createQuestionMessage.collectAsState()
    val addable = remember { mutableStateOf(false) }

    LaunchedEffect(message) {
        if (message !is CreateQuestionMessage.NoMessage) {
            if (message is CreateQuestionMessage.QuestionSaved)
                addable.value = true
            delay(2000)
            viewModel.onCreateQuestionMessageChanged(
                CreateQuestionMessage.NoMessage
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
                    CreateQuestionScreenBottomSheetContent(
                        viewModel = viewModel,
                        onSelectQuestionType = {
                            viewModel.onQuestionTypeChanged(it)
                        },
                        onSelectQuestionDifficulty = {
                           viewModel.onQuestionDifficultyChanged(it)
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
                                onClose = {
                                    viewModel.clearCreateQuestionFields()
                                    onBack()
                                },
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
                            CreateQuestionBottomBar(
                                viewModel = viewModel,
                                onAddQuestion = {
                                    //go to assessment creation screen
                                    onBack()
                                    //clear the question fields
                                    viewModel.clearCreateQuestionFields()
                                },
                                addable = addable.value,
                            )
                        }
                    )
                }
            )

            if (message !is CreateQuestionMessage.NoMessage) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    when (message) {
                        is CreateQuestionMessage.QuestionSaved -> {
                            SuccessBar(message = message.message, maxWidth = maxWidth)
                        }

                        is CreateQuestionMessage.AddQuestionBody -> {
                            MessageBar(
                                message = message.message,
                                icon = R.drawable.icon_info,
                                onClose = {
                                    viewModel.onCreateQuestionMessageChanged(
                                        CreateQuestionMessage.NoMessage
                                    )
                                },
                                maxWidth = maxWidth,
                                modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                            )
                        }

                        else -> {

                        }
                    }
                }
            }
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
    val TAG = "CreateQuestionScreenContent"
    val verticalScroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val correctQuestionOption by viewModel.correctQuestionOption.collectAsState()
    val questionBodyCreateQuestion by viewModel.questionBodyCreateQuestion.collectAsState()
    val questionOptionACreateQuestion by viewModel.questionOptionACreateQuestion.collectAsState()
    val questionOptionBCreateQuestion by viewModel.questionOptionBCreateQuestion.collectAsState()
    val questionOptionCCreateQuestion by viewModel.questionOptionCCreateQuestion.collectAsState()
    val questionOptionDCreateQuestion by viewModel.questionOptionDCreateQuestion.collectAsState()
    val questionType by viewModel.questionTypeCreateQuestion.collectAsState()
    val questionDifficulty by viewModel.questionDifficultyCreateQuestion.collectAsState()
    val questionScoreCreateQuestion by viewModel.questionScoreCreateQuestion.collectAsState()
    val assessmentDurationCreateQuestion by viewModel.assessmentDurationCreateQuestion.collectAsState()

    val localModifier = Modifier
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val questionAnswersCreateQuestion by viewModel.questionAnswersCreateQuestion.collectAsState()
    val rowWidthTrueFalse = remember { mutableStateOf(0) }
    val correctAnswerTrueFalse by viewModel.correctAnswerTrueFalse.collectAsState()
    val correctShortAnswerCreateQuestion by viewModel.correctShortAnswerCreateQuestion.collectAsState()
    val openMode by viewModel.assessmentCreationOpenMode.collectAsState()
    val currentAssessmentIdDraft by viewModel.currentAssessmentIdDraft.collectAsState()
    val assessmentByIdNetwork by viewModel.assessmentByIdNetwork.collectAsState()
    val selectedQuestionIdCreateQuestion by viewModel.selectedQuestionIdCreateQuestion.collectAsState()
    val questionByIdNetwork by viewModel.questionByIdNetwork.collectAsState()
    val currentFeedIdPending by viewModel.currentFeedIdPending.collectAsState()
    val feedByIdNetwork by viewModel.feedByIdNetwork.collectAsState()

    LaunchedEffect(Unit, openMode) {
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentSchoolIdPref()
        delay(1000)
        viewModel.getFeedByIdNetwork(
            currentFeedIdPending.orEmpty(),
            currentSchoolIdPref.orEmpty(),
        )
        viewModel.getAssessmentByIdNetwork(
            currentAssessmentIdDraft.orEmpty(),
            currentSchoolIdPref.orEmpty(),
        )
        delay(200)
        if (assessmentByIdNetwork is Resource.Success &&
            assessmentByIdNetwork.data != null
        ) {
            //update the assessment duration
            viewModel.onAssessmentDurationCreateQuestionChanged(
                assessmentByIdNetwork.data?.duration
            )
        }

        if (openMode is AssessmentCreationOpenMode.Editor) {
            //open the selected question
            viewModel.getQuestionByIdNetwork(
                selectedQuestionIdCreateQuestion.orEmpty(),
                currentSchoolIdPref.orEmpty(),
            )
            delay(1000)
            if (questionByIdNetwork is Resource.Success &&
                questionByIdNetwork.data != null
            ) {
                questionByIdNetwork.data?.apply {
                    viewModel.onQuestionBodyCreateQuestionChanged(
                        this.text
                    )
                    viewModel.onQuestionTypeChanged(this.type?.toQuestionType()!!)
                    viewModel.onQuestionScoreCreateQuestionChanged(
                        this.maxScore.toString().orZeroString()
                    )
                    viewModel.onQuestionDifficultyChanged(
                        this.difficulty?.toQuestionDifficulty()!!
                    )
                    when (this.type) {
                        QuestionType.MultiChoice.title -> {
                            viewModel.onQuestionOptionACreateQuestionChanged(
                                this.optionA
                            )
                            viewModel.onQuestionOptionBCreateQuestionChanged(
                                this.optionB
                            )
                            viewModel.onQuestionOptionCCreateQuestionChanged(
                                this.optionC
                            )
                            viewModel.onQuestionOptionDCreateQuestionChanged(
                                this.optionD
                            )
                            viewModel.clearAnswersCreateQuestion()
                            viewModel.onAddToAnswersCreateQuestion(
                                this.answers.first()
                            )
                        }

                        QuestionType.TrueFalse.title -> {
                            viewModel.clearAnswersCreateQuestion()
                            viewModel.onAddToAnswersCreateQuestion(
                                this.answers.first()
                            )
                        }

                        QuestionType.Short.title -> {
                            viewModel.onQuestionShortAnswerCreateQuestionChanged(
                                this.answers.first()
                            )
                            viewModel.clearAnswersCreateQuestion()
                            viewModel.onAddToAnswersCreateQuestion(
                                this.answers.first()
                            )
                        }

                        QuestionType.Essay.title -> {
                        }
                    }
                }
            }

        }
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
                    value = assessmentDurationCreateQuestion.orZeroString(),
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

                        QuestionType.Essay -> {}

                        QuestionType.Short -> {
                            OutlinedTextField(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                value = correctShortAnswerCreateQuestion.orEmpty(),
                                onValueChange = viewModel::onQuestionShortAnswerCreateQuestionChanged,
                                placeholder = {
                                    Text(
                                        text = stringResource(id = R.string.short_answer),
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
                                isError = false,
                            )
                            Spacer(modifier = modifier.height(8.dp))
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
                                        isLeft = option == QuestionOptionTrueFalse.True,
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
                            if (questionBodyCreateQuestion?.isBlank() == true) {
                                viewModel.onCreateQuestionMessageChanged(
                                    CreateQuestionMessage.AddQuestionBody
                                )
                                return@PrimaryTextButtonFillWidth
                            }

                            scope.launch {
                                when (questionType) {
                                    QuestionType.MultiChoice -> {
                                        //add answer to answers buffer
                                        when (correctQuestionOption) {
                                            QuestionOption.OptionA -> {
                                                viewModel.clearAnswersCreateQuestion()
                                                viewModel.onAddToAnswersCreateQuestion(
                                                    questionOptionACreateQuestion.orEmpty()
                                                )
                                            }

                                            QuestionOption.OptionB -> {
                                                viewModel.clearAnswersCreateQuestion()
                                                viewModel.onAddToAnswersCreateQuestion(
                                                    questionOptionBCreateQuestion.orEmpty()
                                                )
                                            }

                                            QuestionOption.OptionC -> {
                                                viewModel.clearAnswersCreateQuestion()
                                                viewModel.onAddToAnswersCreateQuestion(
                                                    questionOptionCCreateQuestion.orEmpty()
                                                )
                                            }

                                            QuestionOption.OptionD -> {
                                                viewModel.clearAnswersCreateQuestion()
                                                viewModel.onAddToAnswersCreateQuestion(
                                                    questionOptionDCreateQuestion.orEmpty()
                                                )
                                            }
                                        }
                                    }

                                    QuestionType.Short -> {
                                        viewModel.clearAnswersCreateQuestion()
                                        viewModel.onAddToAnswersCreateQuestion(
                                            correctShortAnswerCreateQuestion.orEmpty()
                                        )
                                    }

                                    QuestionType.TrueFalse -> {
                                        viewModel.clearAnswersCreateQuestion()
                                        when (correctAnswerTrueFalse) {
                                            QuestionOptionTrueFalse.True -> {
                                                viewModel.onAddToAnswersCreateQuestion(
                                                    QuestionOptionTrueFalse.True.name
                                                )
                                            }

                                            QuestionOptionTrueFalse.False -> {
                                                viewModel.onAddToAnswersCreateQuestion(
                                                    QuestionOptionTrueFalse.True.name
                                                )
                                            }
                                        }
                                    }

                                    QuestionType.Essay -> {
                                        viewModel.clearAnswersCreateQuestion()
                                    }
                                }

                                val answerBuffer = ArrayList<String>()
                                questionAnswersCreateQuestion.map { answerBuffer.add(it) }

                                val parentAssessmentId: String =
                                    if (assessmentByIdNetwork is Resource.Success &&
                                        assessmentByIdNetwork.data != null
                                    ) {
                                        assessmentByIdNetwork.data?.assessmentId.orEmpty()
                                    } else ""

                                //save the assessment duration in the feed
                                if(feedByIdNetwork is Resource.Success &&
                                        feedByIdNetwork.data != null){
                                    feedByIdNetwork.data?.assessmentDuration =
                                        assessmentDurationCreateQuestion.orEmpty()

                                    viewModel.saveFeedAsVerifiedNetwork(
                                        feedByIdNetwork.data!!, onResult = {}
                                    )
                                }

                                when (openMode) {
                                    is AssessmentCreationOpenMode.Creator -> {
                                        val questionId = UUID.randomUUID().toString()
                                        val questionPosition: Int =
                                            if (assessmentByIdNetwork is Resource.Success &&
                                                assessmentByIdNetwork.data != null
                                            ) {
                                                assessmentByIdNetwork.data?.questionIds?.size!! + 1
                                            } else -1

                                        if(assessmentByIdNetwork is Resource.Success &&
                                                assessmentByIdNetwork.data != null){
                                            assessmentByIdNetwork.data?.duration =
                                                assessmentDurationCreateQuestion
                                        }


                                        val question = QuestionModel(
                                            questionId = questionId,
                                            parentAssessmentIds = arrayListOf(parentAssessmentId),
                                            schoolId = currentSchoolIdPref.orEmpty(),
                                            type = questionType.title,
                                            difficulty = questionDifficulty.name,
                                            maxScore = questionScoreCreateQuestion.orZeroString()
                                                .toInt(),
                                            text = questionBodyCreateQuestion.orEmpty(),
                                            optionA = questionOptionACreateQuestion.orEmpty(),
                                            optionB = questionOptionBCreateQuestion.orEmpty(),
                                            optionC = questionOptionCCreateQuestion.orEmpty(),
                                            optionD = questionOptionDCreateQuestion.orEmpty(),
                                            answers = answerBuffer,
                                            lastModified = todayComputational(),
                                            authorId = currentUserIdPref.orEmpty(),
                                            position = questionPosition,
                                        )

                                        if(!assessmentByIdNetwork.data?.questionIds?.contains(questionId)!!) {
                                            assessmentByIdNetwork.data?.questionIds?.add(questionId)
                                        }

                                        viewModel.onSelectedQuestionIdCreateQuestionChanged(questionId)
                                        viewModel.saveQuestionAsVerifiedNetwork(
                                            question.toNetwork(),
                                            onResult = {}
                                        )

                                        viewModel.saveAssessmentAsVerifiedNetwork(
                                            assessmentByIdNetwork.data!!,
                                            onResult = {}
                                        )
                                    }

                                    is AssessmentCreationOpenMode.Editor -> {
                                        if (assessmentByIdNetwork is Resource.Success &&
                                            assessmentByIdNetwork.data != null &&
                                            questionByIdNetwork is Resource.Success &&
                                            questionByIdNetwork.data != null
                                        ) {
                                            assessmentByIdNetwork.data?.duration =
                                                assessmentDurationCreateQuestion

                                            questionByIdNetwork.data?.apply {
                                                type = questionType.title
                                                difficulty = questionDifficulty.name
                                                maxScore =
                                                    questionScoreCreateQuestion.orZeroString()
                                                        .toInt()
                                                text = questionBodyCreateQuestion.orEmpty()
                                                optionA = questionOptionACreateQuestion.orEmpty()
                                                optionB = questionOptionBCreateQuestion.orEmpty()
                                                optionC = questionOptionCCreateQuestion.orEmpty()
                                                optionD = questionOptionDCreateQuestion.orEmpty()
                                                answers = answerBuffer
                                                lastModified = todayComputational()
                                            }

                                            viewModel.saveAssessmentAsVerifiedNetwork(
                                                assessmentByIdNetwork.data!!, onResult = {}
                                            )

                                            viewModel.saveQuestionAsVerifiedNetwork(
                                                questionByIdNetwork.data!!, onResult = {}
                                            )
                                        }
                                    }
                                }
                            }.invokeOnCompletion {
                                runnableBlock {
                                    viewModel.onAssessmentCreationOpenModeChanged(
                                        AssessmentCreationOpenMode.Editor
                                    )
                                    viewModel.onCreateQuestionMessageChanged(
                                        CreateQuestionMessage.QuestionSaved
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun CreateQuestionBottomBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onAddQuestion: () -> Unit,
    addable: Boolean,
) {
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val questionSidePanelState by viewModel.questionSidePanelState.collectAsState()
    val openMode by viewModel.assessmentCreationOpenMode.collectAsState()
    val currentAssessmentIdDraft by viewModel.currentAssessmentIdDraft.collectAsState()
    val assessmentByIdNetwork by viewModel.assessmentByIdNetwork.collectAsState()

    val icon =
        if (questionSidePanelState == true) R.drawable.icon_double_arrow_down else R.drawable.icon_double_arrow_up

    val assessmentType: String = remember(assessmentByIdNetwork.data) {
        if (assessmentByIdNetwork is Resource.Success &&
            assessmentByIdNetwork.data != null
        ) {
            "${assessmentByIdNetwork.data?.type}"
        } else ""
    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentSchoolIdPref()
        delay(1000)
        viewModel.getAssessmentByIdNetwork(
            currentAssessmentIdDraft.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
    }


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
                enabled = addable,
                label = if (assessmentType.isBlank()) "Add" else "Add to $assessmentType",
                onClick = {
                    onAddQuestion()
                }
            )

            Spacer(modifier = modifier.width(4.dp))
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

fun String.toQuestionDifficulty(): QuestionDifficulty {
    return when (this) {
        QuestionDifficulty.Easy.name -> QuestionDifficulty.Easy
        QuestionDifficulty.Medium.name -> QuestionDifficulty.Medium
        QuestionDifficulty.Hard.name -> QuestionDifficulty.Hard
        else -> QuestionDifficulty.Easy
    }
}


enum class QuestionType(val title: String) {
    TrueFalse("True/False"),
    MultiChoice("Multiple Choice"),
    Short("Short Answer"),
    Essay("Essay")
}

fun String.toQuestionType(): QuestionType {
    return when (this) {
        QuestionType.MultiChoice.title -> QuestionType.MultiChoice
        QuestionType.TrueFalse.title -> QuestionType.TrueFalse
        QuestionType.Short.title -> QuestionType.Short
        QuestionType.Essay.title -> QuestionType.Essay
        else -> QuestionType.MultiChoice
    }
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
    True, False
}

sealed class CreateQuestionMessage(val message: String) {
    object QuestionSaved : CreateQuestionMessage("Question saved successfully!")
    object AddQuestionBody : CreateQuestionMessage("Add a question body")
    object NoMessage : CreateQuestionMessage("")
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