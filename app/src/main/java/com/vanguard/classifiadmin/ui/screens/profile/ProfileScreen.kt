package com.vanguard.classifiadmin.ui.screens.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.components.SecondaryTextButton
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatarBig
import com.vanguard.classifiadmin.ui.theme.Black100


@Composable
fun MyAccountScreenProfile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val verticalScroll = rememberScrollState()
    val constraints = MyAccountScreenProfileConstraints(16.dp)
    val innerModifier = Modifier


    Column(modifier = Modifier.verticalScroll(verticalScroll)) {

        Card(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 8.dp, bottom = 32.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 2.dp,
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 28.dp),
                constraintSet = constraints,
            ) {
                Text(
                    text = stringResource(id = R.string.profile).uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = innerModifier.layoutId("header")
                )

                DefaultAvatarBig(
                    label = "Hamza Jesim",
                    onClick = { /*TODO*/ },
                    size = 78.dp,
                    fontSize = 18.sp,
                    modifier = innerModifier.layoutId("avatar")
                )

                Text(
                    text = stringResource(id = R.string.upload_photo),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = innerModifier.layoutId("subtitle")
                )

                Text(
                    text = stringResource(id = R.string.accepts_examples),
                    fontSize = 12.sp,
                    modifier = innerModifier.layoutId("description")
                )

                SecondaryTextButton(
                    label = stringResource(id = R.string.update_photo).uppercase(),
                    onClick = {},
                    modifier = innerModifier.layoutId("changePhotoBtn")
                )

                Divider(
                    modifier = innerModifier
                        .layoutId("divider1")
                        .fillMaxWidth()
                )


                Text(
                    text = stringResource(id = R.string.your_name),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = innerModifier.layoutId("nameHeader")
                )

                ProfileItemRow(
                    onEdit = { /*TODO*/ },
                    item = "Your name",
                    modifier = innerModifier.layoutId("changeName")
                )

                Text(
                    text = stringResource(id = R.string.your_phone),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = innerModifier.layoutId("phoneHeader")
                )

                ProfileItemRow(
                    onEdit = { /*TODO*/ },
                    item = "Your phone",
                    modifier = innerModifier.layoutId("changePhone")
                )

                Text(
                    text = stringResource(id = R.string.your_password),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = innerModifier.layoutId("passwordHeader")
                )

                ProfileItemRow(
                    onEdit = { /*TODO*/ },
                    item = "xxxxxxxxxxxxxx",
                    modifier = innerModifier.layoutId("changePassword")
                )

                Text(
                    text = stringResource(id = R.string.your_bio),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = innerModifier.layoutId("bioHeader")
                )

                ProfileItemRow(
                    onEdit = { /*TODO*/ },
                    item = "A little something about yourself...",
                    modifier = innerModifier.layoutId("changeBio")
                )

                Text(
                    text = stringResource(id = R.string.your_date_of_birth),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = innerModifier.layoutId("dobHeader")
                )

                ProfileItemRow(
                    onEdit = { /*TODO*/ },
                    item = "Date of birth not set",
                    modifier = innerModifier.layoutId("changeDob")
                )

                Divider(
                    modifier = innerModifier
                        .layoutId("divider2")
                        .fillMaxWidth()
                )



            }
        }

    }
}


private fun MyAccountScreenProfileConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val header = createRefFor("header")
        val avatar = createRefFor("avatar")
        val subtitle = createRefFor("subtitle")
        val description = createRefFor("description")
        val changePhotoBtn = createRefFor("changePhotoBtn")
        val divider1 = createRefFor("divider1")
        val nameHeader = createRefFor("nameHeader")
        val changeName = createRefFor("changeName")
        val phoneHeader = createRefFor("phoneHeader")
        val changePhone = createRefFor("changePhone")
        val passwordHeader = createRefFor("passwordHeader")
        val changePassword = createRefFor("changePassword")
        val bioHeader = createRefFor("bioHeader")
        val changeBio = createRefFor("changeBio")
        val dobHeader = createRefFor("dobHeader")
        val changeDob = createRefFor("changeDob")
        val divider2 = createRefFor("divider2")


        constrain(header) {
            start.linkTo(parent.start, margin)
            top.linkTo(parent.top, 16.dp)
        }

        constrain(avatar) {
            start.linkTo(header.start, 0.dp)
            top.linkTo(subtitle.top, 0.dp)
        }

        constrain(subtitle) {
            top.linkTo(header.bottom, 32.dp)
            start.linkTo(avatar.end, 16.dp)
        }

        constrain(description) {
            top.linkTo(subtitle.bottom, 4.dp)
            start.linkTo(subtitle.start, 0.dp)
        }

        constrain(changePhotoBtn) {
            start.linkTo(description.start, 0.dp)
            top.linkTo(description.bottom, 8.dp)
        }

        constrain(divider1) {
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(changePhotoBtn.bottom, 32.dp)
            width = Dimension.fillToConstraints
        }

        constrain(nameHeader) {
            top.linkTo(divider1.bottom, 28.dp)
            start.linkTo(parent.start, margin)
        }

        constrain(changeName) {
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(nameHeader.bottom, 4.dp)
            width = Dimension.fillToConstraints
        }

        constrain(phoneHeader) {
            top.linkTo(changeName.bottom, 28.dp)
            start.linkTo(parent.start, margin)
        }

        constrain(changePhone) {
            top.linkTo(phoneHeader.bottom, 4.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            width = Dimension.fillToConstraints
        }

        constrain(passwordHeader) {
            start.linkTo(parent.start, margin)
            top.linkTo(changePhone.bottom, 28.dp)
        }

        constrain(changePassword) {
            top.linkTo(passwordHeader.bottom, 4.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            width = Dimension.fillToConstraints
        }

        constrain(bioHeader) {
            start.linkTo(parent.start, margin)
            top.linkTo(changePassword.bottom, 28.dp)
        }

        constrain(changeBio) {
            top.linkTo(bioHeader.bottom, 4.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            width = Dimension.fillToConstraints
        }

        constrain(dobHeader) {
            start.linkTo(parent.start, margin)
            top.linkTo(changeBio.bottom, 28.dp)
        }

        constrain(changeDob) {
            top.linkTo(dobHeader.bottom, 4.dp)
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            width = Dimension.fillToConstraints
        }

        constrain(divider2) {
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(changeDob.bottom, 32.dp)
            width = Dimension.fillToConstraints
        }


    }
}

@Composable
fun ProfileItemRow(
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    item: String,
) {
    var rowWidth by remember { mutableStateOf(0) }

    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                rowWidth = coordinates.size.width
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = item,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Black100.copy(0.5f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .padding(8.dp)
                .width(
                    with(LocalDensity.current) {
                        (rowWidth - 68).toDp()
                    }
                ),
        )

        IconButton(
            onClick = onEdit,
            modifier = modifier
                .size(28.dp)
                .clip(CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_edit),
                contentDescription = stringResource(id = R.string.edit),
                tint = Black100,
                modifier = modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MyAccountScreenProfilePreview() {
    MyAccountScreenProfile(
        onClick = {}
    )
}

@Composable
@Preview
private fun ProfileItemRowPreview() {
    ProfileItemRow(
        onEdit = {},
        item = "khalid.isah@gmail.com fejjdbfjdbfjsdbfjlsdbjlbjndfdfd"
    )
}