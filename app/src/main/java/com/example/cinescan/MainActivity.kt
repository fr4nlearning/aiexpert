package com.example.cinescan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.cinescan.ui.navigation.CinescanNavHost
import com.example.cinescan.ui.theme.CinescanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CinescanTheme {
                val navController = rememberNavController()
                val analysisId = intent.getLongExtra(EXTRA_ANALYSIS_ID, -1L)
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CinescanNavHost(
                        navController = navController,
                        startDestination = if (analysisId != -1L) {
                            com.example.cinescan.ui.navigation.NavRoutes.detailRoute(analysisId)
                        } else {
                            com.example.cinescan.ui.navigation.NavRoutes.HOME
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_ANALYSIS_ID = "analysis_id"
    }
}