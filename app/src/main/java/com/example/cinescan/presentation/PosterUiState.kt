package com.example.cinescan.presentation

import android.net.Uri
import com.example.cinescan.domain.model.PosterAnalysisResult

/**
 * Estado de la UI para el análisis de pósters.
 */
data class PosterUiState(
    /**
     * URI de la imagen seleccionada por el usuario.
     */
    val selectedImage: Uri? = null,
    
    /**
     * Indica si se está realizando un análisis en este momento.
     */
    val isAnalyzing: Boolean = false,
    
    /**
     * Resultado del análisis del póster, si está disponible.
     */
    val analysisResult: PosterAnalysisResult? = null,
    
    /**
     * Mensaje de error, si ocurrió algún problema.
     */
    val errorMessage: String? = null
)

