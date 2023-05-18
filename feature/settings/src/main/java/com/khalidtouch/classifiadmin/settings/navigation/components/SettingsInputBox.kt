package com.khalidtouch.classifiadmin.settings.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsTextFieldBox(
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null,
    textField: @Composable () -> Unit,
    responseButtons: @Composable RowScope.() -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(SettingsInputDefaults.layoutPadding)
    ) {
        if (header != null) {
            Box(Modifier.padding(bottom = 8.dp)) {
                header()
            }
        }

        Box { textField() }

        Row(horizontalArrangement = Arrangement.End) {
            responseButtons()
        }
    }
}


object SettingsInputDefaults {
    val layoutPadding = 16.dp
}