package com.example.cinescan.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO para mapear el JSON de respuesta de la IA (Abacus.AI).
 */
@Serializable
data class PosterAnalysisDto(
    @SerialName("titulo")
    val titulo: String? = null,
    
    @SerialName("tipo")
    val tipo: String? = null,
    
    @SerialName("plataforma")
    val plataforma: String? = null,
    
    @SerialName("fecha_estreno")
    val fechaEstreno: String? = null
)

