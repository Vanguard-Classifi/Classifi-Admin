package com.vanguard.classifiadmin.ui.screens.assessments.questions.boxes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.theme.Black100


@Composable
fun QuestionBox(
    modifier: Modifier = Modifier,
    isOption: Boolean = true,
    selected: Boolean = false,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onMark: () -> Unit
) {
    val headerOffset = remember { mutableStateOf(0) }
    val headerWidth = remember { mutableStateOf(0) }
    //question body
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = with(LocalDensity.current) {
                    headerOffset.value.toDp()
                })
                .heightIn(
                    min =
                    if (isOption) 100.dp else 180.dp
                )
                .clip(
                    RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp,
                    )
                ),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100.copy(0.5f),
                )
            },
            shape = RoundedCornerShape(
                bottomStart = 16.dp,
                bottomEnd = 16.dp,
            ),
            textStyle = TextStyle(
                color = Black100,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            isError = false,
        )

        //top
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .onGloballyPositioned {
                        headerOffset.value = it.size.height
                        headerWidth.value = it.size.width
                    },
                shape = RoundedCornerShape(
                    topStart = animateDpAsState(
                        targetValue = when {
                            isOption && selected -> 16.dp
                            !isOption -> 16.dp
                            else -> 0.dp
                        }
                    ).value,
                    topEnd = animateDpAsState(
                        targetValue = when {
                            isOption && selected -> 16.dp
                            !isOption -> 16.dp
                            else -> 0.dp
                        }
                    ).value,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary.copy(0.5f),
                ),
                color =
                animateColorAsState(
                    targetValue =
                    if (selected) MaterialTheme.colors.primary else Color.Transparent
                ).value
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = animateColorAsState(
                                targetValue =
                                if (selected) MaterialTheme.colors.onPrimary else Black100
                            ).value,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    if (isOption) {
                        TextButton(
                            onClick = onMark,
                            modifier = modifier
                                .padding(0.dp)
                                .width(
                                    with(LocalDensity.current) {
                                        headerWidth.value
                                            .toDp()
                                            .times(0.45f)
                                    }
                                ),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (selected) MaterialTheme.colors.primary else Color.Transparent,
                            )
                        ) {
                            Spacer(modifier = modifier.weight(1f))
                            Text(
                                text = if (selected) stringResource(id = R.string.correct_answer) else
                                    stringResource(id = R.string.mark_as_answer),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                                    0.5f
                                ),
                            )
                            Spacer(modifier = modifier.width(8.dp))
                            if (selected) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_mark),
                                    contentDescription = stringResource(id = R.string.mark_as_answer),
                                    tint = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                                        0.5f
                                    )
                                )
                                Spacer(modifier = modifier.width(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
