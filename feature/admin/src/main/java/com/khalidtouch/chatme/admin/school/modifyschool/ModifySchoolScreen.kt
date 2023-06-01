package com.khalidtouch.chatme.admin.school.modifyschool

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.school.SchoolScreenDefaults
import com.khalidtouch.chatme.admin.school.SchoolViewModel
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.core.common.extensions.orDefaultImageUrl
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifySchoolRoute(
    onBackPressed: () -> Unit,
    modifySchoolViewModel: ModifySchoolViewModel = hiltViewModel<ModifySchoolViewModel>(),
    schoolViewModel: SchoolViewModel,
) {
    val mySchool by schoolViewModel.observeMySchool.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val headerStyle = MaterialTheme.typography.titleMedium

            ClassifiSimpleTopAppBar(
                title = {
                    Box(Modifier.padding(start = 16.dp)) {
                        ProvideTextStyle(headerStyle) {
                            Text(
                                text = stringResource(id = R.string.modify_school)
                            )
                        }
                    }
                },
                navIcon = {
                    Box(modifier = Modifier) {
                        ClassifiIconButton(
                            onClick = onBackPressed,
                            icon = {
                                Icon(
                                    painter = painterResource(id = ClassifiIcons.Back),
                                    contentDescription = stringResource(id = R.string.go_back)
                                )
                            }
                        )
                    }
                },
                actions = {
                    Box(modifier = Modifier) {
                        ClassifiIconButton(
                            onClick = {
                                modifySchoolViewModel.onUpdateSchoolName(mySchool)
                                modifySchoolViewModel.onUpdateSchoolAddress(mySchool)
                                onBackPressed()
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = ClassifiIcons.Tick),
                                    contentDescription = stringResource(id = R.string.save)
                                )
                            }
                        )
                    }
                }
            )
        },
        content = {
            ModifySchoolScreen(
                modifier = Modifier.padding(it),
                modifySchoolViewModel = modifySchoolViewModel,
                schoolViewModel = schoolViewModel,
            )
        }
    )
}


@Composable
private fun ModifySchoolScreen(
    modifier: Modifier = Modifier,
    modifySchoolViewModel: ModifySchoolViewModel,
    schoolViewModel: SchoolViewModel,
) {
    val context = LocalContext.current
    val TAG = "ModifySchool"
    val uiState by modifySchoolViewModel.uiState.collectAsStateWithLifecycle()
    val mySchool by schoolViewModel.observeMySchool.collectAsStateWithLifecycle()
    val imageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it == null) return@rememberLauncherForActivityResult
            modifySchoolViewModel.onSchoolBannerImageChanged(it)
            modifySchoolViewModel.onUpdateSchoolBannerImage(mySchool)
        }

    LaunchedEffect(Unit) {
        Log.e(TAG, "ModifySchoolScreen: LaunchedEffect has been called")
        modifySchoolViewModel.updateFieldsFromDb(mySchool)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin:modifyschool"),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        bannerItem(
            school = mySchool,
            onChangeBannerImage = {
                imageLauncher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                    )
                )
            },
        )

        schoolNameItem(
            placeholder = context.getString(R.string.enter_school_name),
            value = uiState.schoolName,
            onValueChange = modifySchoolViewModel::onSchoolNameChanged,
            isError = false,
        )

        schoolAddressItem(
            placeholder = context.getString(R.string.school_address),
            value = uiState.schoolAddress,
            onValueChange = modifySchoolViewModel::onSchoolAddressChanged,
            isError = false,
        )
    }
}

private fun LazyListScope.bannerItem(
    school: ClassifiSchool?,
    onChangeBannerImage: () -> Unit,
) {
    item {
        Box(Modifier.fillMaxWidth()) {
            val bannerUri = when (school?.bannerImage) {
                null, "" -> Uri.parse("file://dev/null")
                else -> Uri.parse(school.bannerImage?.orDefaultImageUrl())
            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bannerUri).build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )

            Box(
                Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.White.copy(0.3f), Color.Black.copy(0.3f)),
                            0.0f,
                            500.0f,
                        )
                    )
            )

            Box(
                Modifier
                    .matchParentSize()
                    .padding(top = 8.dp, end = 8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                ClassifiIconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.outline.copy(
                            SchoolScreenDefaults.iconTintAlpha
                        )
                    ),
                    onClick = onChangeBannerImage,
                    icon = {
                        Icon(
                            painter = painterResource(id = ClassifiIcons.EditSolid),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline.copy(
                                SchoolScreenDefaults.iconTintAlpha
                            )
                        )
                    }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}


private fun LazyListScope.schoolNameItem(
    placeholder: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
) {
    item {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = value,
            onValueChange = onValueChange,
            enabled = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = placeholder.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.outline.copy(0.5f)
                    )
                )
            },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            singleLine = true,
            maxLines = 1,
            isError = isError,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.outline,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.outline,
            ),
        )
        Spacer(Modifier.height(16.dp))
    }
}

private fun LazyListScope.schoolAddressItem(
    placeholder: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
) {
    item {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .heightIn(min = 200.dp),
            value = value,
            onValueChange = onValueChange,
            enabled = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = placeholder.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.outline.copy(0.5f)
                    )
                )
            },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            singleLine = false,
            maxLines = 5,
            isError = isError,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.outline,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.outline,
            ),
        )
        Spacer(Modifier.height(16.dp))
    }
}
