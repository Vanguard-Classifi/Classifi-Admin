package com.khalidtouch.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

@Composable
fun ClassifiMenu(
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = ClassifiMenuDefaults.cardElevation,
            pressedElevation = ClassifiMenuDefaults.cardElevation,
            focusedElevation = ClassifiMenuDefaults.cardElevation,
        )
    ) {
        Column(
            modifier = modifier,
        ) {
            if (header != null) {
                Box {
                    header()
                }
                Divider(modifier = modifier.padding(horizontal = 16.dp))
            }
            content()
        }
    }
}


@Composable
fun MenuHeader(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.clickable(
            enabled = true,
            onClick = onClick,
            role = Role.Button,
        ),
        color = Color.Transparent,
    ) {
        content()
    }
}


@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.clickable(
            enabled = true,
            onClick = onClick,
            role = Role.Button,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = modifier.padding(ClassifiMenuDefaults.iconPadding)) {
            icon()
        }

        Box(modifier = modifier.weight(1f)) {
            text()
        }
    }
}


object ClassifiMenuDefaults {
    val iconPadding = 16.dp
    val cardElevation = 2.dp
}

@Preview
@Composable
private fun MenuItemPreview() {
    MenuItem(
        icon = {
            Icon(
                painter = painterResource(id = ClassifiIcons.Settings),
                contentDescription = null
            )
        },
        text = {
            Text(
                text = "My Account"
            )
        }
    )
}