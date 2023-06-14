package com.vanguard.classifiadmin.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanguard.classifiadmin.navigation.ClassifiNavHost
import com.vanguard.classifiadmin.onboarding.OnboardingDialog
import com.vanguard.classifiadmin.viewmodel.MainActivityUiState
import com.vanguard.classifiadmin.viewmodel.MainActivityViewModel

@Composable
fun ClassifiApp(
    windowSizeClass: WindowSizeClass,
    mainActivityViewModel: MainActivityViewModel = hiltViewModel<MainActivityViewModel>()
) {
    val uiState by mainActivityViewModel.uiState.collectAsStateWithLifecycle()

    Box {
        //main app navHost
        ClassifiNavHost(windowSizeClass = windowSizeClass)
        when(uiState) {
            is MainActivityUiState.Loading -> Unit
            is MainActivityUiState.Success -> {
                val shouldHideOnboarding = (uiState as MainActivityUiState.Success).data.userData.shouldHideOnboarding
                val isCurrentlySignedIn = (uiState as MainActivityUiState.Success).data.isCurrentlySignedIn
                if(!shouldHideOnboarding) {
                    //onboarding dialog
                    Box(Modifier.fillMaxSize()) {
                        OnboardingDialog(windowSizeClass = windowSizeClass, loginRequiredOnly = false)
                    }
                }
                if(shouldHideOnboarding && !isCurrentlySignedIn) {
                    //login screen
                    Box(Modifier.fillMaxSize()) {
                        OnboardingDialog(windowSizeClass = windowSizeClass, loginRequiredOnly = true)
                    }
                }
            }
        }
    }
}
