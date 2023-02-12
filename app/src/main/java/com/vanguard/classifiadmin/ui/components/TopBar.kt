package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R

@Composable
fun ChildTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    heading: String,
) {
    Card(modifier = modifier, elevation = 2.dp) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedIconButtonTopBar(
                icon = R.drawable.icon_back,
                onClick = onBack,
                size = 20.dp,
            )

            Text(
                text = heading,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Composable
fun RoundedIconButtonTopBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    size: Dp = 24.dp,
    surfaceSize: Dp = 32.dp,
    icon: Int,
) {
    Surface(
        modifier = modifier.clip(CircleShape).size(surfaceSize),
        shape = CircleShape,
        color = MaterialTheme.colors.primary.copy(0.1f)
    ) {
        IconButton(onClick = onClick, modifier = modifier.clip(CircleShape)) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.add),
                modifier = modifier.size(size),
                tint = MaterialTheme.colors.primary,
            )
        }
    }
}


@Preview
@Composable
private fun RoundedIconButtonTopBarPreview() {
    RoundedIconButtonTopBar(
        onClick = {},
        icon = R.drawable.icon_close
    )
}

@Preview
@Composable
private fun ChildTopBarPreview() {
    ChildTopBar(
        onBack = {},
        heading = "Manage Class"
    )
}