package com.vanguard.classifiadmin.ui.screens.admin

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.helpers.today
import com.vanguard.classifiadmin.domain.helpers.todayComputational
import com.vanguard.classifiadmin.domain.services.ClassCreationService
import com.vanguard.classifiadmin.domain.services.ClassCreationServiceActions
import com.vanguard.classifiadmin.domain.services.ClassCreationServiceExtras
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.ClassFilterManageButton
import com.vanguard.classifiadmin.ui.components.ClassIcon
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.SecondaryTextButton
import com.vanguard.classifiadmin.ui.screens.profile.AdminCreateOrManageFeature
import com.vanguard.classifiadmin.ui.screens.profile.FeatureIcon
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val CREATE_CLASS_ADMIN_SCREEN = "create_class_screen"

@Composable
fun CreateClassAdminScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    Surface(modifier = Modifier) {
        Scaffold(
            modifier = modifier,
            topBar = {
                ChildTopBar(
                    onBack = onBack,
                    elevation = 0.dp,
                    heading = stringResource(id = R.string.class_creation),
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                )
            },
            content = {
                CreateClassAdminScreenContent(
                    modifier = Modifier.padding(it),
                    viewModel = viewModel,
                    onBack = onBack,
                )
            }
        )
    }
}


@Composable
fun CreateClassAdminScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    val verticalScroll = rememberScrollState()
    val context = LocalContext.current
    val classNameAdmin by viewModel.classNameAdmin.collectAsState()
    val classCodeAdmin by viewModel.classCodeAdmin.collectAsState()
    val stagedClassesNetwork by viewModel.stagedClassesNetwork.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUsernamePref by viewModel.currentUsernamePref.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolNamePref by viewModel.currentSchoolNamePref.collectAsState()
    var stagingListener by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()
    val exception: MutableState<CreateClassAdminException> = remember {
        mutableStateOf(CreateClassAdminException.NoException)
    }

    LaunchedEffect(Unit) {
        delay(2000)
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentSchoolNamePref()
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentUsernamePref()
    }

    LaunchedEffect(key1 = stagingListener, block = {
        //find the staged classes
        viewModel.getStagedClassesNetwork(currentSchoolIdPref ?: "")
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
                    value = classNameAdmin ?: "",
                    onValueChange = viewModel::onClassNameAdminChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.name_of_class),
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
                    isError = exception.value is CreateClassAdminException.ClassName
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = classCodeAdmin ?: "",
                    onValueChange = viewModel::onClassCodeAdminChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.class_code),
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
                    isError = exception.value is CreateClassAdminException.ClassCode
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
                            if (classNameAdmin == null || classNameAdmin?.isBlank() == true) {
                                exception.value = CreateClassAdminException.ClassName()
                                return@SecondaryTextButton
                            }
                            if (classCodeAdmin == null || classCodeAdmin?.isBlank() == true) {
                                exception.value = CreateClassAdminException.ClassCode()
                                return@SecondaryTextButton
                            }

                            scope.launch {
                                viewModel.saveClassAsStagedNetwork(
                                    ClassModel(
                                        classId = UUID.randomUUID().toString(),
                                        className = classNameAdmin,
                                        classCode = classCodeAdmin,
                                        schoolId = currentSchoolIdPref,
                                        schoolName = currentSchoolNamePref,
                                        modifiedBy = currentUsernamePref,
                                        dateCreated = today(),
                                        lastModified = todayComputational(),
                                    ).toNetwork(),
                                    onResult = {

                                    }
                                )
                            }.invokeOnCompletion {
                                runnableBlock {
                                    stagingListener++
                                    //clear the fields
                                    viewModel.clearCreateClassAdminFields()
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
                            scope.launch {
                                if (
                                    classNameAdmin?.isNotBlank() == true &&
                                    classCodeAdmin?.isNotBlank() == true
                                ) {
                                    viewModel.saveClassAsVerifiedNetwork(
                                        ClassModel(
                                            classId = UUID.randomUUID().toString(),
                                            className = classNameAdmin,
                                            classCode = classCodeAdmin,
                                            schoolId = currentSchoolIdPref,
                                            dateCreated = today(),
                                            lastModified = todayComputational(),
                                        ).toNetwork(),
                                        onResult = {}
                                    )
                                }

                                //call an insertion service
                                val intent = Intent(
                                    context,
                                    ClassCreationService::class.java
                                ).putExtra(
                                    ClassCreationServiceExtras.currentSchoolId,
                                    currentSchoolIdPref
                                )
                                    .setAction(ClassCreationServiceActions.ACTION_UPLOAD)

                                context.startService(intent)
                            }.invokeOnCompletion {
                                runnableBlock {
                                    viewModel.clearCreateClassAdminFields()
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

        if (stagedClassesNetwork.data?.isNotEmpty() == true) {
            Card(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 8.dp, bottom = 64.dp, start = 8.dp, end = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 2.dp,
            ) {

                val stagedClassesSorted = stagedClassesNetwork.data?.sortedByDescending {
                    it.lastModified
                }

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    stagedClassesSorted?.forEach { each ->
                        StagedClassItem(myClass = each.toLocal(), onRemove = {
                            //on remove class from stage
                            viewModel.deleteClassNetwork(each) {
                                stagingListener--
                            }
                        })
                    }
                }
            }
        }
    }
}

sealed class CreateClassAdminException(val message: String) {
    class ClassName : CreateClassAdminException("Please enter a class name")
    class ClassCode : CreateClassAdminException("Please enter a class code")
    object NoException : CreateClassAdminException("")
}


@Composable
fun StagedClassItem(
    modifier: Modifier = Modifier,
    myClass: ClassModel,
    onRemove: (ClassModel) -> Unit,
) {
    val constraints = StagedClassItemConstraints(8.dp)
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
            ClassIcon(
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

            RoundedIconButton(
                modifier = innerModifier.layoutId("manage"),
                onClick = { onRemove(myClass) }, icon = R.drawable.icon_close,
            )
        }
    }
}

private fun StagedClassItemConstraints(margin: Dp): ConstraintSet {
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


@Preview
@Composable
private fun StagedClassItemPreview() {
    StagedClassItem(
        myClass = ClassModel(
            classId = "",
            className = "Class 1",
            classCode = "CHFSD232",
        ),
        onRemove = {},
    )
}