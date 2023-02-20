package com.vanguard.classifiadmin.ui.screens.welcome


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.SchoolModel
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.UserRole
import com.vanguard.classifiadmin.domain.helpers.today
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithCloseButtonOnly
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.SuccessBar
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val CREATE_SCHOOL_SCREEN = "create_school_screen"

@Composable
fun CreateSchoolScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onSignUpCompleted: () -> Unit,
) {
    Surface(modifier = Modifier) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithCloseButtonOnly(
                    onClose = onBack,
                )
            },
            content = { padding ->
                CreateSchoolScreenContent(
                    modifier = modifier.padding(padding),
                    viewModel = viewModel,
                    onLogin = onLogin,
                    onSignUpCompleted = onSignUpCompleted,
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateSchoolScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onLogin: () -> Unit,
    onSignUpCompleted: () -> Unit,
) {
    val innerModifier = Modifier
    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }
    var signUpCompletedState by remember { mutableStateOf(false) }
    val constraints = CreateSchoolScreenContentConstraints(8.dp)
    var createSchoolErrorState: CreateSchoolErrorState? by remember { mutableStateOf(null) }
    val verticalScroll = rememberScrollState()

    val fullNameCreateSchool by viewModel.fullNameCreateSchool.collectAsState()
    val schoolNameCreateSchool by viewModel.schoolNameCreateSchool.collectAsState()
    val emailCreateSchool by viewModel.emailCreateSchool.collectAsState()
    val phoneCreateSchool by viewModel.phoneCreateSchool.collectAsState()
    val passwordCreateSchool by viewModel.passwordCreateSchool.collectAsState()

    LaunchedEffect(createSchoolErrorState) {
        if(createSchoolErrorState != null) {
            delay(3000)
            createSchoolErrorState = null
        }
    }

    LaunchedEffect(signUpCompletedState) {
        if(signUpCompletedState) {
            delay(2000)
            signUpCompletedState = false
            //go to dashboard
            onSignUpCompleted()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
        ) {
            val maxWidth = maxWidth

            Column(modifier = Modifier.verticalScroll(verticalScroll)) {

                Branding(
                    modifier = Modifier
                )

                Card(
                    modifier = modifier
                        .clip(RoundedCornerShape(16.dp))
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = 2.dp,
                ) {
                    ConstraintLayout(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 28.dp),
                        constraintSet = constraints,
                    ) {
                        Text(
                            text = stringResource(id = R.string.create_school_account),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colors.primary,
                            modifier = innerModifier.layoutId("header")
                        )


                        OutlinedTextField(
                            value = fullNameCreateSchool ?: "",
                            onValueChange = viewModel::onFullNameCreateSchoolChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_your_full_name),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary.copy(0.5f)
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(
                                autoCorrect = true,
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colors.primary.copy(0.5f),
                                backgroundColor = MaterialTheme.colors.surface,
                                cursorColor = MaterialTheme.colors.primary,
                                errorCursorColor = MaterialTheme.colors.error,
                            ),
                            singleLine = true,
                            maxLines = 1,
                            modifier = innerModifier.layoutId("fullname"),
                            textStyle = TextStyle(
                                color = MaterialTheme.colors.primary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            isError = createSchoolErrorState == CreateSchoolErrorState.InvalidUsername
                        )

                        OutlinedTextField(
                            value = schoolNameCreateSchool ?: "",
                            onValueChange = viewModel::onSchoolNameCreateSchoolChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_school_name),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary.copy(0.5f)
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(
                                autoCorrect = true,
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colors.primary.copy(0.5f),
                                backgroundColor = MaterialTheme.colors.surface,
                                cursorColor = MaterialTheme.colors.primary,
                                errorCursorColor = MaterialTheme.colors.error,
                            ),
                            singleLine = true,
                            maxLines = 1,
                            modifier = innerModifier.layoutId("school"),
                            textStyle = TextStyle(
                                color = MaterialTheme.colors.primary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            isError = createSchoolErrorState == CreateSchoolErrorState.InvalidSchoolName
                        )

                        OutlinedTextField(
                            value = emailCreateSchool ?: "",
                            onValueChange = viewModel::onEmailCreateSchoolChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_email_address),
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
                            isError = createSchoolErrorState == CreateSchoolErrorState.InvalidEmail
                        )


                        OutlinedTextField(
                            value = phoneCreateSchool ?: "",
                            onValueChange = viewModel::onPhoneCreateSchoolChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_phone),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary.copy(0.5f)
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(
                                autoCorrect = true,
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Phone
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colors.primary.copy(0.5f),
                                backgroundColor = MaterialTheme.colors.surface,
                                cursorColor = MaterialTheme.colors.primary,
                                errorCursorColor = MaterialTheme.colors.error,
                            ),
                            singleLine = true,
                            maxLines = 1,
                            modifier = innerModifier.layoutId("phone"),
                            textStyle = TextStyle(
                                color = MaterialTheme.colors.primary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            isError = createSchoolErrorState == CreateSchoolErrorState.InvalidPhoneNumber
                        )



                        OutlinedTextField(
                            value = passwordCreateSchool ?: "",
                            onValueChange = viewModel::onPasswordCreateSchoolChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.create_password),
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
                            isError = createSchoolErrorState == CreateSchoolErrorState.InvalidPassword
                        )

                        PrimaryTextButton(
                            label = stringResource(id = R.string.create_account),
                            onClick = {
                                if (fullNameCreateSchool == null || fullNameCreateSchool?.isBlank() == true) {
                                    createSchoolErrorState = CreateSchoolErrorState.InvalidUsername
                                    return@PrimaryTextButton
                                }

                                if (schoolNameCreateSchool == null || schoolNameCreateSchool?.isBlank() == true) {
                                    createSchoolErrorState =
                                        CreateSchoolErrorState.InvalidSchoolName
                                    return@PrimaryTextButton
                                }

                                if (emailCreateSchool == null || emailCreateSchool?.isBlank() == true) {
                                    createSchoolErrorState = CreateSchoolErrorState.InvalidEmail
                                    return@PrimaryTextButton
                                }

                                if (phoneCreateSchool == null || phoneCreateSchool?.isBlank() == true) {
                                    createSchoolErrorState =
                                        CreateSchoolErrorState.InvalidPhoneNumber
                                    return@PrimaryTextButton
                                }

                                if (passwordCreateSchool == null || passwordCreateSchool?.isBlank() == true) {
                                    createSchoolErrorState = CreateSchoolErrorState.InvalidPassword
                                    return@PrimaryTextButton
                                }

                                viewModel.signUp(
                                    emailCreateSchool,
                                    passwordCreateSchool,
                                    onResult = { user, error ->
                                        when (error) {
                                            Resource.Success(AuthExceptionState.UserAlreadyExists) -> {
                                                createSchoolErrorState =
                                                    CreateSchoolErrorState.AlreadyExistingEmail
                                                return@signUp
                                            }

                                            Resource.Success(AuthExceptionState.InvalidUserCredentials) -> {
                                                createSchoolErrorState =
                                                    CreateSchoolErrorState.InvalidCredentials
                                                return@signUp
                                            }

                                            Resource.Success(AuthExceptionState.InvalidEmail) -> {
                                                createSchoolErrorState =
                                                    CreateSchoolErrorState.InvalidEmail
                                                return@signUp
                                            }

                                            Resource.Success(AuthExceptionState.NetworkProblem) -> {
                                                createSchoolErrorState =
                                                    CreateSchoolErrorState.Network
                                                return@signUp
                                            }

                                            else -> {
                                                createSchoolErrorState = null
                                                //do your thing
                                                scope.launch {
                                                    //save user to db
                                                    val schoolId = UUID.randomUUID().toString()
                                                    val currentRole = UserRole.SuperAdmin.name
                                                    val superAdminUser = UserModel(
                                                        userId = user.data?.uid ?: "",
                                                        email = emailCreateSchool,
                                                        fullname = fullNameCreateSchool,
                                                        currentRole = currentRole,
                                                        roles = arrayListOf(currentRole),
                                                        schoolIds = arrayListOf(schoolId),
                                                        currentSchoolId = schoolId,
                                                        phone = phoneCreateSchool,
                                                        lastModified = today(),
                                                    )
                                                    //save school
                                                    val newSchool = SchoolModel(
                                                        schoolId = schoolId,
                                                        schoolName = schoolNameCreateSchool,
                                                        superAdminId = user.data?.uid ?: "",
                                                        dateCreated = today(),
                                                        lastModified = today(),
                                                    )

                                                    viewModel.saveUserNetwork(
                                                        user = superAdminUser,
                                                        onResult = {}
                                                    )

                                                    viewModel.saveSchoolNetwork(
                                                        school = newSchool,
                                                        onResult = {}
                                                    )
                                                }

                                                //show completed dialog
                                                signUpCompletedState = true

                                            }
                                        }
                                    }
                                )

                            },
                            modifier = innerModifier.layoutId("createBtn")
                        )

                        TextRowWithClickable(
                            unClickable = stringResource(id = R.string.join_an_existing_school_instead),
                            clickable = stringResource(id = R.string.join),
                            onClick = onLogin,
                            modifier = innerModifier.layoutId("login")
                        )
                    }
                }

            }


            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                AnimatedVisibility(
                    visible = createSchoolErrorState != null,
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
                    val message: String = when (createSchoolErrorState) {
                        CreateSchoolErrorState.InvalidEmail -> createSchoolErrorState?.message ?: ""
                        CreateSchoolErrorState.InvalidUsername -> createSchoolErrorState?.message
                            ?: ""

                        CreateSchoolErrorState.InvalidSchoolName -> createSchoolErrorState?.message
                            ?: ""

                        CreateSchoolErrorState.InvalidPassword -> createSchoolErrorState?.message
                            ?: ""

                        CreateSchoolErrorState.InvalidPhoneNumber -> createSchoolErrorState?.message
                            ?: ""

                        CreateSchoolErrorState.Network -> createSchoolErrorState?.message ?: ""
                        CreateSchoolErrorState.AlreadyExistingEmail -> createSchoolErrorState?.message
                            ?: ""

                        else -> stringResource(id = R.string.something_went_wrong)
                    }

                    MessageBar(
                        message = message,
                        onClose = {
                            createSchoolErrorState = null
                        },
                        maxWidth = maxWidth
                    )
                }

            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                AnimatedVisibility(
                    visible = signUpCompletedState,
                    enter = slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 20)
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 20)
                    ),
                ) {
                   SuccessBar(
                       message = stringResource(id = R.string.account_creation_completed),
                       maxWidth = maxWidth,
                   )
                }

            }

        }
    }
}

private fun CreateSchoolScreenContentConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val header = createRefFor("header")
        val fullname = createRefFor("fullname")
        val school = createRefFor("school")
        val email = createRefFor("email")
        val phone = createRefFor("phone")
        val password = createRefFor("password")
        val createBtn = createRefFor("createBtn")
        val login = createRefFor("login")

        constrain(header) {
            top.linkTo(parent.top, 16.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
        }

        constrain(fullname) {
            top.linkTo(header.bottom, 28.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            width = Dimension.fillToConstraints
        }

        constrain(school) {
            top.linkTo(fullname.bottom, 16.dp)
            start.linkTo(fullname.start, 0.dp)
            end.linkTo(fullname.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(email) {
            top.linkTo(school.bottom, 16.dp)
            start.linkTo(school.start, 0.dp)
            end.linkTo(school.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(phone) {
            top.linkTo(email.bottom, 16.dp)
            start.linkTo(email.start, 0.dp)
            end.linkTo(email.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(password) {
            top.linkTo(phone.bottom, 16.dp)
            start.linkTo(phone.start, 0.dp)
            end.linkTo(phone.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(createBtn) {
            top.linkTo(password.bottom, 42.dp)
            start.linkTo(password.start, 0.dp)
            end.linkTo(password.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(login) {
            top.linkTo(createBtn.bottom, 16.dp)
            start.linkTo(createBtn.start, 0.dp)
            end.linkTo(createBtn.end, 0.dp)
        }
    }
}


@Composable
fun FlagWithCode(
    modifier: Modifier = Modifier,
    code: String,
    expanded: Boolean = false,
    color: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable { onClick() }
            .clip(CircleShape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = code,
            fontSize = 12.sp,
            color = color.copy(0.5f),
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(4.dp)
        )

        Icon(
            painter = painterResource(id = if (expanded) R.drawable.icon_arrow_up else R.drawable.icon_arrow_down),
            contentDescription = stringResource(id = R.string.indicator),
            modifier = modifier
                .size(28.dp)
                .padding(4.dp),
            tint = color.copy(0.5f),
        )
    }
}


enum class CreateSchoolErrorState(val message: String) {
    InvalidUsername("Full name should be at least six characters"),
    InvalidSchoolName("School name should be at least six characters"),
    InvalidEmail("Please check your email address"),
    InvalidCredentials("Please check your email or password"),
    AlreadyExistingEmail("Sorry this email is already in use"),
    InvalidPhoneNumber("Please enter a valid phone number"),
    InvalidPassword("Password should be at least six characters"),
    Network("Please check your network and try again")
}


@Composable
@Preview
private fun FlagWithCodePreview() {
    FlagWithCode(
        code = "2324-5",
        expanded = true,
        onClick = {}
    )
}