package com.vanguard.classifiadmin.ui.screens.students

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.ui.components.ClassFilterManageButton
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatar
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay

const val STUDENTS_SCREEN = "students_screen"

@Composable
fun StudentsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onStudentOptions: (UserModel) -> Unit,
    onStudentDetail: (UserModel) -> Unit,
) {
    StudentsScreenContent(
        modifier = modifier,
        onStudentOptions = onStudentOptions,
        viewModel = viewModel,
        onStudentDetail = onStudentDetail,
    )
}

@Composable
fun StudentsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onStudentOptions: (UserModel) -> Unit,
    onStudentDetail: (UserModel) -> Unit,
) {
    val verticalScroll = rememberLazyListState()
    val innerModifier = Modifier
    val verifiedStudentsUnderClassNetwork by viewModel.verifiedStudentsUnderClassNetwork.collectAsState()
    val currentClassFeedPref by viewModel.currentClassFeedPref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentClassFeedPref()
        delay(1000)
        viewModel.getVerifiedStudentsUnderClassNetwork(
            currentClassFeedPref.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier) {
            val maxHeight = maxHeight
            val maxWidth = maxWidth
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = innerModifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(id = R.string.members),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                    )
                }

                Spacer(modifier = innerModifier.height(16.dp))

                when (verifiedStudentsUnderClassNetwork) {
                    is Resource.Loading -> {
                        LoadingScreen(
                            maxHeight = maxHeight,
                            modifier = innerModifier.weight(1f)
                        )
                    }

                    is Resource.Success -> {
                        if (verifiedStudentsUnderClassNetwork.data?.isNotEmpty() == true) {
                            LazyColumn(
                                modifier = innerModifier
                                        .weight(1f)
                                    .padding(bottom = 72.dp),
                                state = verticalScroll,
                            ) {
                                items(verifiedStudentsUnderClassNetwork.data!!) { each ->
                                    StudentItem(
                                        student = each.toLocal(),
                                        onOptions = onStudentOptions,
                                        onSelectStudent = onStudentDetail
                                    )
                                }
                            }
                        } else {
                            NoDataScreen(
                                maxHeight = maxHeight,
                                message = stringResource(id = R.string.no_students),
                                buttonLabel = "",
                                showButton = false,
                                onClick = {},
                                modifier = innerModifier.weight(1f)
                            )
                        }
                    }

                    is Resource.Error -> {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.something_went_wrong),
                            buttonLabel = stringResource(id = R.string.retry),
                            onClick = {
                                viewModel.getVerifiedStudentsUnderClassNetwork(
                                    currentClassFeedPref.orEmpty(),
                                    currentSchoolIdPref.orEmpty()
                                )
                            },
                            modifier = innerModifier.weight(1f)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun StudentItem(
    modifier: Modifier = Modifier,
    student: UserModel,
    onOptions: (UserModel) -> Unit,
    onSelectStudent: (UserModel) -> Unit,
) {
    val constraints = studentItemConstraints(8.dp)
    val innerModifier = Modifier

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable { onSelectStudent(student) },
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.1f)
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            DefaultAvatar(
                modifier = innerModifier.layoutId("icon"),
                label = student.fullname.orEmpty(),
                onClick = {},
                enabled = false,
            )

            Text(
                modifier = innerModifier.layoutId("studentName"),
                text = student.fullname.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(generateColorFromClassName(student.fullname.orEmpty()))
            )

            Text(
                modifier = innerModifier.layoutId("code"),
                text = student.email.orEmpty(),
                fontSize = 12.sp,
                color = Black100.copy(0.8f)
            )

            RoundedIconButton(
                modifier = innerModifier.layoutId("options"),
                onClick = { onOptions(student) },
                icon = R.drawable.icon_options_horizontal,
            )
        }
    }
}

private fun studentItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val studentName = createRefFor("studentName")
        val code = createRefFor("code")
        val options = createRefFor("options")

        constrain(icon) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(studentName.top, margin = 0.dp)
        }

        constrain(studentName) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(icon.end, margin = 8.dp)
        }

        constrain(code) {
            top.linkTo(studentName.bottom, margin = 4.dp)
            start.linkTo(studentName.start, margin = 0.dp)
        }

        constrain(options) {
            top.linkTo(studentName.top, margin = 0.dp)
            bottom.linkTo(code.bottom, margin = 0.dp)
            end.linkTo(parent.end, margin = margin)
        }
    }
}

data class Student(
    val name: String,
    val code: String,
)