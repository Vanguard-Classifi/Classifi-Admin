package com.khalidtouch.core.designsystem.components

import android.widget.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClassifiButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    border: BorderStroke? = null,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.onBackground,
        contentColor = MaterialTheme.colorScheme.background,
    ),
) {
    ClassifiButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        contentPadding = if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        } else ButtonDefaults.ContentPadding,
    ) {
        ClassifiButtonContent(text = text, leadingIcon = leadingIcon)
    }
}

@Composable
fun ClassifiButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.onBackground,
        contentColor = MaterialTheme.colorScheme.background,
    ),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        border = border,
        content = content
    )
}

@Composable
fun ClassifiOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    ClassifiOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        } else ButtonDefaults.ContentPadding,
    ) {
        ClassifiButtonContent(text = text, leadingIcon = leadingIcon)
    }
}


@Composable
fun ClassifiOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isDark: Boolean = false,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(
            width = ClassifiButtonDefaults.OutlinedButtonBorderWidth,
            color = if (enabled) {
                if (isDark) {
                    MaterialTheme.colorScheme.outline
                } else MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = ClassifiButtonDefaults.DisabledOutlinedButtonBorderAlpha
                )
            }
        ),
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun ClassifiTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    ClassifiTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        ClassifiButtonContent(
            text = text,
            leadingIcon = leadingIcon
        )
    }
}

@Composable
fun ClassifiTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.textButtonColors(
        contentColor = MaterialTheme.colorScheme.outline,
        containerColor = Color.Transparent,
        disabledContentColor = MaterialTheme.colorScheme.outline.copy(0.5f),
        disabledContainerColor = Color.Transparent
    ),
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        content = content,
    )
}


@Composable
private fun ClassifiButtonContent(
    text: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            leadingIcon()
        }
    }

    if (text != null) {
        Box(
            Modifier
                .padding(
                    start = if (leadingIcon != null) {
                        ButtonDefaults.IconSpacing
                    } else 0.dp,
                )
        ) { text() }
    }
}


object ClassifiButtonDefaults {
    const val DisabledOutlinedButtonBorderAlpha = 0.12f
    val OutlinedButtonBorderWidth = 1.dp
}


@Composable
@Preview
private fun ClassifiTextButtonPreview() {
    ClassifiTextButton(
        onClick = {},
        text = { Text("This is a ball") },
    )
}