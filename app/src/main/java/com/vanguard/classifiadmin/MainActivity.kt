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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.khalidtouch.classifiadmin.model.DarkThemeConfig
import com.khalidtouch.classifiadmin.model.ThemeBrand
import com.khalidtouch.core.designsystem.theme.ClassifiTheme
import com.vanguard.classifiadmin.ui.ClassifiApp
import com.vanguard.classifiadmin.viewmodel.MainActivityUiState
import com.vanguard.classifiadmin.viewmodel.MainActivityUiState.*
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val TAG = "MainActivity"
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val uiState: MainActivityUiState by mutableStateOf(Loading)

        //update the uistate
        /*todo: */

        //keep the splashscreen on-screen until the UI state is loaded
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                Loading -> true
                is Success -> false
            }
        }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState = uiState)

            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {  }
            }

            viewModel = hiltViewModel<MainViewModel>()

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
    is Success -> when (uiState.userData.darkThemeConfig) {
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
    is Success -> !uiState.userData.useDynamicColor
}


@Composable
private fun shouldUseAndroidTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> false
    is Success -> when (uiState.userData.themeBrand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
}
