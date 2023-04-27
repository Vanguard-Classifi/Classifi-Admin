package com.khalidtouch.core.designsystem.components

import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun ClassifiToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon
) {
    FilledIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconToggleButtonColors(
            checkedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            checkedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = if(checked) {
                MaterialTheme.colorScheme.onBackground.copy(
                    alpha = ClassifiIconButtonDefaults.DisabledIconButtonContainerAlpha
                )
            } else {
                Color.Transparent
            }
        )
    ) {
        if(checked) checkedIcon() else icon()
    }
}

object ClassifiIconButtonDefaults {
    const val DisabledIconButtonContainerAlpha = 0.12f
}