package com.vishrayne.myfirstreduxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vishrayne.myfirstreduxapp.ui.theme.MyfirstreduxappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyfirstreduxappTheme {
                MainScreen()
            }
        }
    }
}