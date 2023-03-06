package com.vanguard.classifiadmin.ui.screens.admin

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.UserRole
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.domain.helpers.isEmailValid
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.helpers.todayComputational
import com.vanguard.classifiadmin.domain.services.EnrollTeachersService
import com.vanguard.classifiadmin.domain.services.EnrollTeachersServiceActions
import com.vanguard.classifiadmin.domain.services.EnrollTeachersServiceExtras
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.StagedItemIcon
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.SecondaryTextButton
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.util.UUID

const val ENROLL_TEACHER_ADMIN_SCREEN = "enroll_teacher_screen"

@Composable
fun EnrollTeacherAdminScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
) {
    val teacherAlreadyExistState by viewModel.teacherAlreadyExistStateAdmin.collectAsState()

    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier) {
            val maxWidth = maxWidth
            val maxHeight = maxHeight

            Scaffold(modifier = modifier,
                topBar = {
                    ChildTopBar(
                        onBack = onBack,
                        heading = stringResource(id = R.string.enroll_teachers),
                        elevation = 0.dp,
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                    )
                },
                content = {
                    EnrollTeacherAdminScreenContent(
                        viewModel = viewModel,
                        onBack = onBack,
                        modifier = modifier.padding(it)
                    )
                }
            )

            if (teacherAlreadyExistState == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    MessageBar(
                        message = stringResource(id = R.string.teacher_already_exist),
                        icon = R.drawable.icon_info,
                        onClose = {
                            viewModel.onTeacherAlreadyExistStateAdminChanged(false)
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
fun EnrollTeacherAdminScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    val verticalScroll = rememberScrollState()
    val context = LocalContext.current
    val TAG = "EnrollTeacherAdminScreenContent"

    val teacherEmailEnrollTeacher by viewModel.teacherEmailEnrollTeacher.collectAsState()
    val teacherPasswordEnrollTeacher by viewModel.teacherPasswordEnrollTeacher.collectAsState()
    val teacherConfirmPasswordEnrollTeacher by viewModel.teacherConfirmPasswordEnrollTeacher.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val userByEmailNetwork by viewModel.userByEmailNetwork.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUsernamePref by viewModel.currentUsernamePref.collectAsState()
    val currentSchoolNamePref by viewModel.currentSchoolNamePref.collectAsState()
    val stagedUsersNetwork by viewModel.stagedUsersNetwork.collectAsState()
    val verifiedUsersNetwork by viewModel.verifiedUsersNetwork.collectAsState()


    val scope = rememberCoroutineScope()

    val exception: MutableState<EnrollTeacherException> = remember {
        mutableStateOf(EnrollTeacherException.NoException)
    }

    var stagingListener by remember { mutableStateOf(0) }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(teacherEmailEnrollTeacher) {
        //on enter teacher email, search db if user already exists
        if (teacherEmailEnrollTeacher != null) {
            viewModel.getUserByEmailNetwork(teacherEmailEnrollTeacher ?: "")
        }
    }

    Log.e(
        TAG,
        "EnrollTeacherAdminScreenContent: number of staged teachers ${stagedUsersNetwork.data?.size}",
    )

    Log.e(
        TAG,
        "EnrollTeacherAdminScreenContent: number of verified teachers ${verifiedUsersNetwork.data?.size}",
    )

    LaunchedEffect(Unit) {
        viewModel.getCurrentUsernamePref()
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentSchoolNamePref()
        viewModel.getStagedUsersNetwork(currentSchoolIdPref ?: "")
    }

    LaunchedEffect(stagingListener) {
        viewModel.getStagedUsersNetwork(currentSchoolIdPref ?: "")
        viewModel.getVerifiedUsersNetwork(currentSchoolIdPref ?: "")
    }

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
                    value = teacherEmailEnrollTeacher ?: "",
                    onValueChange = viewModel::onTeacherEmailEnrollTeacherChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.teacher_email),
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
                    singleLine = true,
                    maxLines = 1,
                    isError = exception.value is EnrollTeacherException.EmailException
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = teacherPasswordEnrollTeacher ?: "",
                    onValueChange = viewModel::onTeacherPasswordEnrollTeacherChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.enter_password),
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
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                    maxLines = 1,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Rounded.Visibility else
                            Icons.Rounded.VisibilityOff

                        val description =
                            if (passwordVisible) stringResource(id = R.string.hide_password)
                            else stringResource(id = R.string.show_password)

                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) { Icon(imageVector = image, contentDescription = description) }
                    },
                    isError = exception.value is EnrollTeacherException.PasswordException
                )


                Text(
                    text = stringResource(id = R.string.password_minimum),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )


                Spacer(modifier = Modifier.height(32.dp))


                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = teacherConfirmPasswordEnrollTeacher ?: "",
                    onValueChange = viewModel::onTeacherConfirmPasswordEnrollTeacherChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.confirm_password),
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
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    singleLine = true,
                    maxLines = 1,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Rounded.Visibility else
                            Icons.Rounded.VisibilityOff

                        val description =
                            if (passwordVisible) stringResource(id = R.string.hide_password)
                            else stringResource(id = R.string.show_password)

                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) { Icon(imageVector = image, contentDescription = description) }
                    },
                    isError = exception.value is EnrollTeacherException.ConfirmPasswordException
                )

                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SecondaryTextButton(
                        modifier = Modifier.padding(8.dp),
                        label = stringResource(id = R.string.enroll_more).uppercase(),
                        onClick = {
                            keyboardController?.hide()

                            if (teacherEmailEnrollTeacher == null || teacherEmailEnrollTeacher?.isBlank() == true) {
                                exception.value = EnrollTeacherException.EmailException()
                                return@SecondaryTextButton
                            }

                            if (!isEmailValid(teacherEmailEnrollTeacher ?: "")) {
                                exception.value = EnrollTeacherException.EmailException()
                                return@SecondaryTextButton
                            }

                            if (teacherPasswordEnrollTeacher == null || teacherPasswordEnrollTeacher?.isBlank() == true) {
                                exception.value = EnrollTeacherException.PasswordException()
                                return@SecondaryTextButton
                            }

                            if (teacherConfirmPasswordEnrollTeacher == null || teacherConfirmPasswordEnrollTeacher?.isBlank() == true) {
                                exception.value = EnrollTeacherException.ConfirmPasswordException()
                                return@SecondaryTextButton
                            }

                            if (teacherPasswordEnrollTeacher!!.length < 6) {
                                exception.value = EnrollTeacherException.PasswordException()
                                return@SecondaryTextButton
                            }

                            if (teacherConfirmPasswordEnrollTeacher!!.length < 6) {
                                exception.value = EnrollTeacherException.PasswordException()
                                return@SecondaryTextButton
                            }

                            if (teacherPasswordEnrollTeacher != teacherConfirmPasswordEnrollTeacher) {
                                exception.value = EnrollTeacherException.ConfirmPasswordException()
                                return@SecondaryTextButton
                            }

                            if (teacherEmailEnrollTeacher == userByEmailNetwork.data?.email) {
                                viewModel.onTeacherAlreadyExistStateAdminChanged(true)
                                return@SecondaryTextButton
                            }

                            scope.launch {
                                //save teacher to stage
                                viewModel.saveUserToStage(
                                    UserModel(
                                        userId = UUID.randomUUID().toString(),
                                        email = teacherEmailEnrollTeacher,
                                        password = teacherPasswordEnrollTeacher,
                                        schoolIds = arrayListOf(currentSchoolIdPref ?: ""),
                                        currentRole = UserRole.Teacher.name,
                                        currentSchoolId = currentSchoolIdPref,
                                        roles = arrayListOf(UserRole.Teacher.name),
                                        currentSchoolName = currentSchoolNamePref,
                                        lastModified = todayComputational(),
                                        modifiedBy = currentUsernamePref,
                                    )
                                        .toNetwork(),
                                    onResult = {

                                    }
                                )
                            }.invokeOnCompletion {
                                runnableBlock {
                                    stagingListener++
                                    //clear the fields
                                    viewModel.clearEnrollTeacherFields()
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

                            if (
                                teacherEmailEnrollTeacher == userByEmailNetwork.data?.email &&
                                teacherEmailEnrollTeacher?.isNotBlank() == true
                            ) {
                                viewModel.onTeacherAlreadyExistStateAdminChanged(true)
                                return@PrimaryTextButton
                            }


                            scope.launch {
                                if ((teacherEmailEnrollTeacher?.isNotBlank() == true &&
                                            teacherPasswordEnrollTeacher?.isNotBlank() == true &&
                                            teacherConfirmPasswordEnrollTeacher?.isNotBlank() == true
                                            ) ||
                                    stagedUsersNetwork.data?.isNotEmpty() == true
                                ) {
                                    //process constraints
                                    if (
                                        teacherEmailEnrollTeacher?.isNotBlank() == true &&
                                        teacherPasswordEnrollTeacher?.isNotBlank() == true &&
                                        teacherConfirmPasswordEnrollTeacher?.isNotBlank() == true
                                    ) {
                                        //enroll user
                                        viewModel.signUp(
                                            teacherEmailEnrollTeacher,
                                            teacherPasswordEnrollTeacher,
                                            onResult = { _, exception ->
                                                //process sign up
                                                if (exception.data == AuthExceptionState.UserAlreadyExists) {
                                                    return@signUp
                                                }
                                                if (exception.data == AuthExceptionState.InvalidUserCredentials) {
                                                    return@signUp
                                                }
                                                if (exception.data == AuthExceptionState.InvalidEmail) {
                                                    return@signUp

                                                }

                                                if (exception.data == AuthExceptionState.NetworkProblem) {
                                                    return@signUp
                                                }

                                                //save user to db
                                                viewModel.saveUserAsVerified(
                                                    UserModel(
                                                        userId = UUID.randomUUID().toString(),
                                                        email = teacherEmailEnrollTeacher,
                                                        password = teacherPasswordEnrollTeacher,
                                                        schoolIds = arrayListOf(
                                                            currentSchoolIdPref ?: ""
                                                        ),
                                                        currentRole = UserRole.Teacher.name,
                                                        currentSchoolId = currentSchoolIdPref,
                                                        roles = arrayListOf(UserRole.Teacher.name),
                                                        currentSchoolName = currentSchoolNamePref,
                                                        lastModified = todayComputational(),
                                                        modifiedBy = currentUsernamePref,
                                                    )
                                                        .toNetwork(),
                                                    onResult = {

                                                    }
                                                )
                                            }
                                        )
                                    }

                                    //call an insertion service
                                    val intent = Intent(
                                        context,
                                        EnrollTeachersService::class.java
                                    ).putExtra(
                                        EnrollTeachersServiceExtras.currentSchoolId,
                                        currentSchoolIdPref
                                    )
                                        .setAction(EnrollTeachersServiceActions.ACTION_UPLOAD)

                                    context.startService(intent)
                                }

                            }.invokeOnCompletion {
                                runnableBlock {
                                    stagingListener++
                                    viewModel.clearEnrollTeacherFields()
                                    //close screen
                                    onBack()
                                }
                            }
                        },
                    )
                }
            }
        }

        if (stagedUsersNetwork.data?.isNotEmpty() == true) {
            Card(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 8.dp, bottom = 64.dp, start = 8.dp, end = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 2.dp,
            ) {
                val stagedUsersSorted = stagedUsersNetwork.data?.sortedByDescending {
                    it.lastModified
                }

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    stagedUsersSorted?.forEach { each ->
                        //staged user item
                        StagedTeacherItem(
                            teacher = each.toLocal(),
                            onRemove = {
                                viewModel.deleteUserNetwork(each.toLocal()) {
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


sealed class EnrollTeacherException(val message: String) {
    class EmailException : EnrollTeacherException("Please enter a valid email")
    class PasswordException : EnrollTeacherException("Please enter a valid password")
    class ConfirmPasswordException : EnrollTeacherException("Passwords do not match!")
    object NoException : EnrollTeacherException("")
}

@Composable
fun StagedTeacherItem(
    modifier: Modifier = Modifier,
    teacher: UserModel,
    onRemove: (UserModel) -> Unit,
) {
    val constraints = StagedTeacherItemConstraints(8.dp)
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
                color = Color(generateColorFromClassName(teacher.email ?: "")),
                icon = R.drawable.icon_enroll
            )


            Text(
                modifier = innerModifier.layoutId("className"),
                text = stringResource(id = R.string.pending_teacher),
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
                text = teacher.email ?: "",
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.primary,
            )

            RoundedIconButton(
                modifier = innerModifier.layoutId("manage"),
                onClick = { onRemove(teacher) }, icon = R.drawable.icon_close,
            )
        }
    }
}

private fun StagedTeacherItemConstraints(margin: Dp): ConstraintSet {
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
