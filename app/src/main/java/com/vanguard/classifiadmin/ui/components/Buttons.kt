package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.theme.Black100


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
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp)),
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