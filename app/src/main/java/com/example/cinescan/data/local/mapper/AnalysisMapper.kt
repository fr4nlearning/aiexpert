package com.example.cinescan.data.local.mapper

import com.example.cinescan.data.local.entity.AnalysisRecordEntity
import com.example.cinescan.domain.model.Platform
import com.example.cinescan.domain.model.PosterAnalysisResult
import com.example.cinescan.domain.model.PosterType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun PosterAnalysisResult.toEntity(imagePath: String? = null): AnalysisRecordEntity {
    val normalizedDate = fechaEstreno?.normalizeDate()
    return AnalysisRecordEntity(
        titulo = titulo,
        tipo = tipo.name,
        plataforma = plataforma.name,
        fechaEstrenoTexto = normalizedDate?.first,
        fechaEstrenoTimestamp = normalizedDate?.second,
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

private fun String.normalizeDate(): Pair<String, Long?>? {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val trimmed = this.trim()

    val monthsEnToEs = mapOf(
        "january" to "enero", "february" to "febrero", "march" to "marzo",
        "april" to "abril", "may" to "mayo", "june" to "junio",
        "july" to "julio", "august" to "agosto", "september" to "septiembre",
        "october" to "octubre", "november" to "noviembre", "december" to "diciembre"
    )

    var normalized = trimmed.lowercase()
    monthsEnToEs.forEach { (en, es) ->
        normalized = normalized.replace(en, es)
    }

    return try {
        when {
            normalized.matches(Regex("\\d{4}")) -> {
                val timestamp = SimpleDateFormat("yyyy", Locale("es")).parse(normalized)?.time
                Pair(normalized, timestamp)
            }
            normalized.matches(Regex("\\d{1,2}\\s+\\w+\\s+\\d{4}")) -> {
                val timestamp = SimpleDateFormat("dd MMMM yyyy", Locale("es")).parse(normalized)?.time
                Pair(normalized, timestamp)
            }
            normalized.matches(Regex("\\w+\\s+\\d{4}")) -> {
                val timestamp = SimpleDateFormat("MMMM yyyy", Locale("es")).parse(normalized)?.time
                Pair(normalized, timestamp)
            }
            normalized.matches(Regex("\\w+\\s+\\d{1,2}")) -> {
                val parts = normalized.split(" ")
                val month = parts[0]
                val day = parts[1]
                val formatted = "$day de $month $currentYear"
                val timestamp = SimpleDateFormat("dd 'de' MMMM yyyy", Locale("es")).parse(formatted)?.time
                Pair(formatted, timestamp)
            }
            normalized.matches(Regex("\\d{1,2}\\s+\\w+")) -> {
                val withYear = "$normalized $currentYear"
                val timestamp = SimpleDateFormat("dd MMMM yyyy", Locale("es")).parse(withYear)?.time
                Pair(withYear, timestamp)
            }
            else -> Pair(trimmed, null)
        }
    } catch (e: Exception) {
        Pair(trimmed, null)
    }
}
