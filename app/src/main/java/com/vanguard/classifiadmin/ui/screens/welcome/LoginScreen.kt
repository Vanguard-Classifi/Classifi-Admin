package com.vanguard.classifiadmin.ui.screens.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.isEmailValid
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.ui.theme.Green100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay

const val LOGIN_SCREEN = "login_screen"

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    LoginScreenContent(
        viewModel = viewModel,
    )
}


@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val innerModifier = Modifier
    val emailLogin by viewModel.emailLogin.collectAsState()
    val passwordLogin by viewModel.passwordLogin.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var loginErrorState: LoginErrorState? by remember { mutableStateOf(null) }
    val constraints = loginScreenContentConstraints(8.dp)

    LaunchedEffect(loginErrorState) {
        if (loginErrorState != null) {
            delay(3000)
            loginErrorState = null
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
        ) {
            ConstraintLayout(
                modifier = Modifier,
                constraintSet = constraints,
            ) {

                Branding(
                    modifier = innerModifier.layoutId("brand")
                )

                Text(
                    text = stringResource(id = R.string.welcome_back),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = innerModifier.layoutId("welcome")
                )



                OutlinedTextField(
                    value = emailLogin ?: "",
                    onValueChange = viewModel::onEmailLoginChanged,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.enter_your_email),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary.copy(0.5f)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = true,
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.primary.copy(0.5f),
                        backgroundColor = MaterialTheme.colors.surface,
                        cursorColor = MaterialTheme.colors.primary,
                        errorCursorColor = MaterialTheme.colors.error,
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = innerModifier.layoutId("email"),
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    isError =
                    loginErrorState == LoginErrorState.InvalidEmail
                            || loginErrorState == LoginErrorState.InvalidUserCredentials,
                )

                OutlinedTextField(
                    value = passwordLogin ?: "",
                    onValueChange = viewModel::onPasswordLoginChanged,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.enter_your_password),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary.copy(0.5f)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.primary.copy(0.5f),
                        backgroundColor = MaterialTheme.colors.surface,
                        cursorColor = MaterialTheme.colors.primary,
                        errorCursorColor = MaterialTheme.colors.error,
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = innerModifier.layoutId("password"),
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
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
                    isError =
                    loginErrorState == LoginErrorState.InvalidPassword ||
                            loginErrorState == LoginErrorState.InvalidUserCredentials,
                )


                TextRowWithClickable(
                    unClickable = stringResource(id = R.string.cant_remember),
                    clickable = stringResource(id = R.string.reset_password),
                    onClick = {
                        /*reset password*/
                    },
                    modifier = innerModifier.layoutId("resetPasswordRow")
                )

                PrimaryTextButton(
                    label = stringResource(id = R.string.login),
                    onClick = {
                        //login computation
                        if (emailLogin == null || emailLogin?.isBlank() == true) {
                            loginErrorState = LoginErrorState.InvalidEmail
                            return@PrimaryTextButton
                        }

                        if (!isEmailValid(emailLogin ?: "")) {
                            loginErrorState = LoginErrorState.InvalidEmail
                            return@PrimaryTextButton
                        }

                        if (passwordLogin == null || passwordLogin?.isBlank() == true) {
                            loginErrorState = LoginErrorState.InvalidPassword
                            return@PrimaryTextButton
                        }

                        viewModel.signIn(
                            emailLogin,
                            passwordLogin,
                            onResult = { error ->
                                when (error) {
                                    Resource.Success(AuthExceptionState.NetworkProblem) -> {
                                        loginErrorState = LoginErrorState.NetworkError
                                        return@signIn
                                    }

                                    Resource.Success(AuthExceptionState.InvalidUser) -> {
                                        loginErrorState = LoginErrorState.InvalidUserCredentials
                                        return@signIn
                                    }

                                    Resource.Success(AuthExceptionState.InvalidEmail) -> {
                                        loginErrorState = LoginErrorState.InvalidEmail
                                        return@signIn
                                    }

                                    Resource.Success(AuthExceptionState.InvalidUserCredentials) -> {
                                        loginErrorState = LoginErrorState.InvalidUserCredentials
                                        return@signIn
                                    }

                                    else -> {
                                        loginErrorState = LoginErrorState.NetworkError
                                        return@signIn
                                    }
                                }
                            }
                        )

                        //move forward

                    },
                    modifier = innerModifier.layoutId("loginBtn")
                )


                TextRowWithClickable(
                    unClickable = stringResource(id = R.string.join_as_admin_or_school_director),
                    clickable = stringResource(id = R.string.register_school),
                    onClick = {
                        /*register school*/
                    },
                    modifier = innerModifier.layoutId("createSchool")
                )

                SocialMediaHandles(
                    onEnterFacebook = {
                        /*on*/
                    },
                    modifier = innerModifier.layoutId("socialMedia")
                )

                SupportRow(
                    modifier = innerModifier.layoutId("support"),
                )

                Copyright(year = "2023", modifier = innerModifier.layoutId("copyright"))


            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd,
            ) {
                AnimatedVisibility(
                    visible = loginErrorState != null,
                    enter = slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 20)
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 20)
                    ),
                ) {
                    //on tap message content
                    val message: String = when (loginErrorState) {
                        LoginErrorState.InvalidUserCredentials -> loginErrorState?.message ?: ""
                        LoginErrorState.InvalidEmail -> loginErrorState?.message ?: ""
                        LoginErrorState.InvalidPassword -> loginErrorState?.message ?: ""
                        LoginErrorState.NetworkError -> loginErrorState?.message ?: ""
                        else -> stringResource(id = R.string.something_went_wrong)
                    }

                    MessageBar(
                        message = message,
                        onClose = {
                            loginErrorState = null
                        }
                    )
                }

            }
        }
    }
}


