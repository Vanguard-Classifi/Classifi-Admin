package com.vanguard.classifiadmin.ui.screens.admin

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithOptions
import com.vanguard.classifiadmin.ui.components.ClassFilterManageButton
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.StagedItemIcon
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val MANAGE_CLASS_ADMIN_SCREEN = "manage_class_admin_screen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ManageClassAdminScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onManageClassDetail: (ClassModel) -> Unit,
) {
    val TAG = "ManageClassAdminScreen"
    val scope = rememberCoroutineScope()
    val classSelectedState by viewModel.classSelectedStateManageClasses.collectAsState()
    var optionState by remember { mutableStateOf(false) }
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedClassesNetwork by viewModel.verifiedClassesNetwork.collectAsState()
    val selectedClasses by viewModel.selectedClassesManageClasses.collectAsState()
    val classByCodeNetwork by viewModel.classByCodeNetwork.collectAsState()
    val message by viewModel.manageClassMessage.collectAsState()


    Log.e(TAG, "ManageClassAdminScreen: class is selected ${classSelectedState ?: false}")

    LaunchedEffect(Unit) {
        viewModel.getCurrentSchoolIdPref()
        viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(message) {
        if (message !is ManageClassMessage.NoMessage) {
            delay(3000)
            viewModel.onManageClassMessageChanged(ManageClassMessage.NoMessage)
        }
    }


    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithOptions(
                    onBack = onBack,
                    heading = stringResource(id = R.string.manage_classes),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    onOptions = {
                        optionState = !optionState
                    },
                )
            },
            content = {
                ManageClassAdminScreenContent(
                    modifier = modifier.padding(it),
                    viewModel = viewModel,
                    onBack = onBack,
                    onManageClassDetail = onManageClassDetail,
                )
            }
        )

        AnimatedVisibility(
            visible = optionState,
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
                onDismissRequest = { optionState = false }) {
                ManageClassPopupOptionsScreen(
                    onSelectOption = {
                        when (it) {
                            ManageClassPopupOption.SelectAll -> {
                                //select all classes
                                if (verifiedClassesNetwork.data?.isNotEmpty() == true) {
                                    verifiedClassesNetwork.data?.map { myClass ->
                                        viewModel.onAddClassToBuffer(myClass.classCode.orEmpty())
                                    }
                                }
                            }

                            ManageClassPopupOption.Delete -> {
                                //delete selected
                                scope.launch {
                                    if (selectedClasses.isNotEmpty()) {
                                        selectedClasses.map { classCode ->
                                            // get class by code and delete
                                            viewModel.getClassByCodeNetwork(
                                                classCode,
                                                currentSchoolIdPref.orEmpty()
                                            )
                                            delay(1000)
                                            if (classByCodeNetwork.data != null)
                                                viewModel.deleteClassNetwork(classByCodeNetwork.data!!) {
                                                }
                                        }
                                        viewModel.onDecSelectionListenerManageClass()
                                        viewModel.onClearBufferManageClass()
                                    } else {
                                        viewModel.onManageClassMessageChanged(ManageClassMessage.NoItemSelected())
                                    }
                                }
                            }
                        }
                        optionState = false
                    },
                )
            }
        }


        if (message !is ManageClassMessage.NoMessage) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                MessageBar(
                    message = message.message,
                    icon = R.drawable.icon_info,
                    onClose = {
                        viewModel.onManageClassMessageChanged(ManageClassMessage.NoMessage)
                    },
                    maxWidth = maxWidth,
                    modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                )
            }
        }
    }
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun ManageClassAdminScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onManageClassDetail: (ClassModel) -> Unit,
) {
    val TAG = "ManageClassAdminScreenContent"
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedClassesNetwork by viewModel.verifiedClassesNetwork.collectAsState()

    val selectedClasses by viewModel.selectedClassesManageClasses.collectAsState()
    val selectionListener by viewModel.selectionListenerManageClass.collectAsState()
    Log.e(TAG, "ManageClassAdminScreenContent: selectionListener value is $selectionListener")

    LaunchedEffect(Unit) {
        viewModel.onClearBufferManageClass()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getVerifiedClassesNetwork(currentSchoolIdPref ?: "")
    }

    LaunchedEffect(
        key1 = selectionListener,
        block = {
            viewModel.getVerifiedClassesNetwork(currentSchoolIdPref ?: "")
            viewModel.onClassSelectedStateManageClassesChanged(
                selectedClasses.isNotEmpty()
            )
        }
    )


    Column(
        modifier = modifier
    ) {
        Card(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 2.dp,
        ) {
            if (verifiedClassesNetwork.data?.isNotEmpty() == true) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    state = rememberLazyListState(),
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    items(verifiedClassesNetwork.data!!) { each ->
                        ClassListItemAdmin(
                            myClass = each.toLocal(),
                            selected = selectedClasses.contains(each.toLocal().classCode.orEmpty()),
                            onManageClass = {
                                //go to class detail
                                viewModel.onSelectedClassManageClassAdminChanged(it)
                                onManageClassDetail(it)
                            },
                            onHold = {
                                viewModel.onAddClassToBuffer(it.classCode.orEmpty())
                                viewModel.onIncSelectionListenerManageClass()
                            },
                            onTap = {
                                //if selectedClasses not empty, if already selected, unselect
                                // if not selected, selected
                                if (selectedClasses.isNotEmpty()) {
                                    if (selectedClasses.contains(it.classCode)) {
                                        //unselect
                                        viewModel.onRemoveClassFromBuffer(it.classCode.orEmpty())
                                    } else {
                                        //select
                                        viewModel.onAddClassToBuffer(it.classCode.orEmpty())
                                    }
                                } else {
                                    viewModel.onManageClassMessageChanged(ManageClassMessage.HoldToMark())
                                }
                                viewModel.onIncSelectionListenerManageClass()
                            }
                        )
                    }

                }
            }
        }
    }

}


