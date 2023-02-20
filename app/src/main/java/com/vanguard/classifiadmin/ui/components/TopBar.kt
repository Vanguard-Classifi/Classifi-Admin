package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R


@Composable
fun PagerBarWithIcon(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    icon: Int,
    text: String,
    backgroundColor: Color = MaterialTheme.colors.primary,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Surface(
        modifier = Modifier.padding(horizontal = 12.dp).clip(CircleShape),
        color = backgroundColor,
        shape = CircleShape,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = if (selected) Arrangement.SpaceBetween else Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                tint = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(
                    0.5f
                ),
                modifier = modifier
                    .size(24.dp)
                    .padding(2.dp)
            )

            if (selected) {
                Text(
                    modifier = modifier.padding(8.dp),
                    text = text,
                    color = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(
                        0.5f
                    ),
                    maxLines = 1,
                    fontSize = 12.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    onTextLayout = onTextLayout,
                )
            }
        }
    }

}

@Composable
fun ChildTopBarWithCloseButtonOnly(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedIconButton(
            onClick = onClose,
            modifier = modifier.padding(8.dp),
            icon = R.drawable.icon_close,
        )
    }
}

@Composable
fun ChildTopBarWithInfo(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onInfo: () -> Unit,
    heading: String,
    subheading: String,
    elevation: Dp = 2.dp,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.primary,
) {
    Card(modifier = modifier, elevation = elevation, backgroundColor = backgroundColor) {
        val constraints = childTopBarConstraints(8.dp)
        val innerModifier = Modifier

        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            constraintSet = constraints
        ) {
            RoundedIconButtonTopBar(
                icon = R.drawable.icon_back,
                onClick = onBack,
                size = 20.dp,
                tint = contentColor,
                modifier = innerModifier.layoutId("icon"),
            )

            Text(
                text = heading,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = innerModifier.layoutId("title"),
            )

            Text(
                text = subheading,
                color = contentColor.copy(0.5f),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                modifier = innerModifier.layoutId("subtitle"),
            )

            RoundedIconButtonTopBar(
                icon = R.drawable.icon_info,
                onClick = onInfo,
                size = 20.dp,
                tint = contentColor,
                modifier = innerModifier.layoutId("icon2"),
            )
        }
    }
}

private fun childTopBarConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val title = createRefFor("title")
        val subtitle = createRefFor("subtitle")
        val icon2 = createRefFor("icon2")

        constrain(icon) {
            top.linkTo(title.top, margin = 0.dp)
            bottom.linkTo(subtitle.bottom, 0.dp)
            start.linkTo(parent.start, margin = margin)
        }

        constrain(title) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(icon.end, 4.dp)
        }

        constrain(subtitle) {
            top.linkTo(title.bottom, 2.dp)
            start.linkTo(title.start, 4.dp)
        }

        constrain(icon2) {
            top.linkTo(title.top, margin = 0.dp)
            bottom.linkTo(subtitle.bottom, 0.dp)
            end.linkTo(parent.end, margin = margin)
        }
    }
}


@Composable
fun ChildTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    heading: String,
    elevation: Dp = 2.dp,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.primary,
) {
    Card(modifier = modifier, elevation = elevation, backgroundColor = backgroundColor) {
        Box(
            modifier = modifier
                .padding(12.dp)
                .fillMaxWidth(), contentAlignment = Alignment.CenterStart
        ) {
            RoundedIconButtonTopBar(
                icon = R.drawable.icon_back,
                onClick = onBack,
                size = 20.dp,
                tint = contentColor,
            )

            Box(
                modifier = modifier
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = heading,
                    color = contentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = modifier,
                    textAlign = TextAlign.Center,
                )
            }
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
    tint: Color = MaterialTheme.colors.primary
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .size(surfaceSize),
        shape = CircleShape,
        color = tint.copy(0.1f)
    ) {
        IconButton(onClick = onClick, modifier = modifier.clip(CircleShape)) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.add),
                modifier = modifier.size(size),
                tint = tint,
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

@Preview
@Composable
private fun ChildTopBarWithInfoPreview() {
    ChildTopBarWithInfo(
        onBack = {},
        onInfo = {},
        heading = "The Boy is neat",
        subheading = "There is a way out",
    )
}

@Composable
@Preview
private fun ChildTopBarWithCloseButtonOnlyPreview() {
    ChildTopBarWithCloseButtonOnly(
        onClose = {}
    )
}

@Composable
@Preview
private fun PagerBarWithIconPreview() {
    PagerBarWithIcon(
        icon = R.drawable.icon_settings,
        text = "Profile",
        selected = true,
    )
}