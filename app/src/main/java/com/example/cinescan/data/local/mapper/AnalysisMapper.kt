package com.example.cinescan.data.local.mapper

import com.example.cinescan.data.local.entity.AnalysisRecordEntity
import com.example.cinescan.domain.model.Platform
import com.example.cinescan.domain.model.PosterAnalysisResult
import com.example.cinescan.domain.model.PosterType
import java.text.SimpleDateFormat
import java.util.Locale

fun PosterAnalysisResult.toEntity(imagePath: String? = null): AnalysisRecordEntity {
    return AnalysisRecordEntity(
        titulo = titulo,
        tipo = tipo.name,
        plataforma = plataforma.name,
        fechaEstrenoTexto = fechaEstreno,
        fechaEstrenoTimestamp = fechaEstreno?.parseToTimestamp(),
        momentoAnalisis = System.currentTimeMillis(),
        imagePath = imagePath
    )
}

fun AnalysisRecordEntity.toDomain(): PosterAnalysisResult {
    return PosterAnalysisResult(
        titulo = titulo,
        tipo = PosterType.valueOf(tipo),
        plataforma = Platform.valueOf(plataforma),
        fechaEstreno = fechaEstrenoTexto
    )
}

private fun String.parseToTimestamp(): Long? {
    return try {
        when {
            this.matches(Regex("\\d{4}")) -> {
                SimpleDateFormat("yyyy", Locale.getDefault()).parse(this)?.time
            }
            this.matches(Regex("\\d{1,2}\\s+\\w+\\s+\\d{4}")) -> {
                SimpleDateFormat("dd MMMM yyyy", Locale("es")).parse(this)?.time
            }
            this.matches(Regex("\\w+\\s+\\d{4}")) -> {
                SimpleDateFormat("MMMM yyyy", Locale("es")).parse(this)?.time
            }
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}
