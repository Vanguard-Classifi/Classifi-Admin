package com.vanguard.classifiadmin.ui.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.extensions.splitWithSpace
import com.vanguard.classifiadmin.domain.helpers.AvatarColorMap
import com.vanguard.classifiadmin.domain.helpers.generateColorFromUserName
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.ui.theme.White100

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    filterActivated: Boolean = false,
    filterEnabled: Boolean = true,
    filterLabel: String,
    onFilter: (String) -> Unit,
    sheetEnabled: Boolean = true,
    openSheet: () -> Unit,
    openProfile: () -> Unit,
    onLogin: () -> Unit = {},
    username: String,
    profileEnabled: Boolean = true,
) {
    Card(modifier = modifier, elevation = 2.dp) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_branding_big),
                contentDescription = stringResource(id = R.string.icon_brand),
                modifier = modifier
                    .size(28.dp)
                    .clickable { onLogin() },
                tint = MaterialTheme.colors.primary,
            )

            SelectionButton(
                label = filterLabel,
                onClick = onFilter,
                enabled = filterEnabled,
                activated = filterActivated
            )

            Spacer(modifier = modifier.weight(1f))
            SheetsButton(onClick = openSheet, enabled = sheetEnabled)
            DefaultAvatar(label = username, onClick = openProfile, enabled = profileEnabled)
        }
    }
}

@Composable
fun SelectionButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: (String) -> Unit,
    enabled: Boolean = true,
    activated: Boolean = false,
) {
    TextButton(
        onClick = { onClick(label) }, enabled = enabled, shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.primary,
            disabledBackgroundColor = White100.copy(0.5f)
        ),
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(4.dp),
            color = MaterialTheme.colors.primary,
        )

        Icon(
            painter = painterResource(id = if (activated) R.drawable.icon_dropdown_close else R.drawable.icon_dropdown),
            contentDescription = stringResource(id = R.string.dropdown),
            tint = MaterialTheme.colors.primary,
            modifier = modifier
                .padding(2.dp)
                .size(24.dp),
        )
    }
}


@Composable
fun SheetsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colors.primary.copy(0.1f)
    ) {
        Box(
            modifier = modifier
                .padding(0.dp)
                .height(38.dp)
                .width(38.dp), contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onClick, enabled = enabled, modifier = modifier.padding(0.dp)) {
                Icon(
                    modifier = modifier
                        .size(24.dp)
                        .padding(0.dp),
                    painter = painterResource(id = R.drawable.icon_results),
                    tint = MaterialTheme.colors.primary,
                    contentDescription = stringResource(id = R.string.open_sheet)
                )
            }
        }
    }
}

@Composable
fun DefaultAvatarBig(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    val labelSplit = label.lowercase().splitWithSpace()
    val first = labelSplit.first()[0].toString()
    val second = if (labelSplit.size > 1) labelSplit[1][0].toString() else ""
    val avatar = if (labelSplit.size > 1) "$first$second" else first.toString()
    val colorHex = generateColorFromUserName(label)

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color(colorHex)
    ) {
        Box(
            modifier = modifier
                .padding(0.dp)
                .height(42.dp)
                .width(42.dp), contentAlignment = Alignment.Center
        ) {
            TextButton(onClick = onClick, enabled = enabled, modifier = modifier.padding(0.dp)) {
                Text(
                    text = avatar.uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        }
    }
}

@Composable
fun DefaultAvatar(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    val labelSplit = label.lowercase().splitWithSpace()
    val first = if (labelSplit.size > 1) labelSplit.first()[0].toString() else ""
    val second = if (labelSplit.size > 1) labelSplit[1][0].toString() else ""
    val avatar = if (labelSplit.size > 1) "$first$second" else first.toString()
    val colorHex = generateColorFromUserName(label)

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color(colorHex)
    ) {
        Box(
            modifier = modifier
                .padding(0.dp)
                .height(38.dp)
                .width(38.dp), contentAlignment = Alignment.Center
        ) {
            TextButton(onClick = onClick, enabled = enabled, modifier = modifier.padding(0.dp)) {
                Text(
                    text = avatar.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        }
    }
}


@Preview
@Composable
private fun DefaultAvatarPreview() {
    DefaultAvatar(
        label = "Luhammad",
        onClick = {}
    )
}


@Preview
@Composable
private fun SheetsButtonPreview() {
    SheetsButton(
        onClick = {}
    )
}


@Preview
@Composable
private fun SelectionButtonPreview() {
    SelectionButton(
        label = "Year 11 Benin",
        onClick = {}
    )
}