private fun loginScreenContentConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val brand = createRefFor("brand")
        val welcome = createRefFor("welcome")
        val email = createRefFor("email")
        val password = createRefFor("password")
        val resetPasswordRow = createRefFor("resetPasswordRow")
        val loginBtn = createRefFor("loginBtn")
        val createSchool = createRefFor("createSchool")
        val socialMedia = createRefFor("socialMedia")
        val support = createRefFor("support")
        val copyright = createRefFor("copyright")

        constrain(brand) {
            top.linkTo(parent.top, 8.dp)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
        }

        constrain(welcome) {
            top.linkTo(brand.bottom, 16.dp)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
        }

        constrain(email) {
            top.linkTo(welcome.bottom, 22.dp)
            start.linkTo(parent.start, 8.dp)
            end.linkTo(parent.end, 8.dp)
            width = Dimension.fillToConstraints
        }

        constrain(password) {
            top.linkTo(email.bottom, 16.dp)
            start.linkTo(email.start, 0.dp)
            end.linkTo(email.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(resetPasswordRow) {
            top.linkTo(password.bottom, 12.dp)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
        }

        constrain(loginBtn) {
            top.linkTo(resetPasswordRow.bottom, 48.dp)
            start.linkTo(parent.start, 8.dp)
            end.linkTo(parent.end, 8.dp)
            width = Dimension.fillToConstraints
        }

        constrain(createSchool) {
            top.linkTo(loginBtn.bottom, 16.dp)
            start.linkTo(loginBtn.start, 0.dp)
            end.linkTo(loginBtn.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(socialMedia) {
            top.linkTo(createSchool.bottom, 140.dp)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
        }

        constrain(support) {
            top.linkTo(socialMedia.bottom, 12.dp)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(copyright) {
            top.linkTo(support.bottom, 8.dp)
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
            width = Dimension.fillToConstraints
        }
    }

}

@Composable
fun Branding(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
) {

    Card(
        modifier = modifier
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp, shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_branding_big),
                tint = color,
                contentDescription = stringResource(id = R.string.branding),
                modifier = modifier
                    .padding(8.dp)
                    .size(28.dp)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 22.sp,
                color = color,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }


}

@Composable
fun SupportRow(
    modifier: Modifier = Modifier,
    supports: List<SupportType> = SupportType.values().toList(),
    onSelect: (SupportType) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        supports.forEach { support ->
            Text(
                text = support.title,
                fontSize = 12.sp,
                color = Black100.copy(0.3f),
                maxLines = 1,
                modifier = modifier
                    .clickable { onSelect(support) }
                    .padding(horizontal = 16.dp)
            )
        }
    }
}


@Composable
fun Copyright(
    modifier: Modifier = Modifier,
    year: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_copyright),
            tint = Black100.copy(0.3f),
            contentDescription = stringResource(id = R.string.facebook),
            modifier = modifier
                .padding(4.dp)
                .size(14.dp)
        )

        Text(
            text = "$year, Classifi. All rights reserved.",
            fontSize = 12.sp,
            color = Black100.copy(0.3f),
            maxLines = 1,
        )
    }
}


@Composable
fun SocialMediaHandles(
    modifier: Modifier = Modifier,
    onEnterFacebook: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onEnterFacebook,
            modifier = modifier.clip(CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_facebook),
                tint = Black100.copy(0.5f),
                contentDescription = stringResource(id = R.string.facebook),
                modifier = modifier
                    .padding(8.dp)
                    .size(24.dp)
            )
        }
    }
}


@Composable
fun TextRowWithClickable(
    modifier: Modifier = Modifier,
    unClickable: String,
    clickable: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = unClickable,
            fontSize = 12.sp,
            color = Black100,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.padding(2.dp)
        )
        Text(
            text = clickable,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Green100,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .clickable { onClick() }
                .padding(2.dp)
        )

    }
}


@Composable
@Preview
private fun BrandingPreview() {
    Branding()
}

@Composable
@Preview
private fun TextRowWithClickablePreview() {
    TextRowWithClickable(
        unClickable = "New to Classifi?",
        clickable = "Open School",
        onClick = {}
    )
}

@Composable
@Preview
private fun CopyrightPreview() {
    Copyright(
        year = "2023"
    )
}


enum class SupportType(val title: String) {
    AboutUs("About Us"),
    HelpFaq("Help & FAQs"),
    PrivacyPolicy("Privacy Policy")
}

enum class LoginErrorState(val message: String) {
    InvalidEmail("Please check your email address"),
    InvalidPassword("Please check your password"),
    InvalidUserCredentials("Please ensure to enter the correct information"),
    NetworkError("Please check your network and try again"),
}