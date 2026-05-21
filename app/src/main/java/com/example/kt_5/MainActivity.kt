package com.example.kt_5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.kt_5.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as ConcertApp

        setContent {
            val navController = rememberNavController()
            AppNavigation(navController = navController, appModule = app.appModule)
        }
    }
}