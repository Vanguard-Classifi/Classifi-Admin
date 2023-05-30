package com.khalidtouch.chatme.admin.school

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddSchoolScreen() {
    val configuration = LocalConfiguration.current

    AlertDialog(
        onDismissRequest = { /*TODO*/ } ,
        confirmButton = {

        },
        dismissButton = {

        },
        title = {

        },
        text = {

        },
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 16.dp)
    )
}