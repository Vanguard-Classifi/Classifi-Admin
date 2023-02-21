package com.vanguard.classifiadmin.ui.screens.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.UserLoginState
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithCloseButtonOnly
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.components.SuccessBar
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val ADD_SCHOOL_SCREEN = "add_school_screen"

@Composable
fun AddSchoolScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSchoolCreated: () -> Unit,
    onJoinSchool: () -> Unit,
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
                AddSchoolScreenContent(
                    modifier = modifier.padding(padding),
                    viewModel = viewModel,
                    onJoinSchool = onJoinSchool,
                    onSchoolCreated = onSchoolCreated,
                )
            }
        )
    }
}

@Composable
fun AddSchoolScreenContent(
    modifier: Modifier = Modifier,
    onSchoolCreated: () -> Unit,
    viewModel: MainViewModel,
    onJoinSchool: () -> Unit,
) {

    val innerModifier = Modifier
    val scope = rememberCoroutineScope()
    var schoolCreatedState by remember { mutableStateOf(false) }
    val constraints = AddSchoolScreenContentConstraints(8.dp)
    var createSchoolErrorState: CreateSchoolErrorState? by remember { mutableStateOf(null) }
    val verticalScroll = rememberScrollState()

    val schoolNameCreateSchool by viewModel.schoolNameCreateSchool.collectAsState()


    LaunchedEffect(createSchoolErrorState) {
        if (createSchoolErrorState != null) {
            delay(3000)
            createSchoolErrorState = null
        }
    }

    LaunchedEffect(schoolCreatedState) {
        if (schoolCreatedState) {
            delay(2000)
            schoolCreatedState = false
            //go to dashboard
            onSchoolCreated()
        }
    }

    LaunchedEffect(Unit) {
        //reset user login state
        viewModel.onUserLoginStateChanged(UserLoginState.Registered)
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


                        PrimaryTextButtonFillWidth(
                            label = stringResource(id = R.string.create_school),
                            onClick = {

                                if (schoolNameCreateSchool == null || schoolNameCreateSchool?.isBlank() == true) {
                                    createSchoolErrorState =
                                        CreateSchoolErrorState.InvalidSchoolName
                                    return@PrimaryTextButtonFillWidth
                                }

                                scope.launch {
                                    //find current user
                                    //create school
                                    //save user
                                    //save school
                                }

                            },
                            modifier = innerModifier.layoutId("createBtn")
                        )

                        TextRowWithClickable(
                            unClickable = stringResource(id = R.string.join_an_existing_school_instead),
                            clickable = stringResource(id = R.string.join),
                            onClick = onJoinSchool,
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
                contentAlignment = Alignment.BottomCenter,
            ) {
                AnimatedVisibility(
                    visible = schoolCreatedState,
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
                        message = stringResource(id = R.string.school_creation_completed),
                        maxWidth = maxWidth,
                    )
                }

            }

        }
    }
}

private fun AddSchoolScreenContentConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val header = createRefFor("header")
        val school = createRefFor("school")
        val createBtn = createRefFor("createBtn")
        val login = createRefFor("login")

        constrain(header) {
            top.linkTo(parent.top, 16.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
        }

        constrain(school) {
            top.linkTo(header.bottom, 64.dp)
            start.linkTo(header.start, 0.dp)
            end.linkTo(header.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(createBtn) {
            top.linkTo(school.bottom, 42.dp)
            start.linkTo(school.start, 0.dp)
            end.linkTo(school.end, 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(login) {
            top.linkTo(createBtn.bottom, 16.dp)
            start.linkTo(createBtn.start, 0.dp)
            end.linkTo(createBtn.end, 0.dp)
        }
    }
}