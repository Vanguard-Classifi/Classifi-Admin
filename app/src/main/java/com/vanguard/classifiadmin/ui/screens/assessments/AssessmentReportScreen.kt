package com.vanguard.classifiadmin.ui.screens.assessments


import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.vanguard.classifiadmin.ui.components.ChartValueItem
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.ClassIcon
import com.vanguard.classifiadmin.ui.components.PerformanceCircle
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch

const val ASSESSMENT_REPORT_SCREEN = "assessment_report_screen"

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

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

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
                )
            })
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
        )
    }
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
) {
    HorizontalPager(state = pagerState, count = pages.size, userScrollEnabled = false) { page ->
        when (page) {
            0 -> AssessmentReportScreenContentSummary(
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                values = values
            )

            1 -> AssessmentReportScreenContentParticipants()
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

    Column(modifier = modifier.verticalScroll(verticalScroll)) {
        //performance card
        AssessmentPerformanceCard(
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            values = values,
        )

        //access link
    }
}


@Composable
fun AssessmentReportScreenContentParticipants(
    modifier: Modifier = Modifier
) {

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
                modifier = modifier.fillMaxWidth().padding(8.dp),
                constraintSet = constraints,
            ) {
                PerformanceCircle(values = values, modifier = innerModifier.layoutId("circle"))

                ChartValueItem(
                    heading = values.average().toString(),
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
    }
}