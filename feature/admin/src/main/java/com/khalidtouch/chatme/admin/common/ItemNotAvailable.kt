package com.khalidtouch.chatme.admin.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.core.designsystem.icons.ClassifiIcons


@Composable
fun ItemNotAvailable(
    headerText: String,
    labelText: String,
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = ClassifiIcons.EmptyTv),
            contentDescription = null,
            modifier = Modifier.size(300.dp),
            tint = MaterialTheme.colorScheme.outline,
        )

        Text(
            text = headerText,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.outline
            ),
        )

        Text(
            text = labelText,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.outline
            ),
        )
    }
}

@Composable
@Preview
private fun ItemNotAvailablePreview() {
    ItemNotAvailable(
      headerText = "School not available",
      labelText = "Click + to add"
    )
}