@Composable
fun ClassListItemAdmin(
    modifier: Modifier = Modifier,
    myClass: ClassModel,
    selected: Boolean = false,
    onManageClass: (ClassModel) -> Unit,
    onHold: (ClassModel) -> Unit,
    onTap: (ClassModel) -> Unit,
) {
    val constraints = ClassListItemConstraints(8.dp)
    val innerModifier = Modifier

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onHold(myClass)
                    },
                    onTap = {
                        onTap(myClass)
                    }
                )
            }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
                0.1f
            )
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            StagedItemIcon(
                modifier = innerModifier.layoutId("icon"),
                color = Color(generateColorFromClassName(myClass.className ?: "")),
            )

            Text(
                modifier = innerModifier.layoutId("className"),
                text = myClass.className ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(generateColorFromClassName(myClass.className ?: ""))
            )

            Text(
                modifier = innerModifier.layoutId("code"),
                text = myClass.classCode ?: "",
                fontSize = 12.sp,
                color = Black100.copy(0.8f)
            )


            ClassFilterManageButton(
                modifier = innerModifier.layoutId("manage"),
                icon = R.drawable.icon_settings,
                label = stringResource(id = R.string.manage),
                className = myClass.className ?: "",
                onSelect = { onManageClass(myClass) },
            )

        }
    }
}

private fun ClassListItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val className = createRefFor("className")
        val code = createRefFor("code")
        val manage = createRefFor("manage")

        constrain(icon) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(className.top, margin = 0.dp)
        }

        constrain(className) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(icon.end, margin = 8.dp)
        }

        constrain(code) {
            top.linkTo(className.bottom, margin = 4.dp)
            start.linkTo(className.start, margin = 0.dp)
        }

        constrain(manage) {
            top.linkTo(className.top, margin = 0.dp)
            bottom.linkTo(code.bottom, margin = 0.dp)
            end.linkTo(parent.end, margin = margin)
        }
    }
}


@Composable
fun ManageClassPopupOptionsScreen(
    modifier: Modifier = Modifier,
    onSelectOption: (ManageClassPopupOption) -> Unit,
    options: List<ManageClassPopupOption> = ManageClassPopupOption.values().toList(),
) {
    Card(
        modifier = modifier.padding(start = 92.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.onPrimary
    ) {
        Column(modifier = modifier) {
            Spacer(modifier = modifier.height(16.dp))
            options.forEach { ManageClassOptionItem(option = it, onSelect = onSelectOption) }
            Spacer(modifier = modifier.height(16.dp))
        }
    }
}


@Composable
fun ManageClassOptionItem(
    modifier: Modifier = Modifier,
    option: ManageClassPopupOption,
    onSelect: (ManageClassPopupOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .clickable { onSelect(option) }
            .clip(RoundedCornerShape(2.dp)),
        shape = RoundedCornerShape(2.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = option.title,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


enum class ManageClassPopupOption(val title: String) {
    SelectAll("Select All"),
    Delete("Delete")
}

sealed class ManageClassMessage(val message: String) {
    class HoldToMark() : ManageClassMessage("Please hold to mark item")
    class NoItemSelected() : ManageClassMessage("No item selected")
    object NoMessage : ManageClassMessage("")
}