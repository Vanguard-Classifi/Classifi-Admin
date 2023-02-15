package com.vanguard.classifiadmin.ui.screens.assessments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import androidx.constraintlayout.compose.Dimension
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
    viewModel: MainViewModel,
    onPublishedAssessmentOptions: (Assessment) -> Unit,
    onInReviewAssessmentOptions: (Assessment) -> Unit,
    onDraftAssessmentOptions: (Assessment) -> Unit,
    onSelectAssessment: (Assessment) -> Unit,
) {
    AssessmentsScreenContent(
        viewModel = viewModel,
        modifier = modifier,
        onPublishedAssessmentOptions = onPublishedAssessmentOptions,
        onSelectAssessment = onSelectAssessment,
        onInReviewAssessmentOptions = onInReviewAssessmentOptions,
        onDraftAssessmentOptions = onDraftAssessmentOptions,
    )
}


@Composable
fun AssessmentsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onPublishedAssessmentOptions: (Assessment) -> Unit,
    onInReviewAssessmentOptions: (Assessment) -> Unit,
    onDraftAssessmentOptions: (Assessment) -> Unit,
    onSelectAssessment: (Assessment) -> Unit,
) {
    val verticalScroll = rememberLazyListState()
    val innerModifier = Modifier
    val currentAssessmentOption by viewModel.currentAssessmentOption.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                modifier = innerModifier,
                viewModel = viewModel,
            )

            when (currentAssessmentOption) {
                AssessmentOption.Published -> {
                    AssessmentsScreenContentPublished(
                        viewModel = viewModel,
                        verticalScroll = verticalScroll,
                        onPublishedAssessmentOptions = onPublishedAssessmentOptions,
                        onSelectAssessment = onSelectAssessment,
                    )
                }

                AssessmentOption.InReview -> {
                    AssessmentsScreenContentInReview(
                        viewModel = viewModel,
                        verticalScroll = verticalScroll,
                        onInReviewAssessmentOptions = onInReviewAssessmentOptions,
                        onSelectAssessment = onSelectAssessment,
                    )
                }

                AssessmentOption.Draft -> {
                    AssessmentsScreenContentDraft(
                        viewModel = viewModel,
                        verticalScroll = verticalScroll,
                        onDraftAssessmentOptions = onDraftAssessmentOptions,
                        onSelectAssessment = onSelectAssessment,
                    )
                }

                else -> {
                    AssessmentsScreenContentPublished(
                        viewModel = viewModel,
                        verticalScroll = verticalScroll,
                        onPublishedAssessmentOptions = onPublishedAssessmentOptions,
                        onSelectAssessment = onSelectAssessment,
                    )
                }
            }
        }
    }
}


@Composable
fun AssessmentsScreenContentPublished(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    verticalScroll: LazyListState,
    onPublishedAssessmentOptions: (Assessment) -> Unit,
    onSelectAssessment: (Assessment) -> Unit,
) {
    val items = listOf<Assessment>(
        Assessment(
            title = "Chemistry Exam For SS 1",
            subject = "Chemistry",
            type = AssessmentState.Exam.name,
            expired = true,
            reviewing = false,
            date = "Feb",
            attempted = true
        ),
        Assessment(
            title = "Chemistry Exam For SS 1",
            subject = "Chemistry",
            type = AssessmentState.Exam.name,
            expired = false,
            reviewing = false,
            date = "Feb",
            attempted = true
        ),
        Assessment(
            title = "Chemistry Exam For SS 1",
            subject = "Chemistry",
            type = AssessmentState.Exam.name,
            expired = true,
            reviewing = false,
            date = "Feb",
            attempted = false
        ),
    )
    LazyColumn(
        modifier = Modifier
            .padding(bottom = 72.dp),
        state = verticalScroll,
    ) {
        items(items) { each ->
            AssessmentItem(
                assessment = each,
                onOptions = onPublishedAssessmentOptions,
                onSelectAssessment = onSelectAssessment,
            )
        }
    }
}

