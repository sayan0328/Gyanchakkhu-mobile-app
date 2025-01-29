@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gyanchakkhu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.gyanchakkhu.ui.theme.GyanChakkhuTheme
import com.example.gyanchakkhu.utils.AppNavigation
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition{authViewModel.authState.value==AuthState.Loading}

        setContent {
            GyanChakkhuTheme {
                AppNavigation(authViewModel)
            }
        }
    }
}

