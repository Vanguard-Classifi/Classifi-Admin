package com.vanguard.classifiadmin.onboarding

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiButton
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.vanguard.classifiadmin.R

@Composable
fun CreateAccountScreen(
    onLogin: () -> Unit,
    createAccountUiState: CreateAccountUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
) {
    val TAG = "CreateAcct"
    val context = LocalContext.current
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false)}



    LazyColumn {
        when(createAccountUiState){
            is CreateAccountUiState.Loading -> Unit
            is CreateAccountUiState.Success -> {
                item {
                    Box {
                        Text(
                            text = stringResource(id = R.string.join_classifi_as_a_school_owner),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }

                emailItem(
                    value = (createAccountUiState as CreateAccountUiState.Success).data.email,
                    onValueChange = onEmailChanged,
                    placeholder = context.getString(R.string.enter_your_email)
                )

                passwordItem(
                    value = (createAccountUiState as CreateAccountUiState.Success).data.password,
                    onValueChange = onPasswordChanged,
                    placeholder = context.getString(R.string.password),
                    isPasswordVisible = isPasswordVisible,
                    onTogglePasswordVisibility = {
                        isPasswordVisible = !it
                    }
                )

                confirmPasswordItem(
                    value = (createAccountUiState as CreateAccountUiState.Success).data.confirm,
                    onValueChange = onConfirmPasswordChanged,
                    isPasswordVisible = isConfirmPasswordVisible,
                    placeholder = context.getString(R.string.confirm_password),
                    onTogglePasswordVisibility = {
                        isConfirmPasswordVisible = !it
                    }
                )

                alreadyHaveAnAccountItem(
                    onLogin = onLogin
                )
            }
        }
    }

    AnimatedVisibility(
        visible = createAccountUiState is CreateAccountUiState.Loading,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            ClassifiLoadingWheel()
        }
    }
}

fun LazyListScope.alreadyHaveAnAccountItem(
    onLogin: () -> Unit,
) {
    item {
        Row {
            Text(
                text = stringResource(id = R.string.already_have_account),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.log_in_instead),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = onLogin,
                    role = Role.Button,
                )
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

private fun LazyListScope.confirmPasswordItem(
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

private fun LazyListScope.passwordItem(
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


fun LazyListScope.emailItem(
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

const val createAccountNavigationRoute = "create_account_navigation_route"

fun NavController.navigateToCreateAccount(
    navOptions: NavOptions? = null,
    updateCurrentOnboardingDestination: (destination: OnboardingDestination) -> Unit,
) {
    this.navigate(createAccountNavigationRoute, navOptions)
    updateCurrentOnboardingDestination(OnboardingDestination.CREATE_ACCOUNT)
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.createAccountScreen(
    onLogin: () -> Unit,
    createAccountUiState: CreateAccountUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
) {
    composable(
        route = createAccountNavigationRoute,
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
        CreateAccountScreen(
            onLogin = onLogin,
           createAccountUiState = createAccountUiState,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            onConfirmPasswordChanged = onConfirmPasswordChanged,
        )
    }
}