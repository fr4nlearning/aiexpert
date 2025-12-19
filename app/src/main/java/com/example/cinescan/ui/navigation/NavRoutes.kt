package com.example.cinescan.ui.navigation

/**
 * Rutas de navegación de la aplicación.
 */
object NavRoutes {
    const val HOME = "home"
    const val PREVIEW = "preview"
    const val RESULT = "result"
    const val DETAIL = "detail"
    
    /**
     * Construye la ruta de detalle con el ID del análisis.
     */
    fun detailRoute(analysisId: Long) = "$DETAIL/$analysisId"
}

