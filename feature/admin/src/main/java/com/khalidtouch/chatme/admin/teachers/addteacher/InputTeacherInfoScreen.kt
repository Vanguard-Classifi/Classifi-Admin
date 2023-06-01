package com.khalidtouch.chatme.admin.teachers.addteacher

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiStagingIconButton
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

const val inputTeacherInfoNavigationRoute = "input_teacher_info_navigation_route"


@Composable
fun InputTeacherInfoScreen(
    addTeacherViewModel: AddTeacherViewModel,
) {
    val context = LocalContext.current
    val state by addTeacherViewModel.state.collectAsStateWithLifecycle()
    val mySchool by addTeacherViewModel.observeMySchool.collectAsStateWithLifecycle()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    LazyColumn {
        teacherEmail(
            placeholder = context.getString(R.string.enter_teacher_email),
            value = state.email,
            onValueChange = addTeacherViewModel::onEmailChanged,
            isError = false,
        )

        teacherPassword(
            placeholder = context.getString(R.string.enter_password),
            value = state.password,
            onValueChange = addTeacherViewModel::onPasswordChanged,
            isError = false,
            isPasswordVisible = isPasswordVisible,
            onTogglePasswordVisibility = {
                isPasswordVisible = !it
            },
        )


        teacherVerifyPassword(
            placeholder = context.getString(R.string.confirm_password),
            value = state.confirmPassword,
            onValueChange = addTeacherViewModel::onConfirmPasswordChanged,
            isError = false,
            isPasswordVisible = isConfirmPasswordVisible,
            onTogglePasswordVisibility = {
                isConfirmPasswordVisible = !it
            },
        )

        teacherAddMore(
            onEnrollMoreTeachers = {
                addTeacherViewModel.onStageTeacher(
                    email = state.email,
                    password = state.password,
                    confirmPassword = state.confirmPassword,
                    mySchool = mySchool,
                )
            },
            canAddMoreTeachers = state.canAddMoreTeachers,
        )

        teacherStagingArea(
            stagedTeachers = state.stagedTeachers,
            onClick = {/*TODO -> preview staged teacher*/ },
        )
    }
}

private fun LazyListScope.teacherEmail(
    placeholder: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
) {
    item {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = placeholder.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.outline.copy(0.5f)
                    )
                )
            },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            maxLines = 1,
            isError = isError,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.outline,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            )
        )
        Spacer(Modifier.height(16.dp))
    }
}

private fun LazyListScope.teacherPassword(
    placeholder: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    isPasswordVisible: Boolean = true,
    onTogglePasswordVisibility: (Boolean) -> Unit = {},
) {
    item {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = placeholder.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.outline.copy(0.5f)
                    )
                )
            },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
            ),
            singleLine = true,
            maxLines = 1,
            isError = isError,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.outline,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.outline,
            ),
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val image = if (isPasswordVisible) Icons.Rounded.Visibility else
                    Icons.Rounded.VisibilityOff

                val description =
                    if (isPasswordVisible) stringResource(id = R.string.hide_password)
                    else stringResource(id = R.string.show_password)

                ClassifiIconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.outline.copy(0.5f),
                    ),
                    onClick = { onTogglePasswordVisibility(isPasswordVisible) },
                    icon = {
                        Icon(
                            imageVector = image,
                            contentDescription = description
                        )
                    }
                )
            }
        )
        Spacer(Modifier.height(16.dp))
    }
}


private fun LazyListScope.teacherVerifyPassword(
    placeholder: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    isPasswordVisible: Boolean = true,
    onTogglePasswordVisibility: (Boolean) -> Unit = {},
) {
    item {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = placeholder.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.outline.copy(0.5f)
                    )
                )
            },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            maxLines = 1,
            isError = isError,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.outline,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.outline,
            ),
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val image = if (isPasswordVisible) Icons.Rounded.Visibility else
                    Icons.Rounded.VisibilityOff

                val description =
                    if (isPasswordVisible) stringResource(id = R.string.hide_password)
                    else stringResource(id = R.string.show_password)

                ClassifiIconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.outline.copy(0.5f),
                    ),
                    onClick = { onTogglePasswordVisibility(isPasswordVisible) },
                    icon = {
                        Icon(
                            imageVector = image,
                            contentDescription = description
                        )
                    }
                )
            }
        )
        Spacer(Modifier.height(16.dp))
    }
}


private fun LazyListScope.teacherAddMore(
    onEnrollMoreTeachers: () -> Unit,
    canAddMoreTeachers: Boolean,
) {
    item {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.BottomStart,
        ) {
            ClassifiIconButton(
                enabled = canAddMoreTeachers,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.outline.copy(0.7f),
                ),
                onClick = onEnrollMoreTeachers,
                icon = {
                    Icon(
                        painterResource(id = ClassifiIcons.AddPerson),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
private fun LazyListScope.teacherStagingArea(
    stagedTeachers: List<StageTeacher>,
    onClick: (StageTeacher) -> Unit,
) {
    item {
        FlowRow(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            stagedTeachers.forEachIndexed { index, teacher ->
                ClassifiStagingIconButton(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(
                            0.5f
                        )
                    ),
                    color = Color.Transparent,
                    onClick = { onClick(teacher) }) {
                    Box {
                        Icon(
                            painterResource(id = ClassifiIcons.Personal),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline.copy(
                                0.5f
                            )
                        )

                        Box(
                            Modifier
                                .padding(0.dp)
                                .matchParentSize(),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.outline.copy(
                                        0.5f
                                    )
                                ),
                            )
                        }
                    }
                }
                Spacer(Modifier.width(16.dp))
            }
        }
    }
}


fun NavController.navigateToInputTeacherInfo(
    addTeacherViewModel: AddTeacherViewModel,
) {
    this.navigate(inputTeacherInfoNavigationRoute) {
        launchSingleTop = true
        popUpTo(inputTeacherInfoNavigationRoute)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.inputTeacherInfo(
    addTeacherViewModel: AddTeacherViewModel,
) {
    composable(
        route = inputTeacherInfoNavigationRoute,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200)
            )
        }
    ) {
        InputTeacherInfoScreen(addTeacherViewModel = addTeacherViewModel)
    }
}