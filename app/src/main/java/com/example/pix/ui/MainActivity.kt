package com.example.pix.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pix.ui.screens.NavigationScreen
import com.example.pix.ui.theme.PixTheme
import com.google.gson.internal.GsonBuildConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        setContent {
            PixTheme {
                NavigationScreen()
            }
        }

    }
}
