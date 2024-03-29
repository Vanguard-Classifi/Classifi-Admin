package com.vanguard.classifiadmin.ui.screens.assessments

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.QuestionModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.helpers.toSimpleDate
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithInfo
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.SuccessBar
import com.vanguard.classifiadmin.ui.components.YesNoPrompt
import com.vanguard.classifiadmin.ui.screens.assessments.bottomsheet.AssessmentCreationBottomSheetScreen
import com.vanguard.classifiadmin.ui.screens.assessments.items.QuestionItemAssessmentCreation
import com.vanguard.classifiadmin.ui.screens.assessments.questions.CreateQuestionMessage
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionOptionTrueFalse
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.QuestionOptionItem
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.ShortAnswerItem
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.TrueFalseItem
import com.vanguard.classifiadmin.ui.screens.feeds.FeedState
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val ASSESSMENT_CREATION_SCREEN = "assessment_creation_screen"

/**
 * A screen where you get to manage your assessment,
 * you can add or delete questions from the assessment,
 * and ultimately you can publish or even delete the assessment
 */

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun AssessmentCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onCreateQuestion: () -> Unit,
    onImportQuestion: () -> Unit,
    onEditQuestion: () -> Unit,
) {
    val TAG = "AssessmentCreationScreen"
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val message by viewModel.assessmentCreationMessage.collectAsState()
    val openMode by viewModel.assessmentCreationOpenMode.collectAsState()
    val currentAssessmentIdDraft by viewModel.currentAssessmentIdDraft.collectAsState()
    val assessmentByIdNetwork by viewModel.assessmentByIdNetwork.collectAsState()
    val heading: String = remember(assessmentByIdNetwork.data) {
        if (assessmentByIdNetwork is Resource.Success &&
            assessmentByIdNetwork.data != null
        ) {
            "${assessmentByIdNetwork.data?.name}"
        } else ""
    }
    val subheading: String = remember(assessmentByIdNetwork.data) {
        if (assessmentByIdNetwork is Resource.Success &&
            assessmentByIdNetwork.data != null
        ) {
            "${assessmentByIdNetwork.data?.startDate?.toSimpleDate()} - ${assessmentByIdNetwork.data?.endDate?.toSimpleDate()}"
        } else ""
    }
    val deleteAssessmentState = remember { mutableStateOf(false) }
    val selectedQuestionIdCreateQuestion by viewModel.selectedQuestionIdCreateQuestion.collectAsState()
    val questionByIdNetwork by viewModel.questionByIdNetwork.collectAsState()
    val currentFeedIdPending by viewModel.currentFeedIdPending.collectAsState()
    val feedByIdNetwork by viewModel.feedByIdNetwork.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentSchoolIdPref()
        viewModel.onCurrentFeedIdPendingChanged(currentAssessmentIdDraft)
        delay(1000)
        viewModel.getAssessmentByIdNetwork(
            currentAssessmentIdDraft.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
        viewModel.getQuestionByIdNetwork(
            selectedQuestionIdCreateQuestion.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
        viewModel.getFeedByIdNetwork(
            currentFeedIdPending.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
    }

    LaunchedEffect(selectedQuestionIdCreateQuestion) {
        viewModel.getQuestionByIdNetwork(
            selectedQuestionIdCreateQuestion.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
    }

    LaunchedEffect(showModalSheet.value) {
        viewModel.getVerifiedQuestionsByAssessmentNetwork(
            currentAssessmentIdDraft.orEmpty(),
            currentSchoolIdPref.orEmpty(),
        )
        viewModel.getAssessmentByIdNetwork(
            currentAssessmentIdDraft.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
    }

    LaunchedEffect(message) {
        if (message !is AssessmentCreationMessage.NoMessage) {
            delay(2000)
            viewModel.onAssessmentCreationMessageChanged(
                AssessmentCreationMessage.NoMessage
            )
            onBack()
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
                    AssessmentCreationBottomSheetScreen(
                        viewModel = viewModel,
                        onDone = {
                        },
                        maxHeight = maxHeight,
                        onSelectAddQuestionFeature = {
                            if (it == AssessmentCreationAddQuestionFeature.CreateQuestion) {
                                viewModel.onAssessmentCreationOpenModeChanged(
                                    AssessmentCreationOpenMode.Creator
                                )
                                onCreateQuestion()
                            } else {
                                onImportQuestion()
                            }
                        },
                        onSelectQuestionOptionFeature = {
                            if (it == AssessmentCreationQuestionOptionFeature.RemoveQuestion) {
                                coroutineScope.launch {
                                    //remove question from assessment
                                    if (assessmentByIdNetwork is Resource.Success &&
                                        assessmentByIdNetwork.data != null &&
                                        questionByIdNetwork is Resource.Success &&
                                        questionByIdNetwork.data != null
                                    ) {
                                        questionByIdNetwork.data?.parentAssessmentIds?.remove(
                                            assessmentByIdNetwork.data?.assessmentId.orEmpty(),
                                        )
                                        assessmentByIdNetwork.data?.questionIds?.remove(
                                            questionByIdNetwork.data?.questionId.orEmpty()
                                        )
                                        viewModel.saveAssessmentAsVerifiedNetwork(
                                            assessmentByIdNetwork.data!!,
                                            onResult = {}
                                        )
                                        viewModel.saveQuestionAsVerifiedNetwork(
                                            questionByIdNetwork.data!!,
                                            onResult = {}
                                        )
                                    }
                                    showModalSheet.value = false
                                    sheetState.hide()
                                }

                            } else {
                                viewModel.onAssessmentCreationOpenModeChanged(
                                    AssessmentCreationOpenMode.Editor
                                )
                                onEditQuestion()
                            }
                        }
                    )
                },
                content = {
                    Scaffold(
                        modifier = modifier,
                        topBar = {
                            ChildTopBarWithInfo(
                                onBack = {
                                    onBack()
                                },
                                onInfo = {
                                    viewModel.onAssessmentCreationBottomSheetModeChanged(
                                        AssessmentCreationBottomSheetMode.Info
                                    )
                                    coroutineScope.launch {
                                        showModalSheet.value = true
                                        sheetState.show()
                                    }
                                },
                                elevation = 0.dp,
                                heading = heading,
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary,
                                subheading = subheading
                            )
                        },
                        content = {
                            AssessmentCreationScreenContent(
                                modifier = Modifier.padding(it),
                                onAddQuestion = {
                                    viewModel.onAssessmentCreationBottomSheetModeChanged(
                                        AssessmentCreationBottomSheetMode.AddQuestion
                                    )
                                    coroutineScope.launch {
                                        showModalSheet.value = true
                                        sheetState.show()
                                    }
                                },
                                onCreateQuestion = {
                                    viewModel.onAssessmentCreationOpenModeChanged(
                                        AssessmentCreationOpenMode.Creator
                                    )
                                    onCreateQuestion()
                                },
                                onImportQuestion = onImportQuestion,
                                viewModel = viewModel,
                                onQuestionOptions = {
                                    viewModel.onAssessmentCreationBottomSheetModeChanged(
                                        AssessmentCreationBottomSheetMode.QuestionOptions
                                    )
                                    coroutineScope.launch {
                                        showModalSheet.value = true
                                        sheetState.show()
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            CreateAssessmentBottomBar(
                                onClose = {
                                    onBack()
                                },
                                onDelete = {
                                    deleteAssessmentState.value = true
                                },
                                onPublish = {
                                    //on publish
                                    coroutineScope.launch {
                                        if (assessmentByIdNetwork is Resource.Success &&
                                            assessmentByIdNetwork.data != null
                                        ) {
                                            assessmentByIdNetwork.data?.state =
                                                AssessmentState.Published.title

                                            viewModel.saveAssessmentAsVerifiedNetwork(
                                                assessmentByIdNetwork.data!!,
                                                onResult = {}
                                            )
                                        } else return@launch
                                    }.invokeOnCompletion {
                                        runnableBlock {
                                            //show message
                                            viewModel.onAssessmentCreationMessageChanged(
                                                AssessmentCreationMessage.AssessmentPublished
                                            )
                                            //post feed
                                            if(feedByIdNetwork is Resource.Success &&
                                                    feedByIdNetwork.data != null){
                                                feedByIdNetwork.data?.state =
                                                    FeedState.Published.name

                                                viewModel.saveFeedAsVerifiedNetwork(
                                                    feedByIdNetwork.data!!,
                                                    onResult = {}
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    )
                }
            )

            //handle all messages and prompts
            AnimatedVisibility(
                visible = deleteAssessmentState.value,
                enter = scaleIn(
                    initialScale = 0.8f, animationSpec = tween(
                        durationMillis = 50, easing = FastOutLinearInEasing
                    )
                ),
                exit = scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(
                        durationMillis = 50, easing = FastOutLinearInEasing
                    ),
                ),
            ) {
                Popup(alignment = Alignment.Center,
                    offset = IntOffset(0, 0),
                    onDismissRequest = { deleteAssessmentState.value = false }) {
                    YesNoPrompt(
                        maxHeight = maxHeight,
                        maxWidth = maxWidth,
                        onYes = {
                            //delete assessment
                            if (assessmentByIdNetwork is Resource.Success &&
                                assessmentByIdNetwork.data != null
                            ) {
                                viewModel.deleteAssessmentNetwork(
                                    assessmentByIdNetwork.data!!,
                                    onResult = {}
                                )
                                onBack()
                            }
                        },
                        onNo = {
                            deleteAssessmentState.value = false
                        },
                        label = stringResource(id = R.string.delete_this_assessment)
                    )
                }
            }


            if(message !is AssessmentCreationMessage.NoMessage){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    SuccessBar(message = message.message, maxWidth = maxWidth)
                }
            }

        }
    }
}


@Composable
fun AssessmentCreationScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onAddQuestion: () -> Unit,
    onCreateQuestion: () -> Unit,
    onImportQuestion: () -> Unit,
    onQuestionOptions: (QuestionModel) -> Unit,
) {
    val TAG = "AssessmentCreationScreenContent"
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val openMode by viewModel.assessmentCreationOpenMode.collectAsState()
    val currentAssessmentIdDraft by viewModel.currentAssessmentIdDraft.collectAsState()
    val assessmentByIdNetwork by viewModel.assessmentByIdNetwork.collectAsState()
    val verifiedQuestionsByAssessmentNetwork by viewModel.verifiedQuestionsByAssessmentNetwork.collectAsState()


    val totalQuestions = remember(Unit, assessmentByIdNetwork.data) {
        if (assessmentByIdNetwork is Resource.Success &&
            assessmentByIdNetwork.data != null
        ) {
            "Questions (${assessmentByIdNetwork.data?.questionIds?.size})"
        } else "Questions (0)"
    }

    //get all staged questions
    LaunchedEffect(Unit) {
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentUserIdPref()
        delay(1000)
        viewModel.getVerifiedQuestionsByAssessmentNetwork(
            currentAssessmentIdDraft.orEmpty(),
            currentSchoolIdPref.orEmpty(),
        )
    }

    BoxWithConstraints(modifier = Modifier) {
        val maxHeight = maxHeight

        Column(modifier = Modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = totalQuestions,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Black100,
                )

                RoundedIconButton(
                    onClick = onAddQuestion,
                    icon = R.drawable.icon_add,
                    tint = Black100,
                )
            }

            Divider(modifier = modifier.fillMaxWidth())

            when (verifiedQuestionsByAssessmentNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedQuestionsByAssessmentNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(modifier = modifier, state = rememberLazyListState()) {
                            val verifiedQuestionsSorted =
                                verifiedQuestionsByAssessmentNetwork.data?.sortedBy {
                                    it.position
                                }
                            items(verifiedQuestionsSorted!!) { question ->
                                QuestionItemAssessmentCreation(
                                    question = question.toLocal(),
                                    onOptions = {
                                        viewModel.onSelectedQuestionIdCreateQuestionChanged(
                                            it.questionId
                                        )
                                        onQuestionOptions(it)
                                    }
                                )
                            }
                        }
                    } else {
                        NoQuestionPageAssessmentCreation(
                            onCreateQuestion = onCreateQuestion,
                            onImportQuestion = onImportQuestion
                        )
                    }
                }

                is Resource.Error -> {
                    NoQuestionPageAssessmentCreation(
                        onCreateQuestion = onCreateQuestion,
                        onImportQuestion = onImportQuestion
                    )
                }
            }
        }
    }
}


@Composable
fun NoQuestionPageAssessmentCreation(
    modifier: Modifier = Modifier,
    onCreateQuestion: () -> Unit,
    onImportQuestion: () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_question),
                contentDescription = stringResource(id = R.string.no_questions),
                modifier = modifier.size(72.dp),
                tint = Black100,
            )

            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.no_questions),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Black100,
            )

            Spacer(modifier = modifier.height(32.dp))

            PrimaryTextButton(
                label = stringResource(id = R.string.create_questions),
                onClick = onCreateQuestion,
            )

            Spacer(modifier = modifier.height(16.dp))
            PrimaryTextButton(
                label = stringResource(id = R.string.import_questions),
                onClick = onImportQuestion,
            )
        }
    }
}


