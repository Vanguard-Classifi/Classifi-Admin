package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R

@Composable
fun MessageBar(
    message: String,
    icon: Int = R.drawable.icon_info,
    onClose: () -> Unit,
    maxWidth: Dp,
) {
    val constraints = messageBarConstraints(8.dp)
    val innerModifier = Modifier

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .widthIn(max = maxWidth.times(0.95f))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        ),
        elevation = 2.dp, shape = RoundedCornerShape(8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth(),
            constraintSet = constraints,
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.message_icon),
                modifier = innerModifier
                    .layoutId("icon")
                    .padding(4.dp)
                    .size(24.dp),
                tint = MaterialTheme.colors.primary
            )

            Text(
                text = message,
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary,
                modifier = innerModifier
                    .padding(horizontal = 2.dp)
                    .widthIn(max = maxWidth.times(0.7f))
                    .layoutId("message"),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )


            IconButton(
                onClick = onClose,
                modifier = innerModifier.layoutId("close")
                    .size(30.dp).padding(4.dp)
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
            start.linkTo(icon.end, 4.dp)
            end.linkTo(close.start, 4.dp)
         // width = Dimension.fillToConstraints
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
        message = "Warning, Close gcfdfcgcgufuhftguguvcgucvgucfcgcfydtfdfgunohfgcgcgcgcgwdgsgfgfgfgdfgrfgfgfdfggufguvguvuhghiw",
        maxWidth = 700.dp
    )
}