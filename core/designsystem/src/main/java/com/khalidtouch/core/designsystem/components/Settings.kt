package com.khalidtouch.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    hasEditIcon: Boolean = true,
) {
    Box {
        Row(
            modifier = modifier.clickable(
                enabled = true,
                onClick = onClick,
                role = Role.Button,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = modifier.padding(ClassifiSettingDefaults.iconPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                icon()
            }

            Box(
                modifier = modifier
                    .weight(1f)
                    .padding(
                        top = ClassifiSettingDefaults.topTextPadding,
                        bottom = ClassifiSettingDefaults.topTextPadding
                    )
            ) {
                text()
            }
        }

        if (hasEditIcon) {
            Box(
                contentAlignment = Alignment.TopEnd, modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, top = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = ClassifiIcons.EditSolid),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

object ClassifiSettingDefaults {
    val iconPadding = 22.dp
    val topTextPadding = 8.dp
}