@Composable
fun CreateAssessmentBottomBar(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    onPublish: () -> Unit,
) {
    val constraints = CreateAssessmentBottomBarConstraints(4.dp)
    val innerModifier = Modifier
    var rowHeight by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .onGloballyPositioned { rowHeight = it.size.height },
        color = MaterialTheme.colors.onPrimary,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.3f)
        ),
        shape = CircleShape,
    ) {
        Box(
            modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ConstraintLayout(
                modifier = modifier
                    .padding(0.dp)
                    .fillMaxWidth(),
                constraintSet = constraints,
            ) {
                RoundedIconButton(
                    onClick = onClose,
                    icon = R.drawable.icon_close,
                    modifier = innerModifier.layoutId("close"),
                    tint = MaterialTheme.colors.primary,
                )

                Divider(
                    modifier = innerModifier
                        .width(1.dp)
                        .height(
                            with(LocalDensity.current) {
                                rowHeight
                                    .toDp()
                                    .times(0.7f)
                            }
                        )
                        .layoutId("divider")
                )

                RoundedIconButton(
                    onClick = onDelete,
                    icon = R.drawable.icon_delete,
                    modifier = innerModifier.layoutId("delete"),
                    tint = MaterialTheme.colors.primary,
                )

                PrimaryTextButton(
                    modifier = innerModifier.layoutId("publish"),
                    label = stringResource(id = R.string.publish),
                    onClick = onPublish,
                )
            }
        }
    }
}

private fun CreateAssessmentBottomBarConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val close = createRefFor("close")
        val divider = createRefFor("divider")
        val delete = createRefFor("delete")
        val publish = createRefFor("publish")

        constrain(close) {
            start.linkTo(parent.start, 8.dp)
            top.linkTo(parent.top, margin)
            bottom.linkTo(parent.bottom, margin)
        }
        constrain(divider) {
            top.linkTo(close.top, 0.dp)
            bottom.linkTo(close.bottom, 0.dp)
            start.linkTo(close.end, 12.dp)
        }
        constrain(delete) {
            top.linkTo(close.top, 0.dp)
            bottom.linkTo(close.bottom, 0.dp)
            start.linkTo(divider.end, 12.dp)
        }
        constrain(publish) {
            top.linkTo(close.top, 0.dp)
            bottom.linkTo(close.bottom, 0.dp)
            end.linkTo(parent.end, margin)
        }
    }
}


sealed class AssessmentCreationBottomSheetMode {
    object AddQuestion : AssessmentCreationBottomSheetMode()
    object Info : AssessmentCreationBottomSheetMode()
    object QuestionOptions : AssessmentCreationBottomSheetMode()
}

enum class AssessmentCreationAddQuestionFeature(val title: String, val icon: Int) {
    CreateQuestion("Create Question", R.drawable.icon_add),
    ImportQuestion("Import Question", R.drawable.icon_import)
}

