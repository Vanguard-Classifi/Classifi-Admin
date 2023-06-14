package com.vanguard.classifiadmin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khalidtouch.classifiadmin.model.DarkThemeConfig
import com.khalidtouch.classifiadmin.model.ThemeBrand
import com.khalidtouch.core.designsystem.theme.ClassifiTheme
import com.vanguard.classifiadmin.ui.ClassifiApp
import com.vanguard.classifiadmin.viewmodel.MainActivityUiState
import com.vanguard.classifiadmin.viewmodel.MainActivityUiState.*
import com.vanguard.classifiadmin.viewmodel.MainActivityViewModel
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val TAG = "MainActivity"
    private lateinit var viewModel: MainViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            mainActivityViewModel = hiltViewModel<MainActivityViewModel>()
            val uiState by mainActivityViewModel.uiState.collectAsStateWithLifecycle()

            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState = uiState)

            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {  }
            }

            Firebase.auth.addAuthStateListener { auth ->
                Log.e(TAG, "onCreate: auth listener has been called")
                mainActivityViewModel.updateReAuthenticationState(auth.currentUser == null )
            }

            viewModel = hiltViewModel<MainViewModel>()


            val observeMyId by mainActivityViewModel.forceObserveMyId.collectAsStateWithLifecycle()

            Log.e(TAG, "onCreate: my current id $observeMyId")

            ClassifiTheme(
                darkTheme = darkTheme,
                androidTheme = shouldUseAndroidTheme(uiState = uiState),
                disableDynamicTheming = shouldDisableDynamicTheming(uiState = uiState)
            ) {
                ClassifiApp(windowSizeClass = calculateWindowSizeClass(activity = this))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: onPause has been called")
    }

    override fun onStop() {
        //finish()
        super.onStop()
        Log.e(TAG, "onStop: onStop has been called")
    }

    override fun onDestroy() {
        finish()
        super.onDestroy()
        Log.e(TAG, "onDestroy: onDestroy has been called")
    }
}


@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> isSystemInDarkTheme()
    is Success -> when (uiState.data.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}


@Composable
private fun shouldDisableDynamicTheming(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> false
    is Success -> !uiState.data.userData.useDynamicColor
}


@Composable
private fun shouldUseAndroidTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> false
    is Success -> when (uiState.data.userData.themeBrand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
}
