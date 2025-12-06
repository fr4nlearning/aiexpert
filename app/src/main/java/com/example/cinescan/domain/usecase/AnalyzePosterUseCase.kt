package com.example.cinescan.domain.usecase

import com.example.cinescan.data.repository.PosterRepository
import com.example.cinescan.domain.model.PosterAnalysisResult
import javax.inject.Inject

/**
 * Caso de uso para analizar pósters de películas y series.
 */
class AnalyzePosterUseCase @Inject constructor(
    private val repository: PosterRepository
) {
    
    /**
     * Analiza un póster de película o serie.
     * 
     * @param imageBytes Bytes de la imagen del póster
     * @return Result con el análisis del póster o un error
     */
    suspend operator fun invoke(imageBytes: ByteArray): Result<PosterAnalysisResult> {
        return try {
            val result = repository.analyzePoster(imageBytes)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

