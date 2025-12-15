package com.example.cinescan.data.remote.mapper

import com.example.cinescan.data.remote.dto.PosterAnalysisDto
import com.example.cinescan.domain.model.Platform
import com.example.cinescan.domain.model.PosterAnalysisResult
import com.example.cinescan.domain.model.PosterType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        val normalizedDate = dto.fechaEstreno?.normalizeDate()
        return PosterAnalysisResult(
            titulo = dto.titulo,
            tipo = mapTipo(dto.tipo),
            plataforma = mapPlataforma(dto.plataforma),
            fechaEstreno = normalizedDate?.first ?: dto.fechaEstreno,
            fechaEstrenoTimestamp = normalizedDate?.second
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
    
    /**
     * Normaliza una fecha de texto a formato estructurado.
     * 
     * @return Pair con el texto normalizado y el timestamp (si es interpretable)
     */
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
                normalized.matches(Regex("\\w+\\s+\\d{1,2}\\s+\\d{4}")) -> {
                    val parts = normalized.split(" ")
                    val month = parts[0]
                    val day = parts[1]
                    val year = parts[2]
                    val formatted = "$day de $month $year"
                    val timestamp = SimpleDateFormat("dd 'de' MMMM yyyy", Locale("es")).parse(formatted)?.time
                    Pair(formatted, timestamp)
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
}

