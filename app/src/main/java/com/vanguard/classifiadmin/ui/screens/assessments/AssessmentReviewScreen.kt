package com.vanguard.classifiadmin.ui.screens.assessments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.components.ChartValueItem
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.theme.Green100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val ASSESSMENT_REVIEW_SCREEN = "assessment_review_screen"

@Composable
fun AssessmentReviewScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(modifier = modifier,
            topBar = {
                ChildTopBar(
                    onBack = onBack,
                    heading = stringResource(id = R.string.review_questions),
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary,
                )
            },
            content = { padding ->
                AssessmentReviewScreenContent(modifier = modifier.padding(padding))
            })
    }
}

@Composable
fun AssessmentReviewScreenContent(
    modifier: Modifier = Modifier
) {
    val fromClass = "Grade 2"
    val subject = "Islamic Studies"
    val status = "Pending"
    val state = rememberLazyListState()


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = fromClass,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
            )

            RoundedIconButton(
                onClick = { /*TODO*/ },
                icon = R.drawable.icon_add,
            )
        }

        LazyColumn(
            modifier = modifier,
            state = state,
        ) {
            item {
                AssessmentReviewCard(
                    modifier = modifier,
                    subjectName = subject,
                    heading = fromClass,
                    questions = 51,
                    maxScore = 51,
                    dateCreated = "12th August, 2033",
                    fromClass = fromClass,
                    status = status,
                )
            }

        }
    }
}


@Composable
fun AssessmentReviewCard(
    modifier: Modifier = Modifier,
    subjectName: String,
    heading: String,
    questions: Int,
    maxScore: Int,
    dateCreated: String,
    fromClass: String,
    status: String,
) {
    val innerModifier = Modifier
    val constraints = assessmentPerformanceCardConstraints(8.dp)

    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        elevation = 2.dp, shape = RoundedCornerShape(16.dp)
    ) {
        BoxWithConstraints(modifier = modifier) {
            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                constraintSet = constraints,
            ) {

                RoundedIconButton(
                    onClick = {},
                    size = 20.dp,
                    icon = R.drawable.icon_customize,
                    modifier = innerModifier.layoutId("icon")
                )

                Text(
                    text = heading,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = innerModifier
                        .widthIn(max = 180.dp)
                        .layoutId("heading")
                )


                Text(
                    text = subjectName,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary.copy(0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = innerModifier
                        .widthIn(max = 180.dp)
                        .layoutId("subject")
                )


                Divider(
                    modifier = innerModifier
                        .layoutId("divider1")
                        .height(1.dp)
                )




                ChartValueItem(
                    heading = questions.toString(),
                    subtitle = stringResource(id = R.string.questions),
                    modifier = innerModifier.layoutId("questions")
                )



                Divider(
                    modifier = innerModifier
                        .layoutId("divider3")
                        .height(42.dp)
                        .width(1.dp)
                )

                ChartValueItem(
                    heading = maxScore.toString(),
                    subtitle = stringResource(id = R.string.max_score),
                    modifier = innerModifier.layoutId("maxScore")
                )

                Divider(
                    modifier = innerModifier
                        .layoutId("divider2")
                        .height(1.dp)
                )

                AssessmentReviewRowItem(
                    label = stringResource(id = R.string.date_created),
                    value = dateCreated,
                    modifier = innerModifier.layoutId("dateCreated")
                )

                AssessmentReviewRowItem(
                    label = stringResource(id = R.string.from_class),
                    value = fromClass,
                    modifier = innerModifier.layoutId("fromClass")
                )

                AssessmentReviewRowItem(
                    label = stringResource(id = R.string.status),
                    value = status,
                    modifier = innerModifier.layoutId("status")
                )
            }
        }
    }
}


private fun assessmentPerformanceCardConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val heading = createRefFor("heading")
        val subject = createRefFor("subject")
        val divider1 = createRefFor("divider1")
        val dateCreated = createRefFor("dateCreated")
        val fromClass = createRefFor("fromClass")
        val status = createRefFor("status")
        val questions = createRefFor("questions")
        val divider3 = createRefFor("divider3")
        val maxScore = createRefFor("maxScore")
        val divider2 = createRefFor("divider2")

        constrain(icon) {
            start.linkTo(parent.start, margin)
            top.linkTo(heading.top, 0.dp)
            bottom.linkTo(subject.bottom, 0.dp)
        }

        constrain(heading) {
            top.linkTo(parent.top, 12.dp)
            start.linkTo(icon.end, 8.dp)
        }

        constrain(subject) {
            start.linkTo(heading.start, 0.dp)
            top.linkTo(heading.bottom, 2.dp)
        }

        constrain(divider1) {
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(subject.bottom, 12.dp)
            width = Dimension.fillToConstraints
        }

        constrain(questions) {
            top.linkTo(divider1.bottom, 12.dp)
            end.linkTo(divider3.start, 44.dp)
        }

        constrain(divider3) {
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
            top.linkTo(divider1.bottom, 12.dp)
        }

        constrain(maxScore) {
            top.linkTo(divider1.bottom, 12.dp)
            start.linkTo(divider3.end, 44.dp)
        }

        constrain(divider2) {
            top.linkTo(divider3.bottom, 12.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            width = Dimension.fillToConstraints
        }

        constrain(dateCreated) {
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(divider2.bottom, 12.dp)
            width = Dimension.fillToConstraints
        }

        constrain(fromClass) {
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(dateCreated.bottom, 8.dp)
            width = Dimension.fillToConstraints
        }

        constrain(status) {
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(fromClass.bottom, 8.dp)
            width = Dimension.fillToConstraints
        }

    }
}

@Composable
fun AssessmentReviewRowItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colors.primary.copy(0.5f),
            modifier = modifier,
        )


        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = modifier
        )
    }
}

