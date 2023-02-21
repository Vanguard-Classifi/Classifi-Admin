package com.vanguard.classifiadmin.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.layoutId
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.SchoolModel
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.domain.extensions.customTabIndicatorOffset
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.UserLoginState
import com.vanguard.classifiadmin.domain.helpers.UserRole
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.helpers.today
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.PagerBarWithIcon
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.components.SuccessBar
import com.vanguard.classifiadmin.ui.screens.classes.JoinClassScreen
import com.vanguard.classifiadmin.ui.screens.welcome.CreateSchoolErrorState
import com.vanguard.classifiadmin.ui.screens.welcome.TextRowWithClickable
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


const val ACCOUNT_SCREEN = "account_screen"

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MyAccountScreen(
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
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val userByIdNetwork by viewModel.userByIdNetwork.collectAsState()
    var profileSavedMessageState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        //find current user profile
        viewModel.getCurrentUserIdPref()
        delay(1000)
        viewModel.getUserByIdNetwork(currentUserIdPref ?: "")
    }

    LaunchedEffect(profileSavedMessageState) {
        if(profileSavedMessageState) {
            delay(3000)
            profileSavedMessageState = false
        }
    }

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
                        },
                        viewModel = viewModel,
                        onSaveProfileChanges = {

                        }
                    )
                }
            )
        }

        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            AnimatedVisibility(
                modifier = modifier.padding(top = 100.dp),
                visible = profileSavedMessageState,
                enter = scaleIn(
                    initialScale = 0.8f, animationSpec = tween(
                        durationMillis = 50, easing = FastOutLinearInEasing
                    )
                ),
                exit = scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(
                        durationMillis = 50, easing = FastOutLinearInEasing
                    ),
                ),
            ) {
                SuccessBar(
                    message = stringResource(id = R.string.changes_saved),
                    maxWidth = maxWidth
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyAccountScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClick: () -> Unit,
    onSaveProfileChanges: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0)

    Column {
        MyAccountScreenContentHeader(
            pagerState = pagerState,
        )
        MyAccountScreenContentBody(
            pagerState = pagerState,
            onClick = onClick,
            viewModel = viewModel,
            onSaveProfileChanges = onSaveProfileChanges
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
                    PagerBarWithIcon(
                        icon = tab.icon, text = tab.fullname,
                        selected = currentPage == index,
                        backgroundColor = backgroundColor,
                        onTextLayout = { result ->
                            tabWidths[index] =
                                with(density) { result.size.width.toDp().plus(34.dp) }
                        }
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
    viewModel: MainViewModel,
    pagerState: PagerState,
    onClick: () -> Unit,
    onSaveProfileChanges: () -> Unit,
    pages: List<AccountPage> = AccountPage.values().toList(),
) {
    val TAG = "MyAccountScreenContentBody"

    HorizontalPager(state = pagerState, count = pages.size, userScrollEnabled = false) { page ->
        when (page) {
            0 -> MyAccountScreenProfile(
                onClick = onClick,
                onSaveChanges = onSaveProfileChanges,
                viewModel = viewModel,
            )

            1 -> MyAccountScreenAccountSettings()
            2 -> MyAccountScreenPreferences()
            3 -> MyAccountScreenAdmin()
        }
    }
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

