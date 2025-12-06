package com.example.cinescan.data.remote

import com.example.cinescan.data.remote.dto.PosterAnalysisDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source remoto para comunicarse con Abacus.AI.
 */
@Singleton
class AbacusRemoteDataSource @Inject constructor(
    private val apiService: AbacusApiService
) {
    
    /**
     * Analiza un póster de película o serie usando Abacus.AI.
     * 
     * @param imageBytes Bytes de la imagen del póster
     * @return DTO con el resultado del análisis
     * @throws Exception si la petición falla
     */
    suspend fun analyzePoster(imageBytes: ByteArray): PosterAnalysisDto {
        try {
            // Preparar la imagen como multipart
            val requestBody = imageBytes.toRequestBody(
                contentType = "image/*".toMediaTypeOrNull()
            )
            val imagePart = MultipartBody.Part.createFormData(
                name = "image",
                filename = "poster.jpg",
                body = requestBody
            )
            
            // Preparar el prompt para la IA
            val promptText = """
                Analiza esta imagen. 
                
                - Es un póster de película o serie.
                
                - analiza fecha de estreno, esto suele tener formato:
                
                -- mes dia
                
                -- dia mes
                
                el mes puede venir en inges o español, o con algun tipo de abreviatura
                
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
            
            val promptBody = promptText.toRequestBody("text/plain".toMediaTypeOrNull())
            
            // Llamar al endpoint de Abacus.AI
            return apiService.analyzePoster(
                image = imagePart,
                prompt = promptBody
            )
            
        } catch (e: Exception) {
            // Propagar el error para que sea manejado por capas superiores
            throw Exception("Error al analizar el póster: ${e.message}", e)
        }
    }
}

