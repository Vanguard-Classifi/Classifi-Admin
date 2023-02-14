package com.vanguard.classifiadmin.ui.screens.assessments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.generateColorFromAssessment
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.ui.theme.Green100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val ASSESSMENT_SCREEN = "assessment_screen"

@Composable
fun AssessmentsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Wake up Assessment screen")
        }
    }
}


@Composable
fun AssessmentsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val verticalScroll = rememberLazyListState()
    val innerModifier = Modifier

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = innerModifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(id = R.string.assessments),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                )

                RoundedIconButton(
                    onClick = { /*TODO*/ },
                    icon = R.drawable.icon_add,
                )
            }

            AssessmentSelector(
                modifier = modifier,
                viewModel = viewModel,
            )

            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 72.dp),
                state = verticalScroll,
            ) {

            }
        }
    }
}


@Composable
fun AssessmentItem(
    modifier: Modifier = Modifier,
    assessment: Assessment,
    onOptions: (Assessment) -> Unit,
    onSelectAssessment: (Assessment) -> Unit,
) {
    val constraints = assessmentItemConstraints(8.dp)
    val innerModifier = Modifier

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.1f)
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            Box(
                modifier = innerModifier
                    .layoutId("attemptStatus")
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(
                        color = if (assessment.attempted) Green100 else Black100.copy(0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
            )

            DateIcon(
                title = assessment.date,
                subtitle = assessment.date,
                modifier = innerModifier.layoutId("dateIcon")
            )

            Text(
                text = assessment.title.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                modifier = innerModifier
                    .layoutId("title")
                    .widthIn(max = 120.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if(assessment.expired) {
                Text(
                    text = stringResource(id = R.string.closed).uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.error,
                    modifier = innerModifier
                        .layoutId("expiry")
                )
            }

            AssessmentSubjectType(
                assessment = assessment,
                modifier = innerModifier.layoutId("subjectAndType")
            )



        }
    }
}

private fun assessmentItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val dateIcon = createRefFor("dateIcon")
        val attemptStatus = createRefFor("attemptStatus")
        val title = createRefFor("title")
        val expiry = createRefFor("expiry")
        val subjectAndType = createRefFor("subjectAndType")
        val extras = createRefFor("extras")

        constrain(attemptStatus) {
            top.linkTo(parent.top, margin = 0.dp)
            bottom.linkTo(parent.bottom, margin = 0.dp)
            start.linkTo(parent.start, margin = 0.dp)
        }

        constrain(dateIcon) {
            start.linkTo(attemptStatus.end, margin = 4.dp)
            top.linkTo(parent.top, margin = 0.dp)
            bottom.linkTo(parent.bottom, margin = 0.dp)
        }

        constrain(title) {
            top.linkTo(parent.top, margin = 8.dp)
            start.linkTo(dateIcon.end, margin = 8.dp)
        }

        constrain(expiry) {
            start.linkTo(title.start, margin = 0.dp)
            top.linkTo(title.bottom, margin = 4.dp)
        }

        constrain(subjectAndType) {
            start.linkTo(title.start, margin = 0.dp)
            top.linkTo(expiry.bottom, margin = 4.dp)
        }

        constrain(extras) {
            top.linkTo(parent.top, margin = 0.dp)
            bottom.linkTo(parent.bottom, margin = 0.dp)
            end.linkTo(parent.end, margin = 4.dp)
        }
    }
}

@Composable 
fun DateIcon(
    modifier: Modifier = Modifier,
    surfaceSize: Dp = 48.dp,
    title: String,
    subtitle: String,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colors.primary.copy(0.3f)
    ) {
        Column(
            modifier = modifier
                .size(surfaceSize)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary
            )
        }
    }
}


@Composable
fun AssessmentSubjectType(
    modifier: Modifier = Modifier,
    assessment: Assessment,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = assessment.subject,
            modifier = modifier.widthIn(max = 120.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            color = Black100.copy(0.5f)
        )

        Box(
            modifier = modifier
                .size(3.dp)
                .background(color = Black100.copy(0.5f), shape = CircleShape)
        )

        Text(
            text = assessment.type,
            fontSize = 12.sp,
            color = Color(generateColorFromAssessment(assessment))
        )
    }
}

@Composable
fun AssessmentSelector(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    options: List<AssessmentOption> = AssessmentOption.values().toList(),
) {
    val currentAssessmentOption by viewModel.currentAssessmentOption.collectAsState()

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
            modifier = modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEach { each ->
                AssessmentOptionButton(
                    onSelect = viewModel::onCurrentAssessmentOptionChanged,
                    option = each,
                    selected = each == (currentAssessmentOption ?: AssessmentOption.Published)
                )
            }
        }
    }
}


@Composable
fun AssessmentOptionButton(
    modifier: Modifier = Modifier,
    onSelect: (AssessmentOption) -> Unit,
    option: AssessmentOption,
    selected: Boolean = false,
) {
    TextButton(
        onClick = { onSelect(option) },
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
            text = option.title,
            fontSize = 12.sp,
            color = if (selected) MaterialTheme.colors.primary else Black100.copy(0.3f),
        )
    }
}


enum class AssessmentOption(val title: String) {
    Published("Published"),
    InReview("In Review"),
    Draft("Draft")
}


data class Assessment(
    val title: String,
    val subject: String,
    val type: String,
    val expired: Boolean,
    val reviewing: Boolean,
    val date: String,
    val attempted: Boolean,
)

enum class AssessmentState {
    Quiz, Test, Exam
}


@Preview
@Composable
private fun AssessmentSubjectTypePreview() {
    AssessmentSubjectType(
        assessment = Assessment(
            title = "",
            subject = "Mathematics Education Knowledge Subject",
            type = AssessmentState.Quiz.name,
            expired = false,
            reviewing = false,
            date = "",
            attempted = false
        )
    )
}

@Preview
@Composable
private fun DateIconPreview() {
    DateIcon(
        title = "02",
        subtitle = "Aug"
    )
}