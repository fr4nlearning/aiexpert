package com.example.cinescan.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinescan.domain.usecase.AnalyzePosterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para gestionar el análisis de pósters.
 */
@HiltViewModel
class PosterAnalysisViewModel @Inject constructor(
    private val analyzePosterUseCase: AnalyzePosterUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(PosterUiState())
    val uiState: StateFlow<PosterUiState> = _uiState.asStateFlow()

    /**
     * Maneja la selección de una imagen por parte del usuario.
     * 
     * @param imageUri URI de la imagen seleccionada
     */
    fun onImageSelected(imageUri: Uri) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedImage = imageUri,
                analysisResult = null,
                errorMessage = null
            )
        }
    }

    /**
     * Analiza la imagen seleccionada actualmente.
     */
    fun analyzeSelectedImage() {
        val currentImage = _uiState.value.selectedImage
        
        if (currentImage == null) {
            _uiState.update { it.copy(errorMessage = "No hay imagen seleccionada") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzing = true, errorMessage = null) }

            try {
                val imageBytes = uriToByteArray(currentImage)
                
                if (imageBytes == null) {
                    _uiState.update { 
                        it.copy(
                            isAnalyzing = false,
                            errorMessage = "Error al leer la imagen"
                        )
                    }
                    return@launch
                }

                val result = analyzePosterUseCase(imageBytes)

                result.onSuccess { posterAnalysis ->
                    _uiState.update { 
                        it.copy(
                            isAnalyzing = false,
                            analysisResult = posterAnalysis,
                            errorMessage = null
                        )
                    }
                }.onFailure { exception ->
                    _uiState.update { 
                        it.copy(
                            isAnalyzing = false,
                            errorMessage = exception.message ?: "Error desconocido al analizar el póster"
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isAnalyzing = false,
                        errorMessage = e.message ?: "Error inesperado"
                    )
                }
            }
        }
    }

    /**
     * Limpia el mensaje de error actual.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Convierte un URI a ByteArray.
     * 
     * @param uri URI de la imagen
     * @return ByteArray de la imagen o null si hay error
     */
    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            null
        }
    }
}

