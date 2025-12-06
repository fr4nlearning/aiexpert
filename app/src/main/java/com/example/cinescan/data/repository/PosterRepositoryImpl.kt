package com.example.cinescan.data.repository

import com.example.cinescan.data.remote.AbacusRemoteDataSource
import com.example.cinescan.data.remote.mapper.PosterAnalysisMapper
import com.example.cinescan.domain.model.PosterAnalysisResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio para el análisis de pósters.
 */
@Singleton
class PosterRepositoryImpl @Inject constructor(
    private val remoteDataSource: AbacusRemoteDataSource
) : PosterRepository {
    
    /**
     * Analiza un póster de película o serie.
     * 
     * @param imageBytes Bytes de la imagen del póster
     * @return Resultado del análisis con información del póster
     * @throws Exception si la petición falla
     */
    override suspend fun analyzePoster(imageBytes: ByteArray): PosterAnalysisResult {
        return try {
            // Obtener el DTO de la API
            val dto = remoteDataSource.analyzePoster(imageBytes)
            
            // Mapear el DTO al modelo de dominio
            PosterAnalysisMapper.toDomain(dto)
            
        } catch (e: Exception) {
            // Propagar la excepción para que sea manejada por capas superiores
            throw Exception("Error en el repositorio al analizar el póster: ${e.message}", e)
        }
    }
}

