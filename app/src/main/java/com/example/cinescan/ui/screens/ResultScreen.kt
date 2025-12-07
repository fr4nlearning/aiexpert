package com.example.cinescan.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.cinescan.presentation.PosterAnalysisViewModel

/**
 * Pantalla de resultados - Placeholder temporal.
 * Se implementará completamente en la Tarea 6.4.
 */
@Composable
fun ResultScreen(
    onNavigateToHome: () -> Unit,
    viewModel: PosterAnalysisViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Result Screen - Pendiente de implementar")
    }
}

