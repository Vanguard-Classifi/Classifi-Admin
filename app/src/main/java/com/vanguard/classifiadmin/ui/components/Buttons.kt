package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun DropdownButton(
    modifier: Modifier = Modifier,
    prefix: String,
    text: String,
    icon: Int,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colors.primary,
    textColor: Color = MaterialTheme.colors.primary,
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = textColor.copy(0.1f),
            contentColor = textColor,
            disabledBackgroundColor = Black100.copy(0.1f)
        )
    ) {
        Text(
            text = prefix,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = textColor,
            modifier = modifier.padding(horizontal = 4.dp, vertical = 0.dp),
            letterSpacing = (-0.5).sp,
        )

        Text(
            text = text.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = modifier.padding(horizontal = 4.dp, vertical = 0.dp)
        )

        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = R.string.icon),
            tint = iconTint,
            modifier = modifier
                .padding(horizontal = 4.dp, vertical = 0.dp)
                .size(25.dp)
        )
    }
}

@Composable
fun TertiaryTextButtonWithIcon(
    modifier: Modifier = Modifier,
    label: String,
    icon: Int,
    onClick: () -> Unit,
    iconSize: Dp = 24.dp,
    iconTint: Color = MaterialTheme.colors.primary,
    textColor: Color = MaterialTheme.colors.primary,
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = textColor,
            disabledBackgroundColor = Black100.copy(0.1f)
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = R.string.icon),
            tint = iconTint,
            modifier = modifier
                .padding(horizontal = 4.dp, vertical = 0.dp)
                .size(iconSize)
        )

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            color = textColor,
            modifier = modifier.padding(horizontal = 4.dp, vertical = 0.dp)
        )
    }
}


@Composable
fun PrimaryTextButtonWithIcon(
    modifier: Modifier = Modifier,
    label: String,
    icon: Int,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colors.primary,
    textColor: Color = MaterialTheme.colors.primary,
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Black100.copy(0.1f),
            contentColor = textColor,
            disabledBackgroundColor = Black100.copy(0.1f)
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = R.string.icon),
            tint = iconTint,
            modifier = modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .size(12.dp)
        )

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            color = textColor,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}


@Composable
fun LocalIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    icon: Int,
    iconTint: Color = MaterialTheme.colors.primary,
    iconSize: Dp = 24.dp,
    size: Dp = 38.dp,
    contentDescription: String,
    onToggleExpand: () -> Unit,
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onToggleExpand() },
                    onDoubleTap = { onToggleExpand() }
                )
            },
        shape = RoundedCornerShape(12.dp),
        color = Black100.copy(0.3f)
    ) {
        Box(
            modifier = modifier
                .padding(0.dp)
                .height(size)
                .width(size), contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onClick, enabled = enabled, modifier = modifier.padding(0.dp)) {
                Icon(
                    modifier = modifier
                        .size(iconSize)
                        .padding(0.dp),
                    painter = painterResource(id = icon),
                    tint = iconTint,
                    contentDescription = contentDescription,
                )
            }
        }
    }
}


@Composable
fun SecondaryTextButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .clip(CircleShape),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.primary,
            disabledBackgroundColor = Color.Transparent,
        )
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}


@Composable
fun PrimaryTextButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary.copy(0.3f),
            contentColor = MaterialTheme.colors.primary,
            disabledBackgroundColor = Black100.copy(0.1f)
        )
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}


@Composable
fun PrimaryTextButtonFillWidth(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary.copy(0.3f),
            contentColor = MaterialTheme.colors.primary,
            disabledBackgroundColor = Black100.copy(0.1f)
        )
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
@Preview
private fun PrimaryTextButtonPreview() {
    PrimaryTextButtonFillWidth(
        label = "DONE",
        onClick = {}
    )
}


@Preview
@Composable
private fun PrimaryTextButtonWithIconPreview() {
    PrimaryTextButtonWithIcon(
        label = "Live Class",
        onClick = {},
        icon = R.drawable.icon_video_camera,
    )
}