package com.vanguard.classifiadmin.ui.screens.admin

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.data.local.models.SubjectModel
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.helpers.todayComputational
import com.vanguard.classifiadmin.domain.services.SubjectCreationService
import com.vanguard.classifiadmin.domain.services.SubjectCreationServiceActions
import com.vanguard.classifiadmin.domain.services.SubjectCreationServiceExtras
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.StagedItemIcon
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.NoDataInline
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.SecondaryTextButton
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val CREATE_SUBJECT_ADMIN_SCREEN = "create_subject_screen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateSubjectAdminScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
) {

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val subjectAlreadyExistStateAdmin by viewModel.subjectAlreadyExistStateAdmin.collectAsState()
    val verifiedClassesNetwork by viewModel.verifiedClassesNetwork.collectAsState()

    LaunchedEffect(Unit) {
        delay(1000)
        viewModel.getCurrentSchoolIdPref()
        delay(2000)
        viewModel.getVerifiedClassesNetwork(currentSchoolIdPref ?: "")
    }

    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier) {
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
                    CreateSubjectAdminBottomSheetContent(
                        myClasses = verifiedClassesNetwork.data?.map { it.toLocal() }
                            ?: emptyList<ClassModel>(),
                        onSelectClass = {
                            coroutineScope.launch {
                                viewModel.onSelectedClassSubjectCreationAdminChanged(it)
                                //hide the bottomsheet
                                delay(400)
                                sheetState.hide()
                                showModalSheet.value = false
                            }
                        }
                    )
                },
                content = {
                    Scaffold(
                        modifier = modifier,
                        topBar = {
                            ChildTopBar(
                                onBack = onBack,
                                elevation = 0.dp,
                                heading = stringResource(id = R.string.subject_creation),
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary,
                            )
                        },
                        content = {
                            CreateSubjectAdminScreenContent(
                                modifier = Modifier.padding(it),
                                viewModel = viewModel,
                                onSelectSubjectClass = {
                                    //show bottom sheet
                                    coroutineScope.launch {
                                        sheetState.show()
                                        showModalSheet.value = true
                                    }
                                },
                                onBack = onBack,
                            )
                        }
                    )

                }
            )

            if (subjectAlreadyExistStateAdmin == true) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    MessageBar(
                        message = stringResource(id = R.string.subject_already_exists),
                        icon = R.drawable.icon_info,
                        onClose = {
                            viewModel.onSubjectAlreadyExistStateAdminChanged(false)
                        },
                        maxWidth = maxWidth,
                        modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateSubjectAdminScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onSelectSubjectClass: () -> Unit,
    onBack: () -> Unit,
) {
    val TAG = "CreateSubjectAdminScreenContent"
    val context = LocalContext.current
    val verticalScroll = rememberScrollState()
    val subjectNameAdmin by viewModel.subjectNameAdmin.collectAsState()
    val subjectCodeAdmin by viewModel.subjectCodeAdmin.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUsernamePref by viewModel.currentUsernamePref.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolNamePref by viewModel.currentSchoolNamePref.collectAsState()
    val selectedClassSubjectCreationAdmin by viewModel.selectedClassSubjectCreationAdmin.collectAsState()
    val subjectByCodeNetwork by viewModel.subjectByCodeNetwork.collectAsState()
    val stagedSubjectsNetwork by viewModel.stagedSubjectsNetwork.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    var stagingListener by remember { mutableStateOf(0) }
    val exception: MutableState<CreateSubjectAdminException> = remember {
        mutableStateOf(CreateSubjectAdminException.NoException)
    }

    LaunchedEffect(Unit) {
        delay(2000)
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentSchoolNamePref()
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentUsernamePref()
    }


    LaunchedEffect(key1 = subjectCodeAdmin, block = {
        //search for existing class name or class code
        if (subjectCodeAdmin != null) {
            viewModel.getSubjectByCodeNetwork(subjectCodeAdmin ?: "", currentSchoolIdPref ?: "")
            Log.e(TAG, "CreateSubjectAdminScreenContent: finding class by code")
        }
    })

    LaunchedEffect(key1 = stagingListener, block = {
        //find the staged classes
        viewModel.getStagedSubjectsNetwork(currentSchoolIdPref ?: "")
    })


    Column(modifier = Modifier.verticalScroll(verticalScroll)) {
        Card(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 8.dp, bottom = 64.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 2.dp,
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = subjectNameAdmin ?: "",
                    onValueChange = viewModel::onSubjectNameAdminChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.subject_name),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    isError = exception.value is CreateSubjectAdminException.SubjectName
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = subjectCodeAdmin ?: "",
                    onValueChange = viewModel::onSubjectCodeAdminChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.subject_code),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Characters,
                    ),
                    isError = exception.value is CreateSubjectAdminException.SubjectCode
                )

                Spacer(modifier = Modifier.height(32.dp))


                OutlinedTextField(
                    enabled = false,
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectSubjectClass() },
                    value = selectedClassSubjectCreationAdmin?.classCode ?: "",
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(id = R.string.class_code),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_down),
                            contentDescription = stringResource(id = R.string.arrow_dropdown),
                            tint = Black100
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Characters,
                    ),
                    isError = exception.value is CreateSubjectAdminException.SubjectClass
                )

                Spacer(modifier = Modifier.height(32.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SecondaryTextButton(
                        modifier = Modifier.padding(8.dp),
                        label = stringResource(id = R.string.add_more).uppercase(),
                        onClick = {
                            keyboardController?.hide()

                            if (subjectNameAdmin == null || subjectNameAdmin?.isBlank() == true) {
                                exception.value = CreateSubjectAdminException.SubjectName()
                                return@SecondaryTextButton
                            }
                            if (subjectCodeAdmin == null || subjectCodeAdmin?.isBlank() == true) {
                                exception.value = CreateSubjectAdminException.SubjectCode()
                                return@SecondaryTextButton
                            }

                            if (selectedClassSubjectCreationAdmin == null) {
                                exception.value = CreateSubjectAdminException.SubjectClass()
                                return@SecondaryTextButton
                            }

                            if (subjectCodeAdmin == subjectByCodeNetwork.data?.subjectCode ||
                                subjectNameAdmin == subjectByCodeNetwork.data?.subjectName &&
                                subjectCodeAdmin?.isNotBlank() == true
                            ) {
                                //on subject already exists
                                viewModel.onSubjectAlreadyExistStateAdminChanged(true)
                                return@SecondaryTextButton
                            }

                            scope.launch {
                                viewModel.saveSubjectAsStagedNetwork(
                                    SubjectModel(
                                        subjectId = UUID.randomUUID().toString(),
                                        subjectName = subjectNameAdmin,
                                        subjectCode = subjectCodeAdmin,
                                        schoolId = currentSchoolIdPref,
                                        classId = selectedClassSubjectCreationAdmin?.classId,
                                        className = selectedClassSubjectCreationAdmin?.className,
                                        lastModified = todayComputational(),
                                    ).toNetwork(),
                                    onResult = {

                                    }
                                )
                            }.invokeOnCompletion {
                                runnableBlock {
                                    stagingListener++
                                    //clear the fields
                                    viewModel.clearCreateSubjectAdminFields()
                                }
                            }
                        }
                    )


                    PrimaryTextButton(
                        modifier = Modifier.padding(
                            top = 8.dp, bottom = 8.dp, start = 8.dp
                        ),
                        label = stringResource(id = R.string.save_and_exit).uppercase(),
                        onClick = {
                            keyboardController?.hide()

                            if ((subjectCodeAdmin == subjectByCodeNetwork.data?.subjectCode ||
                                        subjectNameAdmin == subjectByCodeNetwork.data?.subjectName) &&
                                subjectCodeAdmin?.isNotBlank() == true
                            ) {
                                viewModel.onSubjectAlreadyExistStateAdminChanged(true)
                                return@PrimaryTextButton
                            }


                            scope.launch {
                                if ((subjectNameAdmin?.isNotBlank() == true &&
                                            subjectCodeAdmin?.isNotBlank() == true &&
                                            selectedClassSubjectCreationAdmin != null) ||
                                    stagedSubjectsNetwork.data?.isNotEmpty() == true
                                ) {
                                    if (
                                        subjectNameAdmin?.isNotBlank() == true &&
                                        subjectCodeAdmin?.isNotBlank() == true &&
                                        selectedClassSubjectCreationAdmin != null
                                    ) {
                                        viewModel.saveSubjectAsVerifiedNetwork(
                                            SubjectModel(
                                                subjectId = UUID.randomUUID().toString(),
                                                subjectName = subjectNameAdmin,
                                                subjectCode = subjectCodeAdmin,
                                                schoolId = currentSchoolIdPref,
                                                classId = selectedClassSubjectCreationAdmin?.classId,
                                                className = selectedClassSubjectCreationAdmin?.className,
                                                lastModified = todayComputational(),
                                            ).toNetwork(),
                                            onResult = {}
                                        )
                                    }

                                    //call an insertion service
                                    val intent = Intent(
                                        context,
                                        SubjectCreationService::class.java,
                                    ).putExtra(
                                        SubjectCreationServiceExtras.currentSchoolId,
                                        currentSchoolIdPref
                                    )
                                        .setAction(SubjectCreationServiceActions.ACTION_UPLOAD)

                                    context.startService(intent)
                                }

                            }.invokeOnCompletion {
                                runnableBlock {
                                    stagingListener++
                                    viewModel.clearCreateSubjectAdminFields()
                                    //close screen
                                    onBack()
                                }
                            }
                        },
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (stagedSubjectsNetwork.data?.isNotEmpty() == true) {
            Card(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 8.dp, bottom = 64.dp, start = 8.dp, end = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 2.dp,
            ) {

                val stagedSubjectsSorted = stagedSubjectsNetwork.data?.sortedByDescending {
                    it.lastModified
                }

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    stagedSubjectsSorted?.forEach { each ->
                        StagedSubjectItem(
                            subject = each.toLocal(),
                            onRemove = {
                                viewModel.deleteSubjectNetwork(each) {
                                    stagingListener--
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}

sealed class CreateSubjectAdminException(val message: String) {
    class SubjectName : CreateSubjectAdminException("Please enter the subject name")
    class SubjectCode : CreateSubjectAdminException("Please enter the subject code")
    class SubjectClass : CreateSubjectAdminException("Please enter the subject class")
    object NoException : CreateSubjectAdminException("")

}

@Composable
fun CreateSubjectAdminBottomSheetContent(
    modifier: Modifier = Modifier,
    myClasses: List<ClassModel>,
    onSelectClass: (ClassModel) -> Unit,
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


        if (myClasses.isEmpty()) {
            //no items screen
            NoDataInline(message = stringResource(id = R.string.classes_not_available))
        } else {
            LazyColumn(
                modifier = modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                state = rememberLazyListState()
            ) {
                items(myClasses) { each ->
                    CreateSubjectClassItem(
                        myClass = each,
                        onSelect = onSelectClass,
                    )
                }
            }
        }

    }

}


@Composable
fun CreateSubjectClassItem(
    modifier: Modifier = Modifier,
    myClass: ClassModel,
    onSelect: (ClassModel) -> Unit,
) {
    val constraints = CreateSubjectClassItemConstraints(8.dp)
    val innerModifier = Modifier
    var rowWidth by remember { mutableStateOf(0) }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp)
            .clickable { onSelect(myClass) },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .onGloballyPositioned { rowWidth = it.size.width }
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
                color = MaterialTheme.colors.primary,
            )

            Text(
                modifier = innerModifier
                    .layoutId("code")
                    .widthIn(max = with(LocalDensity.current) {
                        rowWidth
                            .toDp()
                            .times(.5f)
                    }),
                text = myClass.classCode ?: "",
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.primary,
            )

        }
    }
}

private fun CreateSubjectClassItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val className = createRefFor("className")
        val code = createRefFor("code")

        constrain(icon) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(parent.top, margin = 0.dp)
            bottom.linkTo(parent.bottom, 0.dp)
        }

        constrain(className) {
            top.linkTo(icon.top, margin = 0.dp)
            start.linkTo(icon.end, margin = 8.dp)
        }

        constrain(code) {
            top.linkTo(className.bottom, margin = 4.dp)
            start.linkTo(className.start, margin = 0.dp)
            bottom.linkTo(icon.bottom, 0.dp)
        }

    }
}


