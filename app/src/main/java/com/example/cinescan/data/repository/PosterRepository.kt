package com.example.cinescan.data.repository

import com.example.cinescan.domain.model.PosterAnalysisResult

/**
 * Interfaz del repositorio para el análisis de pósters.
 */
interface PosterRepository {
    
    /**
     * Analiza un póster de película o serie.
     * 
     * @param imageBytes Bytes de la imagen del póster
     * @return Resultado del análisis con información del póster
     * @throws Exception si la petición falla
     */
    suspend fun analyzePoster(imageBytes: ByteArray): PosterAnalysisResult
}

