package com.vanguard.classifiadmin.ui.screens.weeklyplan

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.components.DashboardMenuScreen
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.WeeklyPlanGrid
import com.vanguard.classifiadmin.ui.components.WeeklyPlanModel
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val WEEKLY_PLAN_DETAIL_SCREEN = "weekly_plan_detail_screen"


@Composable
fun WeeklyPlanDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    WeeklyPlanDetailScreenContent()
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun WeeklyPlanDetailScreenContent(
    modifier: Modifier = Modifier,
) {
    val TAG = "WeeklyPlanDetailScreenContent"
    val plans by remember { mutableStateOf(WeeklyPlanModel.init()) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    var onTapMessageState by remember { mutableStateOf(false) }

    LaunchedEffect(onTapMessageState) {
        if (onTapMessageState) {
            delay(3000)
            onTapMessageState = false
        }
    }


    BoxWithConstraints(modifier = modifier.fillMaxSize()) {

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
                //sheet content
                WeeklyPlanDetailBottomSheetContent(
                    onDone = {
                        coroutineScope.launch {
                            showModalSheet.value = false
                            delay(500)
                            sheetState.hide()
                        }
                    }
                )
            },
            content = {
                WeeklyPlanGrid(
                    onTap = {
                        //show message to double tap or hold
                        Log.e(TAG, "WeeklyPlanDetailScreenContent: onTap pressed")
                        onTapMessageState = true
                    },
                    onEdit = {
                        Log.e(TAG, "WeeklyPlanDetailScreenContent: onEdit pressed")
                        coroutineScope.launch {
                            showModalSheet.value = true
                            sheetState.show()
                        }
                    },
                    headingColor = MaterialTheme.colors.primary,
                    headingBackgroundColor = MaterialTheme.colors.primary.copy(0.3f),
                    plans = plans,
                    headings = WeeklyPlanHeading.values().toList().map { it.name },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd,
        ) {
            AnimatedVisibility(
                visible = onTapMessageState,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }
                ),
            ) {
                //on tap message content
                MessageBar(
                    message = stringResource(id = R.string.double_tap_or_hold),
                    onClose = {
                        onTapMessageState = false
                    }
                )
            }

        }

    }
}

enum class WeeklyPlanHeading {
    Class, Subject, Pages, Topic, HomeWork, Notes,
}

@Composable
fun WeeklyPlanDetailBottomSheetContent(
    modifier: Modifier = Modifier,
    onDone: () -> Unit,
) {

    var field by remember { mutableStateOf("") }
    val verticalScroll = rememberScrollState()

    Column(
        modifier = modifier.fillMaxWidth().verticalScroll(verticalScroll),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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

        OutlinedTextField(
            modifier = modifier
                .heightIn(min = 200.dp)
                .fillMaxWidth()
                .padding(8.dp),
            value = field,
            onValueChange = {
                field = it
            },
        )

        PrimaryTextButton(
            label = stringResource(id = R.string.done),
            onClick = onDone,
            modifier = Modifier.padding(8.dp)
        )

    }
}

@Composable
@Preview
private fun WeeklyPlanDetailBottomSheetContentPreview() {
    WeeklyPlanDetailBottomSheetContent(
        onDone = {}
    )
}