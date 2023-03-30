package com.vanguard.classifiadmin.ui.screens.assessments.questions.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.theme.Black100


@Composable
fun QuestionOptionItem(
    modifier: Modifier = Modifier,
    content: String,
    option: String = "A",
    selected: Boolean = true,
) {
    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = modifier,
            color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
                0.1f
            ),
            shape = CircleShape,
        ) {
            Box(
                modifier = modifier
                    .size(22.dp)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape,
                    ), contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_tick),
                        contentDescription = stringResource(id = R.string.icon_tick),
                        tint = MaterialTheme.colors.onPrimary,
                        modifier = modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = option.uppercase(),
                        fontSize = 12.sp,
                        color = Black100,
                    )
                }
            }
        }

        Text(
            text = content,
            fontSize = 12.sp,
            color = Black100,
            modifier = modifier.padding(8.dp)
        )
    }
}
