package com.example.cinescan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
    startDestination: String = NavRoutes.HOME,
    modifier: Modifier = Modifier
) {
    val viewModel: PosterAnalysisViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
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

        // Ruta de detalle del análisis (preparada para cuando se implemente DetailScreen)
        composable(
            route = "${NavRoutes.DETAIL}/{analysisId}",
            arguments = listOf(
                navArgument("analysisId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val analysisId = backStackEntry.arguments?.getLong("analysisId") ?: -1L
            
            // TODO: Implementar DetailScreen cuando se complete la tarea 7.2
            // Por ahora, navegar a HOME como fallback temporal
            LaunchedEffect(analysisId) {
                if (analysisId != -1L) {
                    // Cuando se implemente DetailScreen, reemplazar este bloque
                    // con la llamada a DetailScreen pasando el analysisId
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                }
            }
            
            // Placeholder temporal - se reemplazará con DetailScreen
            HomeScreen(
                onNavigateToPreview = {
                    navController.navigate(NavRoutes.PREVIEW)
                },
                viewModel = viewModel
            )
        }
    }
}

