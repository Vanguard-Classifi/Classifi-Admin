package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.vanguard.classifiadmin.ui.theme.Black100


@Composable
fun NoDataScreen(
    modifier: Modifier = Modifier,
    maxHeight: Dp,
    message: String,
    buttonLabel: String,
    showButton: Boolean = true,
    onClick: () -> Unit,
) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight.div(2f)),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = modifier.height(16.dp))

                Text(
                    text = message,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100.copy(.8f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = modifier.height(16.dp))

                if (showButton) {
                    PrimaryTextButton(label = buttonLabel, onClick = onClick)
                    Spacer(modifier = modifier.height(16.dp))
                }

            }

        }
    }
}

@Composable
fun SuccessBar(
    modifier: Modifier = Modifier,
    message: String,
    maxWidth: Dp,
) {
    val constraints = successBarConstraints(8.dp)
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
                painter = painterResource(id = R.drawable.icon_tick),
                contentDescription = stringResource(id = R.string.completed_icon),
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
        }
    }
}

private fun successBarConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val message = createRefFor("message")

        constrain(icon) {
            start.linkTo(parent.start, margin)
            top.linkTo(message.top, 0.dp)
            bottom.linkTo(message.bottom, 0.dp)
        }

        constrain(message) {
            top.linkTo(parent.top, margin)
            start.linkTo(icon.end, 4.dp)
            end.linkTo(parent.end, 4.dp)
            // width = Dimension.fillToConstraints
        }

    }
}


@Composable
fun MessageBar(
    modifier: Modifier = Modifier,
    message: String,
    icon: Int = R.drawable.icon_info,
    onClose: () -> Unit,
    maxWidth: Dp,
) {
    val constraints = messageBarConstraints(8.dp)
    val innerModifier = Modifier

    Card(
        modifier = modifier
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
            modifier = modifier.fillMaxWidth(),
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
                modifier = innerModifier
                    .layoutId("close")
                    .size(30.dp)
                    .padding(4.dp)
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

@Preview
@Composable
private fun SuccessBarPreview() {
    SuccessBar(
        message = "Account Creation Completed!",
        maxWidth = 400.dp,
    )
}