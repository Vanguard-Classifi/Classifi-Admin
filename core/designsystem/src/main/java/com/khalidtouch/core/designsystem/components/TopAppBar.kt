package com.khalidtouch.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassifiCenterTopAppBar(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    elevate: Boolean = true,
    colors:TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    Card(
        modifier = modifier.wrapContentHeight().fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if(elevate) {
                ClassifiTopAppBarDefaults.CardElevation
            } else {
                0.dp
            },
        ),
        content = {
            CenterAlignedTopAppBar(
                modifier = modifier,
                title = { text() },
                navigationIcon = { if(leadingIcon != null) leadingIcon() },
                actions = {
                    if(trailingIcon != null) trailingIcon()
                },
                colors = colors
            )
        }
    )
}


object ClassifiTopAppBarDefaults {
    val CardElevation = 2.dp
}