@Composable
fun AssessmentsScreenContentInReview(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    verticalScroll: LazyListState,
    onInReviewAssessmentOptions: (Assessment) -> Unit,
    onSelectAssessment: (Assessment) -> Unit,
) {
    val items = listOf<Assessment>(
        Assessment(
            title = "Chemistry Exam For SS 1",
            subject = "Chemistry",
            type = AssessmentState.Exam.name,
            expired = false,
            reviewing = true,
            date = "Feb",
            attempted = false
        ),
        Assessment(
            title = "Chemistry Exam For SS 1",
            subject = "Chemistry",
            type = AssessmentState.Exam.name,
            expired = false,
            reviewing = true,
            date = "Feb",
            attempted = false
        ),
        Assessment(
            title = "Chemistry Exam For SS 1",
            subject = "Chemistry",
            type = AssessmentState.Exam.name,
            expired = false,
            reviewing = true,
            date = "Feb",
            attempted = false
        ),
    )
    LazyColumn(
        modifier = Modifier
            .padding(bottom = 72.dp),
        state = verticalScroll,
    ) {
        items(items) { each ->
            AssessmentItem(
                assessment = each,
                onOptions = onInReviewAssessmentOptions,
                onSelectAssessment = onSelectAssessment,
            )
        }
    }
}