@Composable
fun StagedSubjectItem(
    modifier: Modifier = Modifier,
    subject: SubjectModel,
    onRemove: (SubjectModel) -> Unit,
) {
    val constraints = StagedSubjectItemConstraints(8.dp)
    val innerModifier = Modifier
    var rowWidth by remember { mutableStateOf(0) }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .onGloballyPositioned { rowWidth = it.size.width }
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            StagedItemIcon(
                modifier = innerModifier.layoutId("icon"),
                color = Color(generateColorFromClassName(subject.subjectName ?: "")),
            )


            Text(
                modifier = innerModifier.layoutId("className"),
                text = subject.subjectName ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
            )

            Text(
                modifier = innerModifier
                    .layoutId("code")
                    .widthIn(max = with(LocalDensity.current) {
                        rowWidth
                            .toDp()
                            .times(.5f)
                    }),
                text = subject.subjectCode ?: "",
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.primary,
            )

            RoundedIconButton(
                modifier = innerModifier.layoutId("manage"),
                onClick = { onRemove(subject) }, icon = R.drawable.icon_close,
            )
        }
    }
}

private fun StagedSubjectItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val className = createRefFor("className")
        val code = createRefFor("code")
        val manage = createRefFor("manage")

        constrain(icon) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(manage.top, margin = 0.dp)
            bottom.linkTo(manage.bottom, margin = 0.dp)
        }

        constrain(className) {
            top.linkTo(manage.top, margin = 0.dp)
            start.linkTo(icon.end, margin = 8.dp)
        }

        constrain(code) {
            top.linkTo(className.bottom, margin = 4.dp)
            start.linkTo(className.start, margin = 0.dp)
            bottom.linkTo(manage.bottom, margin = 0.dp)
        }

        constrain(manage) {
            top.linkTo(parent.top, margin = 0.dp)
            bottom.linkTo(parent.bottom, margin = 0.dp)
            end.linkTo(parent.end, margin = margin)
        }
    }
}
