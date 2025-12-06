package com.example.cinescan.domain.usecase

import com.example.cinescan.domain.model.PosterAnalysisResult

/**
 * Caso de uso para analizar pósters de películas y series.
 * De momento, solo la firma y la clase vacía para integrarla más tarde.
 */
class AnalyzePosterUseCase {
    
    suspend operator fun invoke(imageBytes: ByteArray): Result<PosterAnalysisResult> {
        // TODO: Implementar la lógica de análisis del póster
        // Esta implementación se completará en tareas posteriores
        throw NotImplementedError("El caso de uso aún no está implementado")
    }
}