@Composable
fun AssessmentsScreenContentDraft(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    verticalScroll: LazyListState,
    onDraftAssessmentOptions: (Assessment) -> Unit,
    onSelectAssessment: (Assessment) -> Unit,
) {
    val items = listOf<Assessment>(
        Assessment(
            title = "Chemistry Exam For SS 1",
            subject = "Chemistry",
            type = AssessmentState.Exam.name,
            expired = false,
            reviewing = false,
            date = "Feb",
            attempted = false
        ),
        Assessment(
            title = "Chemistry Exam For SS 1",
            subject = "Chemistry",
            type = AssessmentState.Exam.name,
            expired = false,
            reviewing = false,
            date = "Feb",
            attempted = false
        ),
        Assessment(
            title = "Chemistry Exam For SS 1",
            subject = "Chemistry",
            type = AssessmentState.Exam.name,
            expired = false,
            reviewing = false,
            date = "Feb",
            attempted = false
        ),
    )
    LazyColumn(
        modifier = Modifier
            .padding(bottom = 72.dp),
        state = verticalScroll,
    ) {
        items(items) { each ->
            AssessmentItem(
                assessment = each,
                onOptions = onDraftAssessmentOptions,
                onSelectAssessment = onSelectAssessment,
            )
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
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable(onClick = { onSelectAssessment(assessment) }),
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
                    .widthIn(max = 200.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (assessment.expired) {
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


            RoundedIconButton(
                modifier = innerModifier.layoutId("extras"),
                onClick = { onOptions(assessment) },
                icon = R.drawable.icon_options_horizontal,
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
            height = Dimension.fillToConstraints
        }

        constrain(dateIcon) {
            start.linkTo(attemptStatus.end, margin = 4.dp)
            top.linkTo(title.top, margin = 0.dp)
            bottom.linkTo(subjectAndType.bottom, margin = 0.dp)
        }

        constrain(title) {
            top.linkTo(parent.top, margin = 4.dp)
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
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                maxLines = 1,
                overflow = TextOverflow.Clip
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
            modifier = modifier
                .wrapContentHeight()
                .padding(horizontal = 8.dp),
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


@Composable
fun PublishedAssessmentBottomSheetContent(
    modifier: Modifier = Modifier,
    publishedAssessmentOptions: List<PublishedAssessmentBottomSheetOption> = PublishedAssessmentBottomSheetOption.values().toList(),
    onSelectOption: (PublishedAssessmentBottomSheetOption) -> Unit,
    ) {
    LazyColumn(modifier = modifier.padding(bottom = 16.dp), state = rememberLazyListState()) {
        items(publishedAssessmentOptions) { each ->
           PublishedAssessmentOptionsListItem(
               publishedAssessmentOption = each,
               onSelect = onSelectOption
           )
        }
    }
}

@Composable
fun InReviewAssessmentBottomSheetContent(
    modifier: Modifier = Modifier,
    inReviewAssessmentOptions: List<InReviewAssessmentBottomSheetOption> = InReviewAssessmentBottomSheetOption.values()
        .toList(),
    onSelectOption: (InReviewAssessmentBottomSheetOption) -> Unit,
) {
    LazyColumn(modifier = modifier.padding(bottom = 16.dp), state = rememberLazyListState()) {
        items(inReviewAssessmentOptions) { each ->
            InReviewAssessmentOptionsListItem(
                inReviewAssessmentOption = each,
                onSelect = onSelectOption
            )
        }
    }
}

@Composable
fun DraftAssessmentBottomSheetContent(
    modifier: Modifier = Modifier,
    draftAssessmentOptions: List<DraftAssessmentBottomSheetOption> = DraftAssessmentBottomSheetOption.values()
        .toList(),
    onSelectOption: (DraftAssessmentBottomSheetOption) -> Unit,
) {
    LazyColumn(modifier = modifier.padding(bottom = 16.dp), state = rememberLazyListState()) {
        items(draftAssessmentOptions) { each ->
            DraftAssessmentOptionsListItem(
                draftAssessmentOption = each,
                onSelect = onSelectOption,
            )
        }
    }
}


@Composable
fun DraftAssessmentOptionsListItem(
    modifier: Modifier = Modifier,
    draftAssessmentOption: DraftAssessmentBottomSheetOption,
    onSelect: (DraftAssessmentBottomSheetOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .clickable { onSelect(draftAssessmentOption) }
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = draftAssessmentOption.icon),
                contentDescription = draftAssessmentOption.label,
                tint = MaterialTheme.colors.primary,
                modifier = modifier
                    .size(24.dp)
                    .padding(2.dp)
            )

            Text(
                text = draftAssessmentOption.label,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun InReviewAssessmentOptionsListItem(
    modifier: Modifier = Modifier,
    inReviewAssessmentOption: InReviewAssessmentBottomSheetOption,
    onSelect: (InReviewAssessmentBottomSheetOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .clickable { onSelect(inReviewAssessmentOption) }
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = inReviewAssessmentOption.icon),
                contentDescription = inReviewAssessmentOption.label,
                tint = MaterialTheme.colors.primary,
                modifier = modifier
                    .size(24.dp)
                    .padding(2.dp)
            )

            Text(
                text = inReviewAssessmentOption.label,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun PublishedAssessmentOptionsListItem(
    modifier: Modifier = Modifier,
    publishedAssessmentOption: PublishedAssessmentBottomSheetOption,
    onSelect: (PublishedAssessmentBottomSheetOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .clickable { onSelect(publishedAssessmentOption) }
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = publishedAssessmentOption.icon),
                contentDescription = publishedAssessmentOption.label,
                tint = MaterialTheme.colors.primary,
                modifier = modifier
                    .size(24.dp)
                    .padding(2.dp)
            )

            Text(
                text = publishedAssessmentOption.label,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


enum class PublishedAssessmentBottomSheetOption(val label: String, val icon: Int) {
    ViewReport("View Report", R.drawable.icon_preview),
    RestartAssessment("Restart Assessment", R.drawable.icon_repeat),
}

enum class InReviewAssessmentBottomSheetOption(val label: String, val icon: Int) {
    Edit("Edit", R.drawable.icon_edit)
}

enum class DraftAssessmentBottomSheetOption(val label: String, val icon: Int) {
    AddQuestions("Add Questions", R.drawable.icon_add)
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

@Preview
@Composable
private fun AssessmentItemPreview() {
    AssessmentItem(
        assessment = Assessment(
            title = "Year 11 Examination",
            subject = "Mathematics",
            type = AssessmentState.Quiz.name,
            expired = true,
            reviewing = false,
            date = "Aug",
            attempted = false,
        ),
        onOptions = {},
        onSelectAssessment = {}
    )
}