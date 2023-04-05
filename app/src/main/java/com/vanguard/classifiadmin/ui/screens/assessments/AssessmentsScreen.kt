package com.vanguard.classifiadmin.ui.screens.assessments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.AssessmentModel
import com.vanguard.classifiadmin.domain.helpers.UserRole
import com.vanguard.classifiadmin.domain.helpers.generateColorFromAssessment
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.screens.assessments.items.DraftAssessmentOptionsListItem
import com.vanguard.classifiadmin.ui.screens.assessments.items.InReviewAssessmentOptionsListItem
import com.vanguard.classifiadmin.ui.screens.assessments.items.PublishedAssessmentOptionsListItem
import com.vanguard.classifiadmin.ui.screens.assessments.states.AssessmentsScreenContentDraft
import com.vanguard.classifiadmin.ui.screens.assessments.states.AssessmentsScreenContentInReview
import com.vanguard.classifiadmin.ui.screens.assessments.states.AssessmentsScreenContentPublished
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay

const val ASSESSMENT_SCREEN = "assessment_screen"

@Composable
fun AssessmentsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onPublishedAssessmentOptions: (AssessmentModel) -> Unit,
    onInReviewAssessmentOptions: (AssessmentModel) -> Unit,
    onDraftAssessmentOptions: (AssessmentModel) -> Unit,
    onSelectAssessment: (AssessmentModel) -> Unit,
    onCreateQuestions: () -> Unit,
) {
    AssessmentsScreenContent(
        viewModel = viewModel,
        modifier = modifier,
        onPublishedAssessmentOptions = onPublishedAssessmentOptions,
        onSelectAssessment = onSelectAssessment,
        onInReviewAssessmentOptions = onInReviewAssessmentOptions,
        onDraftAssessmentOptions = onDraftAssessmentOptions,
        onCreateQuestions = onCreateQuestions
    )
}


@Composable
fun AssessmentsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onPublishedAssessmentOptions: (AssessmentModel) -> Unit,
    onInReviewAssessmentOptions: (AssessmentModel) -> Unit,
    onDraftAssessmentOptions: (AssessmentModel) -> Unit,
    onSelectAssessment: (AssessmentModel) -> Unit,
    onCreateQuestions: () -> Unit,
) {
    val innerModifier = Modifier
    val currentAssessmentOption by viewModel.currentAssessmentOption.collectAsState()
    val currentUserRolePref by viewModel.currentUserRolePref.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedAssessmentsPublishedNetwork by viewModel.verifiedAssessmentsPublishedNetwork.collectAsState()
    val verifiedAssessmentsInReviewNetwork by viewModel.verifiedAssessmentsInReviewNetwork.collectAsState()
    val verifiedAssessmentsDraftNetwork by viewModel.verifiedAssessmentsDraftNetwork.collectAsState()
    val currentClassFeedPref by viewModel.currentClassFeedPref.collectAsState()


    LaunchedEffect(Unit, currentClassFeedPref, currentUserRolePref) {
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentUserRolePref()
        viewModel.getCurrentClassFeedPref()
        delay(1000)

        when (currentUserRolePref) {
            UserRole.Teacher.name -> {
                //get only verified assessments for class by published, in-review, and draft
                viewModel.getVerifiedAssessmentsDraftForClassNetwork(
                    currentClassFeedPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
                viewModel.getVerifiedAssessmentsInReviewForClassNetwork(
                    currentClassFeedPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
                viewModel.getVerifiedAssessmentsPublishedForClassNetwork(
                    currentClassFeedPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
            }

            UserRole.Parent.name -> {
                //get only verified assessments for class by published, in-review, and draft
                viewModel.getVerifiedAssessmentsDraftForClassNetwork(
                    currentClassFeedPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
                viewModel.getVerifiedAssessmentsInReviewForClassNetwork(
                    currentClassFeedPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
                viewModel.getVerifiedAssessmentsPublishedForClassNetwork(
                    currentClassFeedPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
            }

            UserRole.Student.name -> {
                //get only verified assessments for class by published, in-review, and draft
                viewModel.getVerifiedAssessmentsDraftForClassNetwork(
                    currentClassFeedPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
                viewModel.getVerifiedAssessmentsInReviewForClassNetwork(
                    currentClassFeedPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
                viewModel.getVerifiedAssessmentsPublishedForClassNetwork(
                    currentClassFeedPref.orEmpty(),
                    currentSchoolIdPref.orEmpty()
                )
            }

            UserRole.Admin.name -> {
                //get all verified assessments by published, in-review, and draft
                viewModel.getVerifiedAssessmentsDraftNetwork(currentSchoolIdPref.orEmpty())
                viewModel.getVerifiedAssessmentsPublishedNetwork(currentSchoolIdPref.orEmpty())
                viewModel.getVerifiedAssessmentsInReviewNetwork(currentSchoolIdPref.orEmpty())
            }

            UserRole.SuperAdmin.name -> {
                //get all verified assessments by published, in-review, and draft
                viewModel.getVerifiedAssessmentsDraftNetwork(currentSchoolIdPref.orEmpty())
                viewModel.getVerifiedAssessmentsPublishedNetwork(currentSchoolIdPref.orEmpty())
                viewModel.getVerifiedAssessmentsInReviewNetwork(currentSchoolIdPref.orEmpty())
            }
        }
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier) {
            val maxHeight = maxHeight

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

                    if (currentUserRolePref != null &&
                        currentUserRolePref != UserRole.Student.name &&
                        currentUserRolePref != UserRole.Parent.name
                    ) {
                        RoundedIconButton(
                            onClick = {
                                viewModel.onAssessmentCreationOpenModeChanged(
                                    AssessmentCreationOpenMode.Creator
                                )
                                onCreateQuestions()
                            },
                            icon = R.drawable.icon_add,
                        )
                    }
                }

                AssessmentSelector(
                    modifier = innerModifier,
                    viewModel = viewModel,
                )

                when (currentAssessmentOption) {
                    AssessmentState.Published -> {
                        AssessmentsScreenContentPublished(
                            viewModel = viewModel,
                            onPublishedAssessmentOptions = onPublishedAssessmentOptions,
                            onSelectAssessment = onSelectAssessment,
                            maxHeight = maxHeight
                        )
                    }

                    AssessmentState.InReview -> {
                        AssessmentsScreenContentInReview(
                            viewModel = viewModel,
                            onInReviewAssessmentOptions = onInReviewAssessmentOptions,
                            onSelectAssessment = onSelectAssessment,
                            maxHeight = maxHeight
                        )
                    }

                    AssessmentState.Draft -> {
                        AssessmentsScreenContentDraft(
                            viewModel = viewModel,
                            onDraftAssessmentOptions = onDraftAssessmentOptions,
                            onSelectAssessment = onSelectAssessment,
                            maxHeight = maxHeight,
                        )
                    }

                    else -> {
                        AssessmentsScreenContentPublished(
                            viewModel = viewModel,
                            onPublishedAssessmentOptions = onPublishedAssessmentOptions,
                            onSelectAssessment = onSelectAssessment,
                            maxHeight = maxHeight
                        )
                    }
                }
            }
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
fun AssessmentSubjectRow(
    modifier: Modifier = Modifier,
    assessment: AssessmentModel,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = assessment.subjectName.orEmpty(),
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
            text = assessment.type.orEmpty(),
            fontSize = 12.sp,
            color = Color(generateColorFromAssessment(assessment))
        )
    }
}


@Composable
fun AssessmentSelector(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    options: List<AssessmentState> = AssessmentState.values().toList(),
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
                AssessmentStateButton(
                    onSelect = viewModel::onCurrentAssessmentOptionChanged,
                    option = each,
                    selected = each == (currentAssessmentOption ?: AssessmentState.Published)
                )
            }
        }
    }
}


@Composable
fun AssessmentStateButton(
    modifier: Modifier = Modifier,
    onSelect: (AssessmentState) -> Unit,
    option: AssessmentState,
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

enum class AssessmentType(val title: String) {
    HomeWork("HomeWork"),
    Quiz("Class Quiz"),
    Exam("Exam")
}

enum class AssessmentState(val title: String) {
    Published("Published"),
    InReview("In Review"),
    Draft("Draft")
}


@Composable
fun PublishedAssessmentBottomSheetContent(
    modifier: Modifier = Modifier,
    publishedAssessmentOptions: List<PublishedAssessmentBottomSheetOption> = PublishedAssessmentBottomSheetOption.values()
        .toList(),
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
