package com.khalidtouch.classifiadmin.students.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
internal fun StudentsRoute(
    modifier: Modifier = Modifier,
    studentsViewModel: StudentsViewModel = hiltViewModel<StudentsViewModel>()
) {
    StudentsScreen(modifier = modifier)
}


@Composable
internal fun StudentsScreen(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Students Screen",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}