enum class AssessmentCreationQuestionOptionFeature(val title: String, val icon: Int) {
    EditQuestion("Edit Question", R.drawable.icon_edit),
    RemoveQuestion("Remove Question", R.drawable.icon_delete)
}

sealed class AssessmentCreationOpenMode {
    object Creator : AssessmentCreationOpenMode()
    object Editor : AssessmentCreationOpenMode()
}

sealed class AssessmentCreationMessage(val message: String) {
    object SaveAssessmentToDraft : AssessmentCreationMessage("Saving assessment to draft")
    object AssessmentPublished : AssessmentCreationMessage("Assessment published successfully!")
    object AssessmentDeleted : AssessmentCreationMessage("Assessment deleted successfully!")
    object NoMessage : AssessmentCreationMessage("")
}

@Composable
@Preview
private fun CreateAssessmentBottomBarPreview() {
    CreateAssessmentBottomBar(
        onClose = {},
        onDelete = {},
        onPublish = {}
    )
}


@Preview
@Composable
private fun QuestionOptionItemPreview() {
    QuestionOptionItem(
        content = "Zebra",
        option = "A"
    )
}


@Preview
@Composable
private fun ShortAnswerItemPreview() {
    ShortAnswerItem(
        answer = "Khalid Isah"
    )
}

@Preview
@Composable
private fun TrueFalseItemPreview() {
    TrueFalseItem(
        answer = QuestionOptionTrueFalse.True.name,
    )
}