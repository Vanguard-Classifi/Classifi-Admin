package com.vanguard.classifiadmin.ui.screens.assessments

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithCloseButtonOnly
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithInfo
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.SecondaryButtonWithIconStyled
import com.vanguard.classifiadmin.ui.components.SecondaryTextButton
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch

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
) {
    val verticalScroll = rememberScrollState()
    val correctQuestionOption by viewModel.correctQuestionOption.collectAsState()


    BoxWithConstraints(modifier = Modifier) {
        Column(
            modifier = Modifier
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

            Spacer(modifier = modifier.height(0.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                QuestionConfigBox(
                    label = stringResource(id = R.string.type),
                    onConfig = { onConfigQuestionType() },
                    value = "",
                )
            }

            Spacer(modifier = modifier.height(0.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuestionConfigBox(
                    label = stringResource(id = R.string.difficulty),
                    onConfig = {
                        onConfigQuestionDifficulty()
                    },
                    value = "",
                )

                QuestionConfigBox(
                    label = stringResource(id = R.string.score),
                    onConfig = {
                        onConfigQuestionScore()
                    },
                    value = "",
                )
            }
            Spacer(modifier = modifier.height(0.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                QuestionConfigBox(
                    label = stringResource(id = R.string.duration),
                    onConfig = {
                        onConfigAssessmentDuration()
                    },
                    value = "",
                )
            }


            Spacer(modifier = modifier.height(8.dp))

            Card(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 8.dp, bottom = 8.dp, start = 0.dp, end = 0.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                ),
                elevation = 2.dp,
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.question_body).uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black100,
                    )

                    Spacer(modifier = modifier.height(4.dp))

                    Box(
                        modifier = modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        OutlinedTextField(
                            enabled = false,
                            readOnly = true,
                            modifier = modifier
                                .fillMaxWidth()
                                .heightIn(min = 180.dp)
                                .clip(
                                    RoundedCornerShape(
                                        bottomStart = 16.dp,
                                        bottomEnd = 16.dp,
                                    )
                                ),
                            value = "",
                            onValueChange = {},
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.your_question),
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
                            contentAlignment = Alignment.Center,
                        ) {
                            Surface(
                                modifier = modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colors.primary,
                                )
                            ) {
                                Row(
                                    modifier = modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Body",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Black100.copy(0.5f),
                                    )
                                }
                            }
                        }
                    }
                }
            }


            //option A
            Card(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 8.dp, bottom = 8.dp, start = 0.dp, end = 0.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                ),
                elevation = 2.dp,
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        OutlinedTextField(
                            enabled = false,
                            readOnly = true,
                            modifier = modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp)
                                .clip(
                                    RoundedCornerShape(
                                        bottomStart = 16.dp,
                                        bottomEnd = 16.dp,
                                    )
                                ),
                            value = "",
                            onValueChange = {},
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.option_a),
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
                            contentAlignment = Alignment.Center,
                        ) {
                            Surface(
                                modifier = modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(
                                    topStart =
                                    animateDpAsState(
                                        targetValue = if (correctQuestionOption == QuestionOption.OptionA)
                                            16.dp else 0.dp
                                    ).value,
                                    topEnd = animateDpAsState(
                                        targetValue = if (correctQuestionOption == QuestionOption.OptionA)
                                            16.dp else 0.dp
                                    ).value,
                                ),
                                border = BorderStroke(
                                    width = animateDpAsState(
                                        targetValue = if(correctQuestionOption == QuestionOption.OptionA)
                                            2.dp else 1.dp
                                    ).value,
                                    color = MaterialTheme.colors.primary,
                                )
                            ) {
                                Row(
                                    modifier = modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.option_a),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Black100.copy(0.5f),
                                    )

                                    SecondaryButtonWithIconStyled(
                                        label = stringResource(id = R.string.mark_as_answer),
                                        onClick = {
                                            viewModel.onCorrectQuestionOptionChanged(
                                                QuestionOption.OptionA
                                            )
                                        },
                                        selected = correctQuestionOption == QuestionOption.OptionA,
                                    )
                                }
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
                QuestionTypeBottomSheetContent(onSelect = onSelectQuestionType)
            }

            is CreateQuestionBottomSheetMode.Difficulty -> {
                QuestionDifficultyBottomSheetContent(
                    onSelect = onSelectQuestionDifficulty,
                )
            }

            is CreateQuestionBottomSheetMode.Score -> {

            }

            is CreateQuestionBottomSheetMode.Duration -> {

            }
        }

    }
}


@Composable
fun QuestionTypeBottomSheetContent(
    modifier: Modifier = Modifier,
    questionTypes: List<QuestionType> = QuestionType.values().toList(),
    onSelect: (QuestionType) -> Unit,
) {
    Surface(modifier = modifier) {
        Column(modifier = modifier) {
            questionTypes.forEach { questionType ->
                QuestionTypeItem(type = questionType, onSelect = onSelect)
            }
        }
    }
}


@Composable
fun QuestionDifficultyBottomSheetContent(
    modifier: Modifier = Modifier,
    difficulties: List<QuestionDifficulty> = QuestionDifficulty.values().toList(),
    onSelect: (QuestionDifficulty) -> Unit,
) {
    Surface(modifier = modifier) {
        Column(modifier = modifier) {
            difficulties.forEach { difficulty ->
                QuestionDifficultyItem(difficulty = difficulty, onSelect = onSelect)
            }
        }
    }
}


@Composable
fun QuestionDurationBottomSheetContent(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

    }
}

@Composable
fun QuestionScoreBottomSheetContent(
    modifier: Modifier = Modifier
) {

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
    Har,
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
                            }),
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

sealed class QuestionOption {
    object OptionA : QuestionOption()
    object OptionB : QuestionOption()
    object OptionC : QuestionOption()
    object OptionD : QuestionOption()
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

