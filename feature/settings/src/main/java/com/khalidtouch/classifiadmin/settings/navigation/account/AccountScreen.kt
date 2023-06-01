package com.khalidtouch.classifiadmin.settings.navigation.account

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.khalidtouch.classifiadmin.settings.R
import com.khalidtouch.classifiadmin.settings.navigation.settings.SettingsViewModel
import com.khalidtouch.core.designsystem.components.SettingItem
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

@Composable
fun AccountScreenWrapper(
    settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = settingsViewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = {},
                onDragStopped = {
                    settingsViewModel.updateTabIndexBasedOnSwipe()
                }
            )
    ) {
        Scaffold(
            content = { padding ->
                AccountScreen(
                    modifier = Modifier.padding(padding)
                )
            }
        )
    }
}


@Composable
internal fun AccountScreen(
    modifier: Modifier = Modifier,
){
    val headerStyle = MaterialTheme.typography.titleSmall.copy(
        color = Color.Black.copy(0.8f)
    )
    val textStyle = MaterialTheme.typography.bodyMedium

    LazyColumn(Modifier.fillMaxSize()) {
        schoolItem(
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        classItem(
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        studentItem(
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        subjectItem(
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

    }
}


fun LazyListScope.subjectItem(
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        SettingItem(
            onClick = {
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Subject),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.my_subjects)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "12 Subjects"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}
fun LazyListScope.studentItem(
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        SettingItem(
            onClick = {
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Students),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.my_students)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "25 students"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}

fun LazyListScope.classItem(
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        SettingItem(
            onClick = {
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Classroom),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.my_classes)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "2 classes"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}


fun LazyListScope.schoolItem(
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        Spacer(modifier = Modifier.height(32.dp))

        SettingItem(
            onClick = {
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.School),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.my_schools)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "2 schools"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}