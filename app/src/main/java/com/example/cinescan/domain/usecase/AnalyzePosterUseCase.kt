package com.example.cinescan.domain.usecase

import com.example.cinescan.data.local.mapper.toEntity
import com.example.cinescan.data.repository.AnalysisRepository
import com.example.cinescan.data.repository.PosterRepository
import com.example.cinescan.domain.model.PosterAnalysisResult
import javax.inject.Inject

/**
 * Caso de uso para analizar pósters de películas y series.
 */
class AnalyzePosterUseCase @Inject constructor(
    private val posterRepository: PosterRepository,
    private val analysisRepository: AnalysisRepository
) {
    
    /**
     * Analiza un póster de película o serie.
     * 
     * @param imageBytes Bytes de la imagen del póster
     * @param imagePath Ruta opcional de la imagen para guardar referencia
     * @return Result con el análisis del póster o un error
     */
    suspend operator fun invoke(
        imageBytes: ByteArray,
        imagePath: String? = null
    ): Result<PosterAnalysisResult> {
        return try {
            val result = posterRepository.analyzePoster(imageBytes)
            
            try {
                val entity = result.toEntity(imagePath)
                analysisRepository.saveAnalysis(entity)
            } catch (e: Exception) {
                println("Error al guardar análisis en BD local: ${e.message}")
            }
            
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
