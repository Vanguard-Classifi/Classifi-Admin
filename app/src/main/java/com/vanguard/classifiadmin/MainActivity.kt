package com.vanguard.classifiadmin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanguard.classifiadmin.router.ClassifiApp
import com.vanguard.classifiadmin.ui.theme.ClassifiAdminTheme
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val TAG = "MainActivity"
    private lateinit var viewModel: MainViewModel

    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = hiltViewModel<MainViewModel>()
            ClassifiApp(viewModel = viewModel, finishActivity = { finish() })
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: onPause has been called")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop: onStop has been called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: onDestroy has been called")
    }
}
