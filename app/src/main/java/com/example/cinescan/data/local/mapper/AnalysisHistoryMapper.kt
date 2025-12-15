package com.example.cinescan.data.local.mapper

import com.example.cinescan.data.local.entity.AnalysisRecordEntity
import com.example.cinescan.domain.model.AnalysisHistoryItem

fun AnalysisRecordEntity.toHistoryItem(): AnalysisHistoryItem {
    return AnalysisHistoryItem(
        id = id,
        titulo = titulo,
        tipoEtiqueta = tipo,
        plataforma = plataforma,
        fechaEstrenoTexto = fechaEstrenoTexto,
        momentoAnalisis = momentoAnalisis,
        imagePath = imagePath
    )
}

fun List<AnalysisRecordEntity>.toHistoryItems(): List<AnalysisHistoryItem> {
    return map { it.toHistoryItem() }
}