enum class QuestionMode(val fullname: String) {
    MultipleChoice("Multiple Choice"),
    TrueFalse("True/False"),
    ShortAnswer("Short Answer"),
    Essay("Essay"),
}

enum class QuestionDifficulty {
    Easy,
    Medium,
    Har,
}

@Composable
fun TextIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    size: Dp = 24.dp,
    icon: Int,
    tint: Color = MaterialTheme.colors.primary,
) {
    Surface(
        modifier = modifier.clip(CircleShape),
        shape = CircleShape,
        color = tint.copy(0.1f)
    ) {
        TextButton(onClick = onClick, modifier = modifier.clip(CircleShape)) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.add),
                modifier = modifier.size(size),
                tint = tint,
            )
        }
    }
}


@Composable
fun QuestionItem(
    modifier: Modifier = Modifier,
) {
    val innerModifier = Modifier
    val constraints = questionItemConstraints(8.dp)

    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        elevation = 2.dp, shape = RoundedCornerShape(16.dp)
    ) {
        BoxWithConstraints(modifier = modifier) {
            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                constraintSet = constraints,
            ) {

            }
        }
    }
}

private fun questionItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val number = createRefFor("number")
        val question = createRefFor("question")
        val expandButton = createRefFor("expandButton")
        val subjectRow = createRefFor("subjectRow")
        val mode = createRefFor("mode")
        val answer = createRefFor("answer")
        val remove = createRefFor("remove")

        constrain(number) {
            start.linkTo(parent.start, margin)
            top.linkTo(question.top, 0.dp)
        }

        constrain(expandButton) {
            top.linkTo(question.top, 0.dp)
            end.linkTo(parent.end, margin)
        }

        constrain(subjectRow) {
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
            top.linkTo(subjectRow.bottom, 16.dp)
            width = Dimension.fillToConstraints
        }

        constrain(mode) {
            start.linkTo(parent.start, 8.dp)
            end.linkTo(parent.end, 8.dp)
            top.linkTo(subjectRow.bottom, 16.dp)
            width = Dimension.fillToConstraints
        }

        constrain(answer) {
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
            top.linkTo(mode.bottom, 4.dp)
            width = Dimension.fillToConstraints
        }

        constrain(remove) {
            start.linkTo(parent.start, 16.dp)
            top.linkTo(answer.bottom, 16.dp)
        }
    }
}

@Composable
fun MultipleChoiceAnswerCard(
    modifier: Modifier = Modifier,
    choices: ArrayList<String>,
    chars: List<Char> = listOf('a','b','c','d'),
    selectedChar: Char,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        repeat(chars.size) {index ->
            Row(
                modifier = modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                RoundedCharButton(
                    onClick = { /*TODO*/ },
                    char = chars[index],
                    selected = selectedChar == chars[index],
                    modifier = modifier,
                )

                Text(
                    text = choices[index],
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = modifier
                        .weight(1f)
                        .padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun TrueFalseAnswerCard(
    modifier: Modifier = Modifier,
    choices: ArrayList<String> = arrayListOf("True","False"),
    chars: List<Char> = listOf('a','b'),
    selectedChar: Char,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        repeat(chars.size) {index ->
            Row(
                modifier = modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                RoundedCharButton(
                    onClick = { /*TODO*/ },
                    char = chars[index],
                    selected = selectedChar == chars[index],
                    modifier = modifier,
                )

                Text(
                    text = choices[index],
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = modifier
                        .weight(1f)
                        .padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun ShortAnswerCard(
    modifier: Modifier = Modifier,
    answer: String,
) {
    Surface(modifier = modifier.fillMaxWidth()) {
        Text(
            text = answer,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
        )
    }
}


@Composable
fun RoundedCharButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    size: Dp = 24.dp,
    char: Char,
    selected: Boolean = false,
    color: Color = MaterialTheme.colors.primary,
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .padding(8.dp),
        shape = CircleShape,
        color = if(selected) Green100 else color.copy(0.1f)
    ) {
        TextButton(onClick = onClick,
            modifier = modifier.clip(CircleShape).size(32.dp),
            content = {
           if(selected) {
               Icon(
                   painter = painterResource(id = R.drawable.icon_tick),
                   tint = color,
                   contentDescription = char.toString(),
                   modifier = Modifier.size(size)
               )
           } else {
               Text(
                   text = char.toString(),
                   fontSize = 12.sp,
                   color = color,
                   modifier = Modifier
               )
           }
        })
    }
}

val mode: QuestionMode = QuestionMode.Essay
val questionModeName = mode.toString()


@Composable
@Preview
private fun AssessmentReviewRowItemPreview() {
    AssessmentReviewRowItem(
        label = "Date Created",
        value = "15th July, 2027"
    )
}

@Composable
@Preview
fun AssessmentReviewCardPreview() {
    AssessmentReviewCard(
        subjectName = questionModeName.substring(63),
        heading = "Grade 23",
        questions = 23,
        maxScore = 23,
        dateCreated = "12th August",
        fromClass = "Grade 23",
        status = "Pending"
    )
}

@Composable
@Preview
private fun RoundedCharButtonPreview() {
    RoundedCharButton(
        onClick = {},
        char = 'a',
        selected = true,
    )
}

@Composable
@Preview
private fun MultipleChoiceAnswerCardPreview() {
    MultipleChoiceAnswerCard(
        choices = arrayListOf(
            "Lion", "Tiger", "Giraffe", "Cat"
        ),
        selectedChar = 'b',
    )
}


@Composable
@Preview
private fun ShortAnswerCardPreview() {
    ShortAnswerCard(
        answer = "The cat ate the mice in the new building"
    )
}