package com.khalidtouch.chatme.admin.school.addschool

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.chatme.admin.R

const val inputSchoolInfoNavigationRoute = "input_school_info_navigation_route"

@Composable
fun InputSchoolInfoScreen(
    addSchoolViewModel: AddSchoolViewModel,
) {
    val state by addSchoolViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LazyColumn {
        schoolName(
            value = state.schoolName,
            onValueChange = addSchoolViewModel::onSchoolNameChanged,
            placeholder = context.getString(R.string.enter_school_name),
        )

        schoolAddress(
            value = state.schoolAddress,
            onValueChange = addSchoolViewModel::onSchoolAddressChanged,
            placeholder = context.getString(R.string.school_address)
        )
    }
}

private fun LazyListScope.schoolName(
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
                keyboardType = KeyboardType.Text,
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
        )
        Spacer(Modifier.height(16.dp))
    }
}

private fun LazyListScope.schoolAddress(
    placeholder: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
) {
    item {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp),
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
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            singleLine = false,
            maxLines = 5,
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
        )
        Spacer(Modifier.height(16.dp))
    }
}


fun NavController.navigateToInputSchoolInfo(
    addSchoolViewModel: AddSchoolViewModel,
) {
    this.navigate(inputSchoolInfoNavigationRoute) {
        launchSingleTop = true
        popUpTo(inputSchoolInfoNavigationRoute)
    }
    addSchoolViewModel.onNavigate(AddSchoolDestination.INPUT)
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.inputSchoolInfo(
    addSchoolViewModel: AddSchoolViewModel,
) {
    composable(
        route = inputSchoolInfoNavigationRoute,
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
        InputSchoolInfoScreen(
            addSchoolViewModel = addSchoolViewModel,
        )
    }
}