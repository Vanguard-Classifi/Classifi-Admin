package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R

@Composable
fun MessageBar(
    message: String,
    icon: Int = R.drawable.icon_info,
    onClose: () -> Unit,
) {
    val constraints = messageBarConstraints(8.dp)
    val innerModifier = Modifier

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .widthIn(min = 300.dp)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp, shape = RoundedCornerShape(8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier,
            constraintSet = constraints,
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.message_icon),
                modifier = innerModifier
                    .layoutId("icon")
                    .size(24.dp),
                tint = MaterialTheme.colors.primary
            )

            Text(
                text = message,
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary,
                modifier = innerModifier
                    .widthIn(max = 220.dp)
                    .layoutId("message"),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )


            IconButton(
                onClick = onClose,
                modifier = innerModifier.layoutId("close")
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = stringResource(id = R.string.close),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colors.primary
                )
            }

        }
    }

}

private fun messageBarConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val message = createRefFor("message")
        val close = createRefFor("close")

        constrain(icon) {
            start.linkTo(parent.start, margin)
            top.linkTo(message.top, 0.dp)
            bottom.linkTo(message.bottom, 0.dp)
        }

        constrain(message) {
            top.linkTo(parent.top, margin)
            start.linkTo(icon.end, 8.dp)
        }

        constrain(close) {
            end.linkTo(parent.end, margin)
            top.linkTo(message.top, 0.dp)
            bottom.linkTo(message.bottom, 0.dp)
        }
    }
}

@Preview
@Composable
private fun MessageBarPreview() {
    MessageBar(
       icon = R.drawable.icon_info,
        onClose = {},
        message = "Warning, Close now"
    )
}