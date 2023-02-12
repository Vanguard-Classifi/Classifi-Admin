package com.vanguard.classifiadmin.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.router.BottomDestination
import com.vanguard.classifiadmin.ui.components.ClassFilterScreen
import com.vanguard.classifiadmin.ui.components.ClassifiFeature
import com.vanguard.classifiadmin.ui.components.DashboardMenu
import com.vanguard.classifiadmin.ui.components.DashboardMenuScreen
import com.vanguard.classifiadmin.ui.components.FeatureListItem
import com.vanguard.classifiadmin.ui.components.Level
import com.vanguard.classifiadmin.ui.screens.classes.JoinClassScreen
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val MAIN_DASHBOARD_SCREEN = "main_dashboard_screen"

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainDashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    goToFeature: (ClassifiFeature) -> Unit,
    onSelectMenu: (DashboardMenu) -> Unit,
    onSelectProfile: () -> Unit,
    onManageClass: (String) -> Unit,
) {
    val navController = rememberNavController()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    var menuState by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(false) }
    var joinClassState by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        ModalBottomSheetLayout(
            sheetState = sheetState,
            scrimColor = MaterialTheme.colors.primary.copy(0.1f),
            sheetElevation = 8.dp,
            sheetBackgroundColor = MaterialTheme.colors.onPrimary,
            sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            sheetContent = {
                DashboardBottomSheetContent(
                    modifier = modifier,
                    onSelectFeature = {
                        goToFeature(it)          //go to feature destination
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
                    TopBar(
                        filterActivated = filterState,
                        onFilter = {
                            filterState = !filterState
                        },
                        openProfile = { menuState = !menuState },
                        openSheet = {
                            coroutineScope.launch {
                                showModalSheet.value = true
                                sheetState.show()
                            }
                        },
                        username = "Hamza Jesim",
                        filterLabel = "Grade 1",
                    )
                },
                bottomBar = {
                    BottomBar(
                        navController = navController,
                    )
                },
                content = { padding ->
                    BottomContainer(
                        viewModel = viewModel,
                        modifier = modifier.padding(padding),
                        navController = navController,
                    )
                }
            )
        }

        AnimatedVisibility(
            visible = menuState,
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
            Popup(alignment = Alignment.TopEnd,
                offset = IntOffset(0, 100),
                onDismissRequest = { menuState = false }) {
                DashboardMenuScreen(
                    username = "Genju Wuhabby",
                    email = "hamzajesim@gmail.com",
                    status = "Update your profile",
                    onSelectProfile = onSelectProfile,
                    onSelectMenu = onSelectMenu,
                )
            }
        }

        AnimatedVisibility(
            visible = filterState,
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
            Popup(alignment = Alignment.TopEnd,
                offset = IntOffset(0, 200),
                onDismissRequest = { filterState = false }) {
                ClassFilterScreen(
                    onClose = { filterState = false },
                    assignedClasses = listOf(
                        Level(
                            name = "Grade 1",
                            code = "GRD1/FLIS"
                        ),
                        Level(
                            name = "Grade 9",
                            code = "GRD1/FLIS"
                        ),
                        Level(
                            name = "Grade 8",
                            code = "GRD1/FLIS"
                        ),
                        Level(
                            name = "Grade 7",
                            code = "GRD1/FLIS"
                        ),
                        Level(
                            name = "Grade 2",
                            code = "GRD1/FLIS"
                        ),
                        Level(
                            name = "Grade 3",
                            code = "GRD1/FLIS"
                        ),
                        Level(
                            name = "Grade 4",
                            code = "GRD1/FLIS"
                        ),
                        Level(
                            name = "Grade 5",
                            code = "GRD1/FLIS"
                        ),
                        Level(
                            name = "Grade 6",
                            code = "GRD1/FLIS"
                        ),
                    ),
                    onManageClass = onManageClass,
                    onAddClass = {
                        /*todo*/
                        //close current dialog
                        filterState = false
                        joinClassState = true
                        //open add class dialog
                    }
                )
            }
        }


        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            AnimatedVisibility(
              modifier = modifier.padding(top = maxHeight / 4),
                visible = joinClassState,
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
                JoinClassScreen(
                    viewModel = viewModel, onClose = {
                    joinClassState = false
                },
                )
            }
        }
    }
}


@Composable
fun DashboardBottomSheetContent(
    modifier: Modifier = Modifier,
    onSelectFeature: (ClassifiFeature) -> Unit,
    features: List<ClassifiFeature> = ClassifiFeature.values().toList(),
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
            items(features) { each ->
                FeatureListItem(feature = each, onSelect = onSelectFeature)
            }
        }
    }
}


enum class DestinationItem(val label: String, val icon: Int, val screen: String) {
    Feed("Feed", icon = R.drawable.icon_feeds, BottomDestination.feeds),
    Students("Students", R.drawable.icon_students, BottomDestination.students),
    Assessments("Assessments", R.drawable.icon_assessment, BottomDestination.assessments),
    Reports("Reports", R.drawable.icon_reports, BottomDestination.reports)
}
