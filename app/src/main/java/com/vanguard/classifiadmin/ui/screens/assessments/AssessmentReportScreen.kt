package com.vanguard.classifiadmin.ui.screens.assessments


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.extensions.average
import com.vanguard.classifiadmin.domain.extensions.customTabIndicatorOffset
import com.vanguard.classifiadmin.domain.extensions.toGrade
import com.vanguard.classifiadmin.domain.extensions.toPercentage
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.domain.helpers.generateColorFromGrade
import com.vanguard.classifiadmin.ui.components.ChartValueItem
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.GradePreviewBar
import com.vanguard.classifiadmin.ui.components.PerformanceCircle
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatar
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val ASSESSMENT_REPORT_SCREEN = "assessment_report_screen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AssessmentReportScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    val scores = listOf<Float>(
        0.5f,
        0.5f,
        0.5f,
        0.5f,
        0.05f,
        0.5f,
        0.5f,
        0.5f,
        0.5f,
        0.05f,
        0.5f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.05f,
        0.5f,
        0.5f,
        0.5f,
        0.5f,
        0.5f,
        0.5f,
        0.5f,
        0.5f,
        0.95f,
        0.5f,
        0.75f,
        0.5f,
        0.75f,
        0.55f,
        0.59f,
        0.95f,
        0.95f,
        0.95f,
        0.95f,
        0.5f,
    )
    val tests = listOf(0.5f, 0.9f, 0.46f, 0.34f)
    val participants = listOf(
        Participant(
            order = 1,
            studentName = "Musuf Musa",
            date = "23rd June, 2023",
            score = 0.67f,
        ),
        Participant(
            order = 2,
            studentName = "Musuf Musa",
            date = "23rd June, 2023",
            score = 0.67f,
        ),
        Participant(
            order = 3,
            studentName = "Musuf Musa",
            date = "23rd June, 2023",
            score = 0.67f,
        ),
        Participant(
            order = 4,
            studentName = "Yusuf Musa",
            date = "23rd June, 2023",
            score = 0.67f,
        ),
        Participant(
            order = 5,
            studentName = "Yusuf Musa",
            date = "23rd June, 2023",
            score = 0.67f,
        ),
        Participant(
            order = 6,
            studentName = "Yusuf Musa",
            date = "23rd June, 2023",
            score = 0.67f,
        ),
        Participant(
            order = 7,
            studentName = "Yusuf Musa",
            date = "23rd June, 2023",
            score = 0.67f,
        ),
    )

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(modifier = modifier) {
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
                AssessmentReportBottomSheetContent(
                    onSelectParticipantOption = {
                        //todo clear submission
                        //hide bottom sheet
                        coroutineScope.launch {
                            showModalSheet.value = false
                            delay(500)
                            sheetState.hide()
                        }
                    },
                )
            }
        ) {
            Scaffold(modifier = modifier,
                topBar = {
                    ChildTopBar(
                        onBack = onBack,
                        elevation = 0.dp,
                        heading = "KG 2 CBT EXAMINATION THIRD TERM",
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                    )
                },
                content = { padding ->
                    AssessmentReportScreenContent(
                        viewModel = viewModel,
                        modifier = modifier.padding(padding),
                        maxWidth = maxWidth,
                        maxHeight = maxHeight,
                        values = tests,
                        onOptions = {
                            //show bottom sheet
                            coroutineScope.launch {
                                showModalSheet.value = true
                                sheetState.show()
                            }
                        },
                        participants = participants,
                    )
                }
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun AssessmentReportScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    maxHeight: Dp,
    maxWidth: Dp,
    values: List<Float>,
    participants: List<Participant>,
    onOptions: (String) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0)

    Column {
        AssessmentReportScreenContentHeader(
            modifier = modifier,
            pagerState = pagerState,
        )
        AssessmentReportScreenContentBody(
            modifier = modifier,
            pagerState = pagerState,
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            values = values,
            onOptions = onOptions,
            participants = participants,
        )
    }
}

@Composable
fun AssessmentReportBottomSheetContent(
    modifier: Modifier = Modifier,
    participantOptions: List<AssessmentReportParticipantOption> =
        AssessmentReportParticipantOption.values().toList(),
    onSelectParticipantOption: (AssessmentReportParticipantOption) -> Unit,
) {
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

        LazyColumn(modifier = modifier.padding(bottom = 16.dp), state = rememberLazyListState()) {
            items(participantOptions) { each ->
                AssessmentReportParticipantOptionListItem(
                    option = each,
                    onSelect = onSelectParticipantOption
                )
            }
        }
    }
}

