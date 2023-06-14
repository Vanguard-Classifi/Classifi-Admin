package com.khalidtouch.classifiadmin.settings.navigation.administration

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
import com.khalidtouch.classifiadmin.settings.navigation.preferences.PreferencesScreen
import com.khalidtouch.classifiadmin.settings.navigation.settings.SettingsViewModel
import com.khalidtouch.core.designsystem.components.SettingItem
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdministrationScreenWrapper(
    settingsViewModel: SettingsViewModel,
    onOpenSchoolAdminPanel: () -> Unit,
    onOpenTeacherAdminPanel: () -> Unit,
    onOpenParentAdminPanel: () -> Unit,
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
                AdministrationScreen(
                    modifier = Modifier.padding(padding),
                    onOpenSchoolAdminPanel = onOpenSchoolAdminPanel,
                    onOpenTeacherAdminPanel = onOpenTeacherAdminPanel,
                    onOpenParentAdminPanel = onOpenParentAdminPanel,
                )
            }
        )
    }
}


@Composable
private fun AdministrationScreen(
    modifier: Modifier = Modifier,
    onOpenSchoolAdminPanel: () -> Unit,
    onOpenTeacherAdminPanel: () -> Unit,
    onOpenParentAdminPanel: () -> Unit,
){
    val headerStyle = MaterialTheme.typography.titleMedium.copy(
        color = Color.Black.copy(0.8f)
    )
    val textStyle = MaterialTheme.typography.bodyLarge

    LazyColumn(Modifier.fillMaxSize()) {
        manageSchool(
            headerStyle = headerStyle,
            textStyle = textStyle,
            onOpenSchoolAdminPanel = onOpenSchoolAdminPanel,
        )

        manageAdmins(
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        manageTeachers(
            headerStyle = headerStyle,
            textStyle = textStyle,
            onOpenTeacherAdminPanel = onOpenTeacherAdminPanel,
        )

        manageParents(
            headerStyle = headerStyle,
            textStyle = textStyle,
            onOpenParentAdminPanel = onOpenParentAdminPanel
        )

        manageStudents(
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        manageClasses(
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        manageSubjects(
            headerStyle = headerStyle,
            textStyle = textStyle,
        )
    }
}

fun LazyListScope.manageSchool(
    headerStyle: TextStyle,
    textStyle: TextStyle,
    onOpenSchoolAdminPanel: () -> Unit,
) {
    item {
        Spacer(modifier = Modifier.height(8.dp))

        SettingItem(
            onClick = onOpenSchoolAdminPanel,
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
                            text = stringResource(id = R.string.manage_school)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Click to manage school"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}

fun LazyListScope.manageTeachers(
    headerStyle: TextStyle,
    textStyle: TextStyle,
    onOpenTeacherAdminPanel: () -> Unit,
) {
    item {
        SettingItem(
            onClick = onOpenTeacherAdminPanel,
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Profile),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.manage_teachers)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Click to manage teachers"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}

fun LazyListScope.manageAdmins(
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        SettingItem(
            onClick = {
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Admin),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.manage_admins)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Click to manage admins"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}

fun LazyListScope.manageParents(
    headerStyle: TextStyle,
    textStyle: TextStyle,
    onOpenParentAdminPanel: () -> Unit,
) {
    item {
        SettingItem(
            onClick = onOpenParentAdminPanel,
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Parent),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.manage_parents)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Click to manage parents"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}

fun LazyListScope.manageStudents(
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
                            text = stringResource(id = R.string.manage_students)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Click to manage students"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}

fun LazyListScope.manageClasses(
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
                            text = stringResource(id = R.string.manage_classes)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Click to manage classes"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}


fun LazyListScope.manageSubjects(
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
                            text = stringResource(id = R.string.manage_subjects)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Click to manage subjects"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}