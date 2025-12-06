package com.example.cinescan.data.remote.mapper

import com.example.cinescan.data.remote.dto.PosterAnalysisDto
import com.example.cinescan.domain.model.Platform
import com.example.cinescan.domain.model.PosterAnalysisResult
import com.example.cinescan.domain.model.PosterType

/**
 * Mapper para convertir DTOs de la capa de datos a modelos de dominio.
 */
object PosterAnalysisMapper {
    
    /**
     * Convierte un PosterAnalysisDto a PosterAnalysisResult.
     * 
     * @param dto DTO recibido de la API
     * @return Modelo de dominio con los datos mapeados
     */
    fun toDomain(dto: PosterAnalysisDto): PosterAnalysisResult {
        return PosterAnalysisResult(
            titulo = dto.titulo,
            tipo = mapTipo(dto.tipo),
            plataforma = mapPlataforma(dto.plataforma),
            fechaEstreno = dto.fechaEstreno
        )
    }
    
    /**
     * Mapea el tipo de String a PosterType.
     */
    private fun mapTipo(tipo: String?): PosterType {
        return when (tipo?.lowercase()) {
            "pelicula" -> PosterType.PELÍCULA
            "serie" -> PosterType.SERIE
            else -> PosterType.DESCONOCIDO
        }
    }
    
    /**
     * Mapea la plataforma de String a Platform.
     */
    private fun mapPlataforma(plataforma: String?): Platform {
        return when (plataforma?.lowercase()) {
            "netflix" -> Platform.NETFLIX
            "amazon" -> Platform.AMAZON
            "disney" -> Platform.DISNEY
            "apple" -> Platform.APPLE
            "otra" -> Platform.DESCONOCIDA
            else -> Platform.DESCONOCIDA
        }
    }
}

