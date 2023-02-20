package com.vanguard.classifiadmin.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.extensions.customTabIndicatorOffset
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentReportScreenContentParticipants
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentReportScreenContentSummary
import kotlinx.coroutines.launch


const val ACCOUNT_SCREEN = "account_screen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyAccountScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
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
                MyAccountScreenContentBottomSheetContent()
            }
        ) {
            Scaffold(modifier = modifier,
                topBar = {
                    ChildTopBar(
                        onBack = onBack,
                        elevation = 0.dp,
                        heading = stringResource(id = R.string.manage_account),
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                    )
                },
                content = { padding ->
                    MyAccountScreenContent(
                        modifier = Modifier.padding(padding),
                        onClick = {
                            coroutineScope.launch {
                                showModalSheet.value = true
                                sheetState.show()
                            }
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyAccountScreenContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0)

    Column {
        MyAccountScreenContentHeader(
            pagerState = pagerState,
        )
        MyAccountScreenContentBody(
            pagerState = pagerState,
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyAccountScreenContentHeader(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    backgroundColor: Color = MaterialTheme.colors.primary,
    pages: List<AccountPage> = AccountPage.values().toList()
) {
    val TAG = "MyAccountScreenContentHeader"
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
        backgroundColor = backgroundColor,
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
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_preference),
                        contentDescription = tab.name,
                        tint = if (currentPage == index) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(
                            0.5f
                        ),
                        modifier = modifier
                            .size(24.dp)
                            .padding(2.dp)
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
fun MyAccountScreenContentBody(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onClick: () -> Unit,
    pages: List<AccountPage> = AccountPage.values().toList(),
) {
    val TAG = "MyAccountScreenContentBody"

    HorizontalPager(state = pagerState, count = pages.size, userScrollEnabled = false) { page ->
        when (page) {
            0 -> MyAccountScreenProfile(onClick = onClick)
            1 -> MyAccountScreenAccountSettings()
            2 -> MyAccountScreenPreferences()
            3 -> MyAccountScreenAdmin()
        }
    }
}

@Composable
fun MyAccountScreenProfile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Text(text = "Profile", modifier = Modifier.clickable { onClick() })
}

@Composable
fun MyAccountScreenAccountSettings(
    modifier: Modifier = Modifier
) {

}

@Composable
fun MyAccountScreenPreferences(
    modifier: Modifier = Modifier
) {

}

@Composable
fun MyAccountScreenAdmin(
    modifier: Modifier = Modifier
) {

}

@Composable
fun MyAccountScreenContentBottomSheetContent(
    modifier: Modifier = Modifier
) {
    Text(text = "Bottom Sheet")
}


enum class AccountPage(val fullname: String, val icon: Int) {
    Profile("Profile", R.drawable.icon_profile),
    AccountSettings("Account Settings", R.drawable.icon_setting),
    Preferences("Preferences", R.drawable.icon_preference),
    Administration("Administration", R.drawable.icon_admin)
}

