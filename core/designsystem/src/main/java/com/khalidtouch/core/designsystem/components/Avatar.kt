package com.khalidtouch.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp


@Composable
fun ClassifiAvatar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLarge: Boolean = false,
    text: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = if (isLarge) MaterialTheme.shapes.medium else MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = modifier
                .size(
                    if (isLarge) {
                        ClassifiAvatarDefaults.largeSize
                    } else {
                        ClassifiAvatarDefaults.smallSize
                    }
                )
                .clip(if (isLarge) MaterialTheme.shapes.medium else MaterialTheme.shapes.small)
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    role = Role.Button,
                )
        ) {
            text()
        }
    }
}


object ClassifiAvatarDefaults {
    val smallSize = 38.dp
    val largeSize = 78.dp
}