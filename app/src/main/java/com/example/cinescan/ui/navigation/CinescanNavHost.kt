package com.example.cinescan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cinescan.presentation.PosterAnalysisViewModel
import com.example.cinescan.ui.screens.HomeScreen
import com.example.cinescan.ui.screens.PreviewScreen
import com.example.cinescan.ui.screens.ResultScreen

/**
 * NavHost principal de la aplicación.
 */
@Composable
fun CinescanNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // ViewModel compartido entre todas las pantallas
    val viewModel: PosterAnalysisViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME,
        modifier = modifier
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onNavigateToPreview = {
                    navController.navigate(NavRoutes.PREVIEW)
                },
                viewModel = viewModel
            )
        }

        composable(NavRoutes.PREVIEW) {
            PreviewScreen(
                onNavigateToResult = {
                    navController.navigate(NavRoutes.RESULT)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(NavRoutes.RESULT) {
            ResultScreen(
                onNavigateToHome = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
    }
}

