package com.vanguard.classifiadmin.ui.screens.assessments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.vanguard.classifiadmin.domain.extensions.average
import com.vanguard.classifiadmin.domain.extensions.toPercentage
import com.vanguard.classifiadmin.ui.components.ChartValueItem
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.GradePreviewBar
import com.vanguard.classifiadmin.ui.components.PerformanceCircle
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
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
        subjectName = "Mathematics",
        heading = "Grade 23",
        questions = 23,
        maxScore = 23,
        dateCreated = "12th August",
        fromClass = "Grade 23",
        status = "Pending"
    )
}