@Composable
fun AssessmentReportParticipantOptionListItem(
    modifier: Modifier = Modifier,
    option: AssessmentReportParticipantOption,
    onSelect: (AssessmentReportParticipantOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .clickable { onSelect(option) }
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
                painter = painterResource(id = option.icon),
                contentDescription = option.label,
                tint = MaterialTheme.colors.primary,
                modifier = modifier
                    .size(24.dp)
                    .padding(2.dp)
            )

            Text(
                text = option.label,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

enum class AssessmentReportParticipantOption(val label: String, val icon: Int) {
    ClearSubmission("Clear Submission", R.drawable.icon_close)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AssessmentReportScreenContentHeader(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    pages: List<AssessmentReportPage> = AssessmentReportPage.values().toList(),
) {
    val TAG = "AssessmentReportScreenContentHeader"
    val scope = rememberCoroutineScope()
    var currentPage by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(pages.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    LaunchedEffect(Unit) {
        pagerState.animateScrollToPage(currentPage)
    }

    ScrollableTabRow(
        selectedTabIndex = currentPage,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier,
        edgePadding = 0.dp,
        contentColor = MaterialTheme.colors.onPrimary,
        indicator = { positions ->
            TabRowDefaults.Indicator(
                color = MaterialTheme.colors.onPrimary,
                height = 3.dp,
                modifier = Modifier.customTabIndicatorOffset(
                    currentTabPosition = positions[currentPage],
                    tabWidth = tabWidths[currentPage]
                )
            )
        }
    ) {
        pages.forEachIndexed { index, tab ->
            Tab(
                selected = currentPage == index,
                unselectedContentColor = MaterialTheme.colors.onPrimary.copy(0.5f),
                text = {
                    Text(
                        text = tab.name,
                        color = if (currentPage == index) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(
                            0.5f
                        ),
                        onTextLayout = { result ->
                            tabWidths[index] = with(density) { result.size.width.toDp() }
                        },
                        maxLines = 1,
                        fontSize = 12.sp,
                        fontWeight = if (currentPage == index) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                selectedContentColor = MaterialTheme.colors.onPrimary,
                onClick = {
                    currentPage = index
                    scope.launch { pagerState.animateScrollToPage(index) }
                }
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun AssessmentReportScreenContentBody(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    pages: List<AssessmentReportPage> = AssessmentReportPage.values().toList(),
    maxHeight: Dp,
    maxWidth: Dp,
    values: List<Float>,
    participants: List<Participant>,
    onOptions: (String) -> Unit,
) {
    HorizontalPager(state = pagerState, count = pages.size, userScrollEnabled = false) { page ->
        when (page) {
            0 -> AssessmentReportScreenContentSummary(
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                values = values
            )

            1 -> AssessmentReportScreenContentParticipants(
                participants = participants,
                onOptions = onOptions,
            )
        }
    }
}


@Composable
fun AssessmentReportScreenContentSummary(
    modifier: Modifier = Modifier,
    maxHeight: Dp,
    maxWidth: Dp,
    values: List<Float>,
) {
    val verticalScroll = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(verticalScroll),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        //performance card
        AssessmentPerformanceCard(
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            values = values,
        )

        //access link
        AssessmentLinkBar(
            onCopyLink = { /*TODO*/ })
    }
}


@Composable
fun AssessmentReportScreenContentParticipants(
    modifier: Modifier = Modifier,
    participants: List<Participant>,
    onOptions: (String) -> Unit,
) {
    val scrollState = rememberLazyListState()

    Surface(modifier = modifier) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(id = R.string.participants),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                )
            }

            LazyColumn(
                modifier = modifier,
                state = scrollState,
            ) {
                items(participants) { each ->
                    AssessmentParticipantItem(
                        order = each.order,
                        studentName = each.studentName,
                        date = each.date,
                        score = each.score,
                        onOptions = onOptions
                    )
                }
            }
        }
    }
}


enum class AssessmentReportPage {
    Summary, Participants
}


@Composable
fun AssessmentPerformanceCard(
    modifier: Modifier = Modifier,
    values: List<Float>,
    maxHeight: Dp,
    maxWidth: Dp,
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
                PerformanceCircle(values = values, modifier = innerModifier.layoutId("circle"))

                ChartValueItem(
                    heading = values.average().toPercentage(),
                    subtitle = "Avg. Score",
                    modifier = innerModifier.layoutId("avg")
                )

                ChartValueItem(
                    heading = "1/28",
                    subtitle = "Submitted",
                    modifier = innerModifier.layoutId("submitted")
                )

                ChartValueItem(
                    heading = "6%",
                    subtitle = "Completion Rate",
                    modifier = innerModifier.layoutId("completion")
                )

                GradePreviewBar(
                    modifier = innerModifier.layoutId("gradeBar")
                )
            }
        }
    }
}

private fun assessmentPerformanceCardConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val circle = createRefFor("circle")
        val avg = createRefFor("avg")
        val submitted = createRefFor("submitted")
        val completion = createRefFor("completion")
        val gradeBar = createRefFor("gradeBar")

        constrain(circle) {
            top.linkTo(parent.top, margin)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
        }

        constrain(avg) {
            top.linkTo(circle.top, 0.dp)
            bottom.linkTo(circle.bottom, 0.dp)
            start.linkTo(circle.start, 0.dp)
            end.linkTo(circle.end, 0.dp)
        }

        constrain(submitted) {
            top.linkTo(circle.bottom, 4.dp)
            start.linkTo(parent.start, margin)
        }

        constrain(completion) {
            top.linkTo(circle.bottom, 4.dp)
            end.linkTo(parent.end, margin)
        }

        constrain(gradeBar) {
            top.linkTo(submitted.bottom, 8.dp)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
        }
    }
}

