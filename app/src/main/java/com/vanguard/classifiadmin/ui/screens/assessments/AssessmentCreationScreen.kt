package com.vanguard.classifiadmin.ui.screens.assessments

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.AssessmentModel
import com.vanguard.classifiadmin.data.local.models.QuestionModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.toSimpleDate
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithInfo
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataInline
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionOption
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionOptionTrueFalse
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionType
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val ASSESSMENT_CREATION_SCREEN = "assessment_creation_screen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AssessmentCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onCreateQuestion: () -> Unit,
    onImportQuestion: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val stagedAssessmentsNetwork by viewModel.stagedAssessmentsNetwork.collectAsState()
    val heading = remember(stagedAssessmentsNetwork.data) {
        if (stagedAssessmentsNetwork is Resource.Success &&
            stagedAssessmentsNetwork.data?.isNotEmpty() == true
        ) {
            "${stagedAssessmentsNetwork.data?.first()?.name}"
        } else ""
    }
    val subheading = remember(stagedAssessmentsNetwork.data) {
        if (stagedAssessmentsNetwork is Resource.Success &&
            stagedAssessmentsNetwork.data?.isNotEmpty() == true
        ) {
            "${stagedAssessmentsNetwork.data?.first()?.startDate?.toSimpleDate()} - ${stagedAssessmentsNetwork.data?.first()?.endDate?.toSimpleDate()}"
        } else ""
    }

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
                        onSelect = {
                            if (it == AssessmentCreationAddQuestionFeature.CreateQuestion) {
                                onCreateQuestion()
                            } else {
                                onImportQuestion()
                            }
                        },
                    )
                },
                content = {
                    Scaffold(
                        modifier = modifier,
                        topBar = {
                            ChildTopBarWithInfo(
                                onBack = onBack,
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
                                onCreateQuestion = onCreateQuestion,
                                onImportQuestion = onImportQuestion,
                                viewModel = viewModel,
                            )
                        },
                        bottomBar = {
                            CreateAssessmentBottomBar(
                                onClose = {

                                },
                                onDelete = {

                                },
                                onPublish = {

                                }
                            )
                        }
                    )
                }
            )
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
) {
    val TAG = "AssessmentCreationScreenContent"
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val stagedQuestionsNetwork by viewModel.stagedQuestionsNetwork.collectAsState()

    //get all staged questions
    LaunchedEffect(Unit) {
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentUserIdPref()
        delay(1000)
        viewModel.getStagedQuestionsNetwork(
            currentSchoolIdPref.orEmpty(),
            currentUserIdPref.orEmpty()
        )

    }

    BoxWithConstraints(modifier = Modifier) {
        val maxHeight = maxHeight

        Column(modifier = Modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Questions (0)",
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

            when(stagedQuestionsNetwork){
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }
                is Resource.Success -> {
                   if(stagedQuestionsNetwork.data?.isNotEmpty() == true){
                        LazyColumn(modifier = modifier, state = rememberLazyListState()){
                            items(stagedQuestionsNetwork.data!!){question ->
                                QuestionItemAssessmentCreation(
                                    question = question.toLocal(),
                                )
                            }
                        }
                   } else {
                       Log.e(TAG, "AssessmentCreationScreenContent: no staged questions", )
                       NoQuestionPageAssessmentCreation(
                           onCreateQuestion = onCreateQuestion,
                           onImportQuestion = onImportQuestion
                       )
                   }
                }
                is Resource.Error -> {
                    Log.e(TAG, "AssessmentCreationScreenContent: an error occurred on staged questions", )
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
}

enum class AssessmentCreationAddQuestionFeature(val title: String, val icon: Int) {
    CreateQuestion("Create Question", R.drawable.icon_add),
    ImportQuestion("Import Question", R.drawable.icon_import)
}

@Composable
fun AssessmentCreationBottomSheetScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onDone: () -> Unit,
    maxHeight: Dp,
    onSelect: (AssessmentCreationAddQuestionFeature) -> Unit,
    addQuestionFeatures: List<AssessmentCreationAddQuestionFeature> =
        AssessmentCreationAddQuestionFeature.values().toList(),
) {
    val mode by viewModel.assessmentCreationBottomSheetMode.collectAsState()
    val stagedAssessmentsNetwork by viewModel.stagedAssessmentsNetwork.collectAsState()

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
            is AssessmentCreationBottomSheetMode.AddQuestion -> {
                addQuestionFeatures.forEach { feature ->
                    AssessmentCreationAddQuestionFeatureItem(
                        feature = feature,
                        onSelect = onSelect,
                    )
                }
            }

            is AssessmentCreationBottomSheetMode.Info -> {
                when (stagedAssessmentsNetwork) {
                    is Resource.Loading -> {
                        LoadingScreen(maxHeight = maxHeight)
                    }

                    is Resource.Success -> {
                        if (stagedAssessmentsNetwork.data?.isNotEmpty() == true) {
                            AssessmentCreationBottomSheetInfo(
                                onDone = onDone,
                                assessment = stagedAssessmentsNetwork.data?.first()?.toLocal()!!
                            )
                        } else {
                            NoDataInline(message = stringResource(id = R.string.could_not_load_assessment))
                        }
                    }

                    is Resource.Error -> {
                        NoDataInline(message = stringResource(id = R.string.could_not_load_assessment))
                    }
                }
            }
        }
    }
}


