package com.khalidtouch.core.designsystem.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassifiDatePickerDialog(
    onDismiss: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    pickerContent: @Composable ColumnScope.() -> Unit,
) {
    Box(Modifier.padding(16.dp)) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(16.dp),
            properties = DialogProperties(
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true,
                decorFitsSystemWindows = false,
            ),
            content = pickerContent,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassifiDatePicker(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    colors: DatePickerColors = DatePickerDefaults.colors(),
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.padding(16.dp)) {
        DatePicker(
            showModeToggle = false,
            state = state,
            modifier = modifier,
            colors = colors,
        )
    }

}