@Composable
fun AssessmentLinkBar(
    modifier: Modifier = Modifier,
    onCopyLink: () -> Unit,
    color: Color = MaterialTheme.colors.onPrimary,
    backgroundColor: Color = MaterialTheme.colors.primary,
) {
    val innerModifier = Modifier
    val constraints = assessmentLinkBarConstraints(8.dp)

    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        backgroundColor = backgroundColor,
        elevation = 2.dp, shape = RoundedCornerShape(16.dp)
    ) {
        BoxWithConstraints(modifier = modifier) {
            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                constraintSet = constraints,
            ) {
                Text(
                    text = stringResource(id = R.string.assessment_link),
                    fontSize = 12.sp,
                    modifier = innerModifier.layoutId("title"),
                    color = color.copy(0.5f)
                )
                Text(
                    text = "https://app.classifi.co/exam/start/530523",
                    fontSize = 12.sp,
                    modifier = innerModifier.layoutId("link"),
                    color = color,
                )

                RoundedIconButton(
                    icon = R.drawable.icon_copy,
                    onClick = onCopyLink,
                    modifier = innerModifier.layoutId("icon"),
                    tint = MaterialTheme.colors.onPrimary,
                )
            }
        }
    }

}

private fun assessmentLinkBarConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val title = createRefFor("title")
        val link = createRefFor("link")

        constrain(icon) {
            top.linkTo(title.top, 0.dp)
            bottom.linkTo(link.bottom, 0.dp)
            end.linkTo(parent.end, margin)
        }

        constrain(title) {
            start.linkTo(parent.start, margin)
            top.linkTo(parent.top, margin)
        }

        constrain(link) {
            top.linkTo(title.bottom, 4.dp)
            start.linkTo(title.start, 0.dp)
        }
    }
}

@Composable
fun AssessmentParticipantItem(
    modifier: Modifier = Modifier,
    order: Int,
    studentName: String,
    date: String,
    score: Float,
    onOptions: (String) -> Unit,
) {
    val constraints = assessmentParticipantItemConstraints(8.dp)
    val innerModifier = Modifier

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
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
            Text(
                text = "#$order",
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary,
                modifier = innerModifier.layoutId("order")
            )

            DefaultAvatar(
                modifier = innerModifier.layoutId("avatar"),
                label = studentName,
                onClick = {},
                enabled = false,
            )

            Text(
                modifier = innerModifier
                    .widthIn(max = 180.dp)
                    .layoutId("name"),
                text = studentName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(generateColorFromClassName(studentName))
            )

            Text(
                modifier = innerModifier
                    .widthIn(max = 180.dp)
                    .layoutId("date"),
                text = date,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Black100.copy(0.8f)
            )

            Text(
                modifier = innerModifier.layoutId("score"),
                text = score.toPercentage(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(generateColorFromGrade(score.toGrade()))
            )

            RoundedIconButton(
                modifier = innerModifier.layoutId("extras"),
                onClick = { onOptions(studentName) },
                icon = R.drawable.icon_options_horizontal,
            )
        }
    }
}

data class Participant(
    val order: Int,
    val studentName: String,
    val date: String,
    val score: Float,
)

private fun assessmentParticipantItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val avatar = createRefFor("avatar")
        val order = createRefFor("order")
        val name = createRefFor("name")
        val date = createRefFor("date")
        val score = createRefFor("score")
        val extras = createRefFor("extras")

        constrain(avatar) {
            top.linkTo(name.top, 0.dp)
            bottom.linkTo(date.bottom, 0.dp)
            start.linkTo(order.end, 16.dp)
        }

        constrain(order) {
            start.linkTo(parent.start, margin)
            top.linkTo(name.top, 0.dp)
            bottom.linkTo(date.bottom, 0.dp)
        }

        constrain(name) {
            top.linkTo(parent.top, margin)
            start.linkTo(avatar.end, 8.dp)
        }

        constrain(date) {
            top.linkTo(name.bottom, 4.dp)
            start.linkTo(name.start, 0.dp)
        }

        constrain(score) {
            top.linkTo(name.top, 0.dp)
            bottom.linkTo(date.bottom, 0.dp)
            start.linkTo(name.end, 16.dp)
            end.linkTo(extras.start, 8.dp)
        }

        constrain(extras) {
            top.linkTo(name.top, 0.dp)
            bottom.linkTo(date.bottom, 0.dp)
            end.linkTo(parent.end, margin)
        }
    }
}


@Preview
@Composable
private fun AssessmentLinkBarPreview() {
    AssessmentLinkBar(
        onCopyLink = {}
    )
}

@Preview
@Composable
private fun AssessmentParticipantItemPreview() {
    AssessmentParticipantItem(
        order = 1,
        studentName = "Sanusi Lafiagi",
        date = "29th July, 2022",
        score = 0.95f,
        onOptions = {}
    )
}