@Composable
fun AssessmentCreationBottomSheetInfo(
    modifier: Modifier = Modifier,
    onDone: () -> Unit,
    assessment: AssessmentModel,
) {
    BoxWithConstraints(modifier = modifier) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = assessment.name.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Black100,
            )

            Text(
                text = "Created: ${assessment.lastModified.orEmpty()}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Black100,
            )

            Spacer(modifier = modifier.height(32.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.status),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = "OPEN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.subject),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = assessment.subjectName.orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.start_date),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = assessment.startDate?.toSimpleDate().orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.end_date),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = assessment.endDate?.toSimpleDate().orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.assesment_type),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = assessment.type?.uppercase().orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.questions),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = "0",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            PrimaryTextButtonFillWidth(label = stringResource(id = R.string.done), onClick = onDone)
            Spacer(modifier = modifier.height(12.dp))
        }
    }
}


@Composable
fun AssessmentCreationAddQuestionFeatureItem(
    modifier: Modifier = Modifier,
    onSelect: (AssessmentCreationAddQuestionFeature) -> Unit,
    feature: AssessmentCreationAddQuestionFeature = AssessmentCreationAddQuestionFeature.CreateQuestion,
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect(feature) },
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
            Icon(
                painter = painterResource(id = feature.icon),
                contentDescription = stringResource(id = R.string.icon),
                tint = MaterialTheme.colors.primary,
                modifier = modifier.size(24.dp)
            )

            Text(
                text = feature.title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                modifier = modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun QuestionItemAssessmentCreation(
    modifier: Modifier = Modifier,
    question: QuestionModel,
) {
    val innerModifier = Modifier
    val constraints = QuestionItemAssessmentCreationConstraints(4.dp)
    val answer = if(question.answers.isNotEmpty()) question.answers.first()
    else ""

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val maxWidth = maxWidth

        Card(
            modifier = modifier,
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            ConstraintLayout(
                modifier = modifier,
                constraintSet = constraints,
            ) {
                Text(
                    text = question.text.orEmpty(),
                    fontSize = 12.sp,
                    color = Black100,
                    modifier = innerModifier.layoutId("question")
                )

                RoundedIconButton(
                    onClick = {},
                    tint = Black100,
                    modifier = innerModifier.layoutId("more"),
                    icon = R.drawable.icon_options_horizontal
                )

                //option
                when(question.type){
                    QuestionType.MultiChoice.name -> {
                        MultiChoiceItem(
                            answer = answer,
                            contents = listOf(
                                question.optionA.orEmpty(),
                                question.optionB.orEmpty(),
                                question.optionC.orEmpty(),
                                question.optionD.orEmpty()
                            ),
                        )
                    }
                    QuestionType.TrueFalse.name -> {
                        TrueFalseItem(
                            answer = answer
                        )
                    }
                    QuestionType.Short.name -> {
                        ShortAnswerItem(
                            answer = answer
                        )
                    }
                    QuestionType.Essay.name -> {

                    }
                }
            }
        }
    }
}

private fun QuestionItemAssessmentCreationConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val question = createRefFor("question")
        val options = createRefFor("options")
        val more = createRefFor("more")

        constrain(question) {
            start.linkTo(parent.start, 8.dp)
            top.linkTo(parent.top, 4.dp)
        }
        constrain(more) {
            top.linkTo(question.top, 0.dp)
            end.linkTo(parent.end, 4.dp)
        }
        constrain(options) {
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
            top.linkTo(question.bottom, 8.dp)
            bottom.linkTo(parent.bottom, 4.dp)
        }
    }
}

@Composable
fun MultiChoiceItem(
    modifier: Modifier = Modifier,
    answer: String,
    contents: List<String> = emptyList(),
    options: List<QuestionOption> = QuestionOption.values().toList(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        options.forEachIndexed { index, option ->
            QuestionOptionItem(
                content = contents[index],
                option = when (option) {
                    QuestionOption.OptionA -> "A"
                    QuestionOption.OptionB -> "B"
                    QuestionOption.OptionC -> "C"
                    QuestionOption.OptionD -> "D"
                },
                selected = contents[index] == answer,
            )
        }
    }
}

@Composable
fun TrueFalseItem(
    modifier: Modifier = Modifier,
    answer: String,
    options: List<QuestionOptionTrueFalse> = QuestionOptionTrueFalse.values().toList(),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        options.forEach { option ->
            QuestionOptionItem(
                content = option.name,
                option = if (option == QuestionOptionTrueFalse.True)
                    "A" else "B",
                selected = option.name == answer,
            )
            Spacer(modifier = modifier.width(8.dp))
        }
    }
}

@Composable
fun ShortAnswerItem(
    modifier: Modifier = Modifier,
    answer: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.answer).uppercase(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Black100,
            modifier = modifier.padding(4.dp)
        )


        Text(
            text = answer,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Black100,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.padding(4.dp)
        )

    }
}

@Composable
fun QuestionOptionItem(
    modifier: Modifier = Modifier,
    content: String,
    option: String = "A",
    selected: Boolean = true,
) {
    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = modifier,
            color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
                0.1f
            ),
            shape = CircleShape,
        ) {
            Box(
                modifier = modifier
                    .size(22.dp)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape,
                    ), contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_tick),
                        contentDescription = stringResource(id = R.string.icon_tick),
                        tint = MaterialTheme.colors.onPrimary,
                        modifier = modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = option.uppercase(),
                        fontSize = 12.sp,
                        color = Black100,
                    )
                }
            }
        }

        Text(
            text = content,
            fontSize = 12.sp,
            color = Black100,
            modifier = modifier.padding(8.dp)
        )
    }
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

@Composable
@Preview
private fun MultiChoiceItem(){
    MultiChoiceItem(
        answer = "Choice",
        contents = listOf(
            "Boy", "Choice", "Baby", "Shoe"
        ),
    )
}