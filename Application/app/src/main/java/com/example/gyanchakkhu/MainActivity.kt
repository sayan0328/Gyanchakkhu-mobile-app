@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gyanchakkhu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.gyanchakkhu.ui.theme.GyanChakkhuTheme
import com.example.gyanchakkhu.utils.AppNavigation
import com.example.gyanchakkhu.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GyanChakkhuTheme {
                val authViewModel: AuthViewModel by viewModels()
                AppNavigation(authViewModel)
            }
        }
    }
}

