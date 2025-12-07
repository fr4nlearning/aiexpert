package com.example.cinescan.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.cinescan.presentation.PosterAnalysisViewModel

/**
 * Pantalla inicial - Placeholder temporal.
 * Se implementará completamente en la Tarea 6.2.
 */
@Composable
fun HomeScreen(
    onNavigateToPreview: () -> Unit,
    viewModel: PosterAnalysisViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Home Screen - Pendiente de implementar")
    }
}

