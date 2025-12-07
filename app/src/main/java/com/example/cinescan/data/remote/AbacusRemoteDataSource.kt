package com.example.cinescan.data.remote

import android.util.Base64
import com.example.cinescan.data.remote.dto.ContentDto
import com.example.cinescan.data.remote.dto.ImageUrlDto
import com.example.cinescan.data.remote.dto.MessageDto
import com.example.cinescan.data.remote.dto.OpenAIRequestDto
import com.example.cinescan.data.remote.dto.PosterAnalysisDto
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source remoto para comunicarse con Abacus.AI usando formato OpenAI.
 */
@Singleton
class AbacusRemoteDataSource @Inject constructor(
    private val apiService: AbacusApiService
) {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    /**
     * Analiza un póster de película o serie usando Abacus.AI.
     * 
     * @param imageBytes Bytes de la imagen del póster
     * @return DTO con el resultado del análisis
     * @throws Exception si la petición falla
     */
    suspend fun analyzePoster(imageBytes: ByteArray): PosterAnalysisDto {
        try {
            // Convertir la imagen a base64
            val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
            val imageDataUrl = "data:image/jpeg;base64,$base64Image"
            
            // Preparar el prompt para la IA
            val promptText = """
                Analiza esta imagen. 
                
                - Es un póster de película o serie.
                
                - analiza fecha de estreno, esto suele tener formato:
                
                -- mes dia
                
                -- dia mes
                
                el mes puede venir en inglés o español, o con algún tipo de abreviatura
                
                - analiza la plataforma si la hubiera: netflix, amazon, apple, disney
                
                
                Devuélveme ÚNICAMENTE un JSON válido con esta estructura exacta:
                {
                    "titulo": "nombre de la película/serie",
                    "tipo": "pelicula" o "serie" o null,
                    "plataforma": "netflix" o "amazon" o "disney" o "apple" o "otra" o null,
                    "fecha_estreno": "fecha si está visible" o null
                }
                
                
                No añadas texto antes ni después del JSON. Solo el JSON.
            """.trimIndent()
            
            // Crear la petición en formato OpenAI
            val request = OpenAIRequestDto(
                model = "gpt-4o",
                messages = listOf(
                    MessageDto(
                        role = "user",
                        content = listOf(
                            ContentDto(type = "text", text = promptText),
                            ContentDto(
                                type = "image_url",
                                image_url = ImageUrlDto(url = imageDataUrl)
                            )
                        )
                    )
                ),
                max_tokens = 500
            )
            
            // Llamar al endpoint de Abacus.AI
            val response = apiService.analyzePoster(request)
            
            // Extraer el contenido JSON de la respuesta
            val content = response.choices.firstOrNull()?.message?.content
                ?: throw Exception("No se recibió respuesta de la IA")
            
            // Limpiar el contenido de posibles bloques de código markdown
            val cleanedContent = content
                .trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            
            // Parsear el JSON de la respuesta
            return json.decodeFromString<PosterAnalysisDto>(cleanedContent)
            
        } catch (e: Exception) {
            // Propagar el error para que sea manejado por capas superiores
            throw Exception("Error al analizar el póster: ${e.message}", e)
        }
    }
}

