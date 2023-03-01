package com.vanguard.classifiadmin.ui.screens.profile

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.services.AvatarDownloadService
import com.vanguard.classifiadmin.domain.services.AvatarUploadService
import com.vanguard.classifiadmin.domain.services.DownloadServiceExtras
import com.vanguard.classifiadmin.domain.services.UploadServiceActions
import com.vanguard.classifiadmin.domain.services.UploadServiceExtras
import com.vanguard.classifiadmin.ui.components.DatePicker
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.SecondaryTextButton
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatarBig
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyAccountScreenProfile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    viewModel: MainViewModel,
    onSaveChanges: () -> Unit,
    onEditUsername: () -> Unit,
    onEditUserPhone: () -> Unit,
    onEditUserPassword: () -> Unit,
    onEditUserBio: () -> Unit,
    onShowCountries: () -> Unit
) {
    val TAG = "MyAccountScreenProfile"
    val verticalScroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val constraints = MyAccountScreenProfileConstraints(16.dp)
    val innerModifier = Modifier
    val context = LocalContext.current
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val userByIdNetwork by viewModel.userByIdNetwork.collectAsState()
    val usernameProfile by viewModel.usernameProfile.collectAsState()
    val userPhoneProfile by viewModel.userPhoneProfile.collectAsState()
    val userBioProfile by viewModel.userBioProfile.collectAsState()
    val userDobProfile by viewModel.userDobProfile.collectAsState()
    val userAddressProfile by viewModel.userAddressProfile.collectAsState()
    val userCountryProfile by viewModel.userCountryProfile.collectAsState()
    val userStateProfile by viewModel.userStateProfile.collectAsState()
    val userCityProfile by viewModel.userCityProfile.collectAsState()
    val userPostalCodeProfile by viewModel.userPostalCodeProfile.collectAsState()
    val bottomSheetState by viewModel.accountBottomSheetState.collectAsState()
    val avatarUri by viewModel.avatarUri.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            viewModel.onAvatarUriChanged(uri)
            //start upload service
            val intent = Intent(
                context,
                AvatarUploadService::class.java,
            )
            intent.apply {
                putExtra(UploadServiceExtras.uploadedFileUri, uri)
                putExtra(UploadServiceExtras.currentUserId, currentUserIdPref)
                action = UploadServiceActions.ACTION_UPLOAD
            }
            context.startService(intent)
        }
    )


    LaunchedEffect(userByIdNetwork.data) {
        viewModel.getUserByIdNetwork(currentUserIdPref ?: "")
        Log.e(TAG, "MyAccountScreenProfile: currentUserIdPref is ${currentUserIdPref}")
        //update the fields with value from the current user
        val user = userByIdNetwork.data
        Log.e(TAG, "MyAccountScreenProfile: user is $user")
        viewModel.onUsernameProfileChanged(user?.fullname ?: "")
        viewModel.onUserPhoneProfileChanged(user?.phone ?: "")
        viewModel.onUserBioProfileChanged(user?.bio ?: "")
        viewModel.onUserDobProfileChanged(user?.dob ?: "")
        viewModel.onUserAddressProfileChanged(user?.address ?: "")
        viewModel.onUserCountryProfileChanged(user?.country ?: "")
        viewModel.onUserStateProfileChanged(user?.state ?: "")
        viewModel.onUserCityProfileChanged(user?.city ?: "")
        viewModel.onUserPostalCodeProfileChanged(user?.postalCode ?: "")
    }

    Column(modifier = Modifier.verticalScroll(verticalScroll)) {

        Card(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 8.dp, bottom = 64.dp, start = 8.dp, end = 8.dp),
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
                    label = usernameProfile ?: "",
                    onClick = {
                        //download photo
                        val intent = Intent(context, AvatarDownloadService::class.java)
                            .putExtra(DownloadServiceExtras.currentUserId, currentUserIdPref)
                            .putExtra(DownloadServiceExtras.downloadFileUri, avatarUri)
                        context.startService(intent)
                        Log.e(TAG, "MyAccountScreenProfile: start download", )
                    },
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
                    onClick = {
                        //load image from file chooser
                        launcher.launch(arrayOf("image/*"))
                    },
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
                    onEdit = onEditUsername,
                    item = usernameProfile ?: "",
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
                    onEdit = onEditUserPhone,
                    item = userPhoneProfile ?: "Phone number not set",
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
                    onEdit = onEditUserPassword,
                    item = "xxxxxxxxxxxxxx",
                    modifier = innerModifier.layoutId("changePassword")
                )

                Text(
                    text = stringResource(id = R.string.your_bio),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    maxLines = 10,
                    modifier = innerModifier.layoutId("bioHeader")
                )

                ProfileItemRow(
                    onEdit = onEditUserBio,
                    item = userBioProfile ?: "A little something about yourself...",
                    modifier = innerModifier.layoutId("changeBio")
                )

                Text(
                    text = stringResource(id = R.string.your_date_of_birth),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = innerModifier.layoutId("dobHeader")
                )

                DatePicker(
                    value = userDobProfile ?: "",
                    onValueChange = viewModel::onUserDobProfileChanged,
                    modifier = innerModifier.layoutId("changeDob")
                )


                Divider(
                    modifier = innerModifier
                        .layoutId("divider2")
                        .fillMaxWidth()
                )

                Text(
                    text = stringResource(id = R.string.contact_details).uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = innerModifier.layoutId("contactHeader")
                )

                OutlinedTextField(
                    modifier = innerModifier.layoutId("address"),
                    value = userAddressProfile ?: "",
                    onValueChange = viewModel::onUserAddressProfileChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.street_address),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100.copy(0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    )
                )

                OutlinedTextField(
                    modifier = innerModifier
                        .layoutId("country")
                        .clickable {
                            onShowCountries()
                        }
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    value = userCountryProfile ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = {
                        Text(
                            text = stringResource(id = R.string.country),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_arrow_down),
                            contentDescription = stringResource(id = R.string.down),
                            tint = MaterialTheme.colors.primary,
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100.copy(0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    )
                )


                OutlinedTextField(
                    modifier = innerModifier.layoutId("state"),
                    value = userStateProfile ?: "",
                    onValueChange = viewModel::onUserStateProfileChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.state),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100.copy(0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    )
                )


                OutlinedTextField(
                    modifier = innerModifier.layoutId("city"),
                    value = userCityProfile ?: "",
                    onValueChange = viewModel::onUserCityProfileChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.city_town),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100.copy(0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    )
                )

                OutlinedTextField(
                    modifier = innerModifier.layoutId("postalCode"),
                    value = userPostalCodeProfile ?: "",
                    onValueChange = viewModel::onUserPostalCodeProfileChanged,
                    label = {
                        Text(
                            text = stringResource(id = R.string.postal_code),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        color = Black100.copy(0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    )
                )

                Divider(
                    modifier = innerModifier
                        .layoutId("divider3")
                        .fillMaxWidth()
                )


                PrimaryTextButton(
                    label = stringResource(id = R.string.save_changes).uppercase(),
                    onClick = {
                        //save user
                        scope.launch {
                            val currentUser = userByIdNetwork.data?.toLocal()
                            currentUser?.fullname = usernameProfile
                            currentUser?.phone = userPhoneProfile
                            currentUser?.bio = userBioProfile
                            currentUser?.dob = userDobProfile
                            currentUser?.address = userAddressProfile
                            currentUser?.country = userCountryProfile
                            currentUser?.state = userStateProfile
                            currentUser?.city = userCityProfile
                            currentUser?.postalCode = userPostalCodeProfile
                            viewModel.saveUserNetwork(currentUser ?: UserModel.Invalid) {}
                        }.invokeOnCompletion { runnableBlock { onSaveChanges() } }
                    },
                    modifier = innerModifier.layoutId("saveBtn")
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
        val contactHeader = createRefFor("contactHeader")
        val address = createRefFor("address")
        val country = createRefFor("country")
        val state = createRefFor("state")
        val city = createRefFor("city")
        val postalCode = createRefFor("postalCode")
        val divider3 = createRefFor("divider3")
        val saveBtn = createRefFor("saveBtn")


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

        constrain(contactHeader) {
            top.linkTo(divider2.bottom, 28.dp)
            start.linkTo(parent.start, margin)
        }

        constrain(address) {
            width = Dimension.fillToConstraints
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(contactHeader.bottom, 16.dp)
        }

        constrain(country) {
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(address.bottom, 16.dp)
            width = Dimension.fillToConstraints
        }

        constrain(state) {
            width = Dimension.fillToConstraints
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(country.bottom, 16.dp)
        }

        constrain(city) {
            width = Dimension.fillToConstraints
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(state.bottom, 16.dp)
        }

        constrain(postalCode) {
            width = Dimension.fillToConstraints
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(city.bottom, 16.dp)
        }

        constrain(divider3) {
            width = Dimension.fillToConstraints
            start.linkTo(parent.start, margin)
            end.linkTo(parent.end, margin)
            top.linkTo(postalCode.bottom, 28.dp)
        }

        constrain(saveBtn) {
            top.linkTo(divider3.bottom, 32.dp)
            end.linkTo(parent.end, margin)
        }

    }
}

@Composable
fun ProfileItemRow(
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    item: String,
    maxLines: Int = 2,
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
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
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

@Composable
@Preview
private fun ProfileItemRowPreview() {
    ProfileItemRow(
        onEdit = {},
        item = "khalid.isah@gmail.com fejjdbfjdbfjsdbfjlsdbjlbjndfdfd"
    )
}