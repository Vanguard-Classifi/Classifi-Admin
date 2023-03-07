package com.vanguard.classifiadmin.ui.screens.admin

import android.content.Intent
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
import com.vanguard.classifiadmin.domain.services.EnrollStudentsService
import com.vanguard.classifiadmin.domain.services.EnrollStudentsServiceActions
import com.vanguard.classifiadmin.domain.services.EnrollStudentsServiceExtras
import com.vanguard.classifiadmin.domain.services.EnrollTeachersService
import com.vanguard.classifiadmin.domain.services.EnrollTeachersServiceActions
import com.vanguard.classifiadmin.domain.services.EnrollTeachersServiceExtras
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.SecondaryTextButton
import com.vanguard.classifiadmin.ui.components.StagedItemIcon
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val ENROLL_STUDENT_ADMIN_SCREEN = "enroll_student_screen"

@Composable
fun EnrollStudentAdminScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
) {
    val enrollStudentException by viewModel.enrollStudentException.collectAsState()
    val studentAlreadyExistState by viewModel.studentAlreadyExistStateAdmin.collectAsState()

    LaunchedEffect(studentAlreadyExistState) {
        if (studentAlreadyExistState == true) {
            delay(3000)
            viewModel.onStudentAlreadyExistStateAdminChanged(false)
        }
    }

    LaunchedEffect(enrollStudentException) {
        if (enrollStudentException !is EnrollStudentException.NoException) {
            delay(3000)
            viewModel.onEnrollStudentExceptionChanged(EnrollStudentException.NoException)
        }
    }


    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier) {
            val maxWidth = maxWidth
            val maxHeight = maxHeight

            Scaffold(
                modifier = modifier,
                topBar = {
                    ChildTopBar(
                        onBack = onBack,
                        heading = stringResource(id = R.string.enroll_students),
                        elevation = 0.dp,
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                    )
                },
                content = {
                    EnrollStudentAdminScreenContent(
                        modifier = modifier.padding(it),
                        viewModel = viewModel,
                        onBack = onBack,
                    )
                },
            )

            if (studentAlreadyExistState == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    MessageBar(
                        message = stringResource(id = R.string.user_already_exists),
                        icon = R.drawable.icon_info,
                        onClose = {
                            viewModel.onStudentAlreadyExistStateAdminChanged(false)
                        },
                        maxWidth = maxWidth,
                        modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                    )
                }
            }

            if (enrollStudentException !is EnrollStudentException.NoException) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    MessageBar(
                        message = enrollStudentException.message,
                        icon = R.drawable.icon_info,
                        onClose = {
                            viewModel.onEnrollStudentExceptionChanged(EnrollStudentException.NoException)
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
fun EnrollStudentAdminScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    val TAG = "EnrollStudentAdminScreenContent"
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val verticalScroll = rememberScrollState()
    var stagingListener by remember { mutableStateOf(0) }
    var passwordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val enrollStudentException by viewModel.enrollStudentException.collectAsState()
    val studentEmailEnrollStudent by viewModel.studentEmailEnrollStudent.collectAsState()
    val studentPasswordEnrollStudent by viewModel.studentPasswordEnrollStudent.collectAsState()
    val studentConfirmPasswordEnrollStudent by viewModel.studentConfirmPasswordEnrollStudent.collectAsState()
    val userByEmailNetwork by viewModel.userByEmailNetwork.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUsernamePref by viewModel.currentUsernamePref.collectAsState()
    val currentSchoolNamePref by viewModel.currentSchoolNamePref.collectAsState()
    val stagedStudentsNetwork by viewModel.stagedStudentsNetwork.collectAsState()


    LaunchedEffect(studentEmailEnrollStudent) {
        if (studentEmailEnrollStudent != null) {
            viewModel.getUserByEmailNetwork(studentEmailEnrollStudent ?: "")
        }
    }

    LaunchedEffect(stagingListener) {
        viewModel.getStagedStudentsNetwork(currentSchoolIdPref ?: "")
    }


    LaunchedEffect(Unit) {
        viewModel.getCurrentUsernamePref()
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentSchoolNamePref()
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
                    value = studentEmailEnrollStudent ?: "",
                    onValueChange = viewModel::onStudentEmailEnrollStudentChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.student_email),
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
                    isError =
                    enrollStudentException is EnrollStudentException.EmptyEmailException ||
                            enrollStudentException is EnrollStudentException.InvalidEmailException
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = studentPasswordEnrollStudent ?: "",
                    onValueChange = viewModel::onStudentPasswordEnrollStudentChanged,
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
                    isError = enrollStudentException is EnrollStudentException.EmptyPasswordException ||
                            enrollStudentException is EnrollStudentException.PasswordMismatchException ||
                            enrollStudentException is EnrollStudentException.PasswordLengthException
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
                    value = studentConfirmPasswordEnrollStudent ?: "",
                    onValueChange = viewModel::onStudentConfirmPasswordEnrollStudentChanged,
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
                    isError = enrollStudentException is EnrollStudentException.PasswordMismatchException
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

                            if (studentEmailEnrollStudent == null || studentEmailEnrollStudent?.isBlank() == true) {
                                //empty email exception
                                viewModel.onEnrollStudentExceptionChanged(EnrollStudentException.EmptyEmailException())
                                return@SecondaryTextButton
                            }

                            if (!isEmailValid(studentEmailEnrollStudent ?: "")) {
                                //invalid email exception
                                viewModel.onEnrollStudentExceptionChanged(EnrollStudentException.InvalidEmailException())
                                return@SecondaryTextButton
                            }

                            if (studentPasswordEnrollStudent == null || studentPasswordEnrollStudent?.isBlank() == true) {
                                //empty password exception
                                viewModel.onEnrollStudentExceptionChanged(EnrollStudentException.EmptyPasswordException())
                                return@SecondaryTextButton
                            }

                            if (studentPasswordEnrollStudent!!.length < 6) {
                                // password length exception
                                viewModel.onEnrollStudentExceptionChanged(EnrollStudentException.PasswordLengthException())
                                return@SecondaryTextButton
                            }


                            if (studentConfirmPasswordEnrollStudent == null || studentConfirmPasswordEnrollStudent?.isBlank() == true) {
                                //empty confirm password exception
                                viewModel.onEnrollStudentExceptionChanged(EnrollStudentException.PasswordMismatchException())
                                return@SecondaryTextButton
                            }


                            if (studentConfirmPasswordEnrollStudent!!.length < 6) {
                                // confirm password length exception
                                viewModel.onEnrollStudentExceptionChanged(EnrollStudentException.PasswordLengthException())
                                return@SecondaryTextButton
                            }

                            if (studentPasswordEnrollStudent != studentConfirmPasswordEnrollStudent) {
                                // password mismatch exception
                                viewModel.onEnrollStudentExceptionChanged(EnrollStudentException.PasswordMismatchException())
                                return@SecondaryTextButton
                            }

                            if (studentEmailEnrollStudent == userByEmailNetwork.data?.email) {
                                viewModel.onStudentAlreadyExistStateAdminChanged(true)
                                return@SecondaryTextButton
                            }

                            scope.launch {
                                //save teacher to stage
                                viewModel.saveUserToStage(
                                    UserModel(
                                        userId = UUID.randomUUID().toString(),
                                        email = studentEmailEnrollStudent,
                                        password = studentPasswordEnrollStudent,
                                        schoolIds = arrayListOf(currentSchoolIdPref ?: ""),
                                        currentRole = UserRole.Student.name,
                                        currentSchoolId = currentSchoolIdPref,
                                        roles = arrayListOf(UserRole.Student.name),
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
                                    viewModel.clearEnrollStudentFields()
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
                                studentEmailEnrollStudent == userByEmailNetwork.data?.email &&
                                studentEmailEnrollStudent?.isNotBlank() == true
                            ) {
                                viewModel.onStudentAlreadyExistStateAdminChanged(true)
                                return@PrimaryTextButton
                            }


                            scope.launch {
                                if ((studentEmailEnrollStudent?.isNotBlank() == true &&
                                            studentPasswordEnrollStudent?.isNotBlank() == true &&
                                            studentConfirmPasswordEnrollStudent?.isNotBlank() == true
                                            ) ||
                                    stagedStudentsNetwork.data?.isNotEmpty() == true
                                ) {
                                    //process constraints
                                    if (
                                        studentEmailEnrollStudent?.isNotBlank() == true &&
                                        studentPasswordEnrollStudent?.isNotBlank() == true &&
                                        studentConfirmPasswordEnrollStudent?.isNotBlank() == true
                                    ) {
                                        //enroll user
                                        viewModel.signUp(
                                            studentEmailEnrollStudent,
                                            studentPasswordEnrollStudent,
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
                                                        email = studentEmailEnrollStudent,
                                                        password = studentPasswordEnrollStudent,
                                                        schoolIds = arrayListOf(
                                                            currentSchoolIdPref ?: ""
                                                        ),
                                                        currentRole = UserRole.Student.name,
                                                        currentSchoolId = currentSchoolIdPref,
                                                        roles = arrayListOf(UserRole.Student.name),
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
                                        EnrollStudentsService::class.java
                                    ).putExtra(
                                        EnrollStudentsServiceExtras.currentSchoolId,
                                        currentSchoolIdPref
                                    )
                                        .setAction(EnrollStudentsServiceActions.ACTION_UPLOAD)

                                    context.startService(intent)
                                }

                            }.invokeOnCompletion {
                                runnableBlock {
                                    stagingListener++
                                    viewModel.clearEnrollStudentFields()
                                    //close screen
                                    onBack()
                                }
                            }
                        },
                    )


                }

            }
        }


        if (stagedStudentsNetwork.data?.isNotEmpty() == true) {
            Card(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 8.dp, bottom = 64.dp, start = 8.dp, end = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 2.dp,
            ) {
                val stagedStudentsSorted = stagedStudentsNetwork.data?.sortedByDescending {
                    it.lastModified
                }

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    stagedStudentsSorted?.forEach { each ->
                        //staged user item
                        StagedStudentItem(
                            student = each.toLocal(),
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


sealed class EnrollStudentException(val message: String) {
    class EmptyEmailException : EnrollStudentException("Email cannot be blank")
    class EmptyPasswordException : EnrollStudentException("Please enter a valid password")
    class InvalidEmailException : EnrollStudentException("Please enter a valid email")
    class PasswordLengthException : EnrollStudentException("Password must be at least 6 characters")
    class PasswordMismatchException : EnrollStudentException("Passwords do not match!")
    object NoException : EnrollStudentException("")
}


@Composable
fun StagedStudentItem(
    modifier: Modifier = Modifier,
    student: UserModel,
    onRemove: (UserModel) -> Unit,
) {
    val constraints = StagedStudentItemConstraints(8.dp)
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
                color = Color(generateColorFromClassName(student.email ?: "")),
                icon = R.drawable.icon_student_cap
            )


            Text(
                modifier = innerModifier.layoutId("className"),
                text = stringResource(id = R.string.pending_student),
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
                text = student.email ?: "",
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.primary,
            )

            RoundedIconButton(
                modifier = innerModifier.layoutId("manage"),
                onClick = { onRemove(student) }, icon = R.drawable.icon_close,
            )
        }
    }
}

private fun StagedStudentItemConstraints(margin: Dp): ConstraintSet {
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
