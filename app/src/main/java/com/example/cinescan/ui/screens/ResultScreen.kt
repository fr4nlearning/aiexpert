package com.example.cinescan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cinescan.presentation.PosterAnalysisViewModel

/**
 * Pantalla de resultados del análisis.
 */
@Composable
fun ResultScreen(
    onNavigateToHome: () -> Unit,
    viewModel: PosterAnalysisViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Resultado del Análisis",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Mostrar la imagen analizada
        uiState.selectedImage?.let { imageUri ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Póster analizado",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Mostrar resultados o error
        if (uiState.errorMessage != null) {
            // Mostrar mensaje de error
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = uiState.errorMessage ?: "Error desconocido",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        } else if (uiState.analysisResult != null) {
            // Mostrar resultados del análisis
            val result = uiState.analysisResult!!
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Título
                    ResultRow(
                        label = "Título:",
                        value = result.titulo ?: "No detectado"
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Tipo
                    ResultRow(
                        label = "Tipo:",
                        value = when (result.tipo) {
                            com.example.cinescan.domain.model.PosterType.PELÍCULA -> "Película"
                            com.example.cinescan.domain.model.PosterType.SERIE -> "Serie"
                            com.example.cinescan.domain.model.PosterType.DESCONOCIDO -> "Desconocido"
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Plataforma
                    ResultRow(
                        label = "Plataforma:",
                        value = when (result.plataforma) {
                            com.example.cinescan.domain.model.Platform.NETFLIX -> "Netflix"
                            com.example.cinescan.domain.model.Platform.AMAZON -> "Amazon Prime"
                            com.example.cinescan.domain.model.Platform.DISNEY -> "Disney+"
                            com.example.cinescan.domain.model.Platform.APPLE -> "Apple TV+"
                            com.example.cinescan.domain.model.Platform.DESCONOCIDA -> "No detectada"
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Fecha de estreno
                    ResultRow(
                        label = "Fecha de estreno:",
                        value = result.fechaEstreno ?: "No detectada"
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Botón para nuevo análisis
        Button(
            onClick = {
                viewModel.clearError()
                onNavigateToHome()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nuevo Análisis")
        }
    }
}

/**
 * Componente para mostrar una fila de resultado con etiqueta y valor.
 */
@Composable
private fun ResultRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.4f)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.6f)
        )
    }
}

