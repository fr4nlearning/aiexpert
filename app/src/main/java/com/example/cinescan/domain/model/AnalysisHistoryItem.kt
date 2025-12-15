package com.example.cinescan.domain.model

/**
 * Modelo de dominio para representar un elemento del historial de análisis en la UI.
 */
data class AnalysisHistoryItem(
    val id: Long,
    val titulo: String?,
    val tipoEtiqueta: String,
    val plataforma: String,
    val fechaEstrenoTexto: String?,
    val momentoAnalisis: Long,
    val imagePath: String?
)
