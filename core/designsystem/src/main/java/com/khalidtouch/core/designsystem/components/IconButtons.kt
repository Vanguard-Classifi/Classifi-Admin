package com.khalidtouch.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ClassifiStagingIconButton(
    modifier: Modifier = Modifier,
    buttonSize: Dp = 45.dp,
    enabled: Boolean = true,
    onClick: () -> Unit,
    elevation: Dp = 2.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    border: BorderStroke? = null,
    shadowElevation: Dp = ClassifiIconButtonDefaults.shadowElevationDefaults,
    icon: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .size(buttonSize)
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                role = Role.Button,
            ),
        shape = CircleShape,
        tonalElevation = elevation,
        border = border,
        color = color,
        shadowElevation = shadowElevation,
    ) {
        Box(contentAlignment = Alignment.Center) {
            icon()
        }
    }
}


@Composable
fun ClassifiTakeSnapshotButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    border: BorderStroke? = null,
    buttonSize: Dp = ClassifiIconButtonDefaults.defaultButtonSize,
    color: Color = MaterialTheme.colorScheme.primary,
    elevation: Dp = ClassifiIconButtonDefaults.tonalElevationDefaults,
    shadowElevation: Dp = ClassifiIconButtonDefaults.shadowElevationDefaults,
) {
    Surface(
        modifier = modifier
            .size(buttonSize)
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                role = Role.Button,
            ),
        shape = CircleShape,
        tonalElevation = elevation,
        border = border,
        color = color,
        shadowElevation = shadowElevation,
    ) {
        Box(contentAlignment = Alignment.Center) {
            icon()
        }

    }
}

@Composable
fun ClassifiFab(
    onClick: () -> Unit,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    icon: @Composable () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(16.dp),
    ) {
        icon()
    }
}


@Composable
fun ClassifiIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    buttonSize: Dp = ClassifiIconButtonDefaults.defaultButtonSize,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ),
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(buttonSize)
            .clip(CircleShape),
        colors = colors,
        enabled = enabled,
    ) {
        icon()
    }
}


@Composable
fun ClassifiToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = ClassifiIconButtonDefaults.smallButtonSize,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon,
    colors: IconToggleButtonColors = IconButtonDefaults.filledIconToggleButtonColors(
        checkedContainerColor = MaterialTheme.colorScheme.primary,
        checkedContentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        disabledContainerColor = if (checked) {
            MaterialTheme.colorScheme.onBackground.copy(
                alpha = ClassifiIconButtonDefaults.DisabledIconButtonContainerAlpha
            )
        } else {
            Color.Transparent
        }
    ),
) {
    FilledIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.size(buttonSize),
        enabled = enabled,
        colors = colors,
    ) {
        if (checked) checkedIcon() else icon()
    }
}

object ClassifiIconButtonDefaults {
    const val DisabledIconButtonContainerAlpha = 0.12f
    val smallButtonSize = 42.dp
    val defaultButtonSize = 48.dp
    val bigButtonSize = 96.dp
    val tonalElevationDefaults = 2.dp
    val shadowElevationDefaults = 0.dp
}