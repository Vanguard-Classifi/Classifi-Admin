package com.khalidtouch.classifiadmin.feeds.mediastore

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.khalidtouch.classifiadmin.feeds.R
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.components.ClassifiTextButton
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GalleryRoute(
    onChooseImage: () -> Unit,
    onDismissDialog: () -> Unit,
    onBackPressed: () -> Unit,
    galleryViewModel: GalleryViewModel = hiltViewModel<GalleryViewModel>(),
    readExternalStoragePermissionState: PermissionState = rememberPermissionState(
        Manifest.permission.READ_EXTERNAL_STORAGE
    ),
    writeExternalStoragePermissionState: PermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ),
) {
    val TAG = "GalleryRoute"
    val uiState by galleryViewModel.uiState.collectAsStateWithLifecycle()
    val selectedMedia by galleryViewModel.selectedMedia.collectAsStateWithLifecycle()

    Log.e(TAG, "GalleryRoute: refreshing  top screen")
    GalleryScreen(
        uiState = uiState,
        onToggleSelection = galleryViewModel::onToggleSelection,
        loadFromMediaStore = galleryViewModel::loadMediaFromStore,
        onBackPressed = onBackPressed,
        readExternalStoragePermissionState = readExternalStoragePermissionState,
        writeExternalStoragePermissionState = writeExternalStoragePermissionState,
        onRequestReadExternalStorage = readExternalStoragePermissionState::launchPermissionRequest,
        onRequestWriteExternalStorage = writeExternalStoragePermissionState::launchPermissionRequest,
        onDismissDialog = onDismissDialog,
        selectedMedia = selectedMedia,
        onChooseImage = {
            galleryViewModel.onChooseImage(it)
            onChooseImage()
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun GalleryScreen(
    onChooseImage: (MediaData) -> Unit,
    selectedMedia: HashMap<String, Boolean>,
    uiState: GalleryUiState,
    onToggleSelection: (Boolean, String) -> Unit,
    loadFromMediaStore: (Context) -> Unit,
    onBackPressed: () -> Unit,
    onDismissDialog: () -> Unit,
    writeExternalStoragePermissionState: PermissionState,
    readExternalStoragePermissionState: PermissionState,
    onRequestReadExternalStorage: () -> Unit,
    onRequestWriteExternalStorage: () -> Unit,
    noPermissionScreen: @Composable () -> Unit = {
        GalleryNoPermissionScreen(
            onRequestReadExternalStoragePermission = onRequestReadExternalStorage,
            onRequestWriteExternalStoragePermission = onRequestWriteExternalStorage,
            writeExternalStoragePermissionState = writeExternalStoragePermissionState,
            readExternalStoragePermissionState = readExternalStoragePermissionState,
            onDismissDialog = onDismissDialog,
        )
    },
) {
    val TAG = "Gallery"


    Scaffold(
        topBar = {
            val numberOfImagesSelected = 5
            val add = stringResource(id = R.string.add)
            ClassifiSimpleTopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ClassifiTextButton(
                            onClick = { /*TODO*/ },
                        ) {
                            Text(
                                text = "Docs",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = ClassifiIcons.ArrowDown),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary

                            )
                        }

                    }
                },
                navIcon = {
                    ClassifiIconButton(
                        onClick = onBackPressed,
                        icon = {
                            Icon(
                                painterResource(id = ClassifiIcons.Back),
                                contentDescription = null
                            )
                        }
                    )
                },
                actions = {
                    Box {
                        ClassifiTextButton(onClick = { /*TODO*/ }) {
                            Text(
                                text = if (numberOfImagesSelected > 0) {
                                    "$add($numberOfImagesSelected)"
                                } else {
                                    add
                                },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            )
        },
        content = {
            GalleryScreen(
                modifier = Modifier.padding(it),
                uiState = uiState,
                onToggleSelection = onToggleSelection,
                selectedMedia = selectedMedia,
                loadFromMediaStore = loadFromMediaStore,
                onChooseImage = onChooseImage,
            )


            AnimatedVisibility(
                visible = uiState is GalleryUiState.Loading,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> -fullHeight },
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> -fullHeight },
                ) + fadeOut(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center,
                ) {
                    ClassifiLoadingWheel()
                }
            }


            if (
                !readExternalStoragePermissionState.status.isGranted ||
                !writeExternalStoragePermissionState.status.isGranted
            ) {
                noPermissionScreen()
            }
        }
    )
}


@Composable
private fun GalleryScreen(
    modifier: Modifier = Modifier,
    uiState: GalleryUiState,
    loadFromMediaStore: (Context) -> Unit,
    onToggleSelection: (Boolean, String) -> Unit,
    onChooseImage: (MediaData) -> Unit,
    selectedMedia: HashMap<String, Boolean>,
) {
    val TAG = "Gallery"
    val state = rememberLazyGridState()

    val context = LocalContext.current
    Log.e(TAG, "GalleryScreen: refreshing screen")

    LaunchedEffect(uiState, selectedMedia.size) {
        Log.e(TAG, "GalleryScreen: LaunchedEffect has been called")
        loadFromMediaStore(context)
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        contentPadding = PaddingValues(2.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize(),
        state = state,
    ) {
        when (uiState) {
            is GalleryUiState.Loading -> Unit
            is GalleryUiState.Success -> {
                Log.e(TAG, "GalleryScreen: ${uiState.data.mediaSelection.loadedImages.size}")
                uiState.data.mediaSelection.loadedImages
                    .chunked(25)
                    .map { images ->
                        images.map {
                            val selected =
                                uiState.data.mediaSelection.selectedMedias.contains(it.uri.toString())
                            val newSelected = selectedMedia.contains(it.uri.toString())
                            Log.e(TAG, "GalleryScreen: Gallery selected state is $newSelected")
                            Log.e(
                                TAG,
                                "GalleryScreen: Gallery selected size is ${uiState.data.mediaSelection.selectedMedias.size}"
                            )
                            galleryItem(
                                it,
                                selected = !newSelected,
                                onToggleSelection = onToggleSelection,
                                onChooseImage = onChooseImage,
                            )
                        }
                    }
            }
        }
    }
}


fun LazyGridScope.galleryItem(
    mediaData: MediaData,
    selected: Boolean,
    onToggleSelection: (Boolean, String) -> Unit,
    onChooseImage: (MediaData) -> Unit,
) {
    item {
        GalleryItem(
            mediaData = mediaData,
            selected = selected,
            onToggleSelection = onToggleSelection,
            onChooseImage = onChooseImage,
        )
    }
}


@Composable
fun GalleryItem(
    mediaData: MediaData,
    selected: Boolean,
    onToggleSelection: (Boolean, String) -> Unit,
    onChooseImage: (MediaData) -> Unit,
) {
    val TAG = "GalleryItem"
    Box(
        Modifier
            .clickable(
                enabled = true,
                onClick = { onChooseImage(mediaData) }
            )
            .size(100.dp)
            .wrapContentSize()
    ) {
        Log.e(TAG, "GalleryItem: onClick")
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(mediaData.uri).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        if (selected) {
            Box(
                modifier = Modifier
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    .matchParentSize()
            ) {
                Text(
                    "1",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                )
            }
        }
    }
}

@Composable
@Preview
private fun GalleryItemPreview() {
    val url = "https://www.petsworld.in/blog/wp-content/uploads/2014/09/funny-cat.jpg"
    GalleryItem(
        mediaData = MediaData(
            uri = Uri.parse(url),
            filename = "",
            mimeType = ""
        ),
        selected = false,
        onToggleSelection = { _, _ -> },
        onChooseImage = {},
    )
}