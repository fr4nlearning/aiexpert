package com.example.cinescan.data.local.mapper

import com.example.cinescan.domain.model.Platform
import com.example.cinescan.domain.model.PosterAnalysisResult
import com.example.cinescan.domain.model.PosterType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class AnalysisMapperTest {

    @Test
    fun `toEntity preserves fechaEstreno and fechaEstrenoTimestamp from PosterAnalysisResult`() {
        val timestamp = 1704672000000L // Example timestamp
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "8 de diciembre 2024",
            fechaEstrenoTimestamp = timestamp
        ).toEntity()
        
        assertEquals("8 de diciembre 2024", result.fechaEstrenoTexto)
        assertEquals(timestamp, result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with null fechaEstreno returns null values`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = null,
            fechaEstrenoTimestamp = null
        ).toEntity()
        
        assertNull(result.fechaEstrenoTexto)
        assertNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with fechaEstreno but null timestamp preserves both`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "Próximamente",
            fechaEstrenoTimestamp = null
        ).toEntity()
        
        assertEquals("Próximamente", result.fechaEstrenoTexto)
        assertNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity preserves titulo tipo and plataforma`() {
        val result = PosterAnalysisResult(
            titulo = "Dune",
            tipo = PosterType.SERIE,
            plataforma = Platform.AMAZON,
            fechaEstreno = "8 de diciembre 2024",
            fechaEstrenoTimestamp = 1704672000000L
        ).toEntity()
        
        assertEquals("Dune", result.titulo)
        assertEquals("SERIE", result.tipo)
        assertEquals("AMAZON", result.plataforma)
    }

    @Test
    fun `toEntity sets momentoAnalisis to current time`() {
        val before = System.currentTimeMillis()
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "8 de diciembre 2024",
            fechaEstrenoTimestamp = 1704672000000L
        ).toEntity()
        val after = System.currentTimeMillis()
        
        assert(result.momentoAnalisis >= before)
        assert(result.momentoAnalisis <= after)
    }

    @Test
    fun `toEntity with imagePath includes it in entity`() {
        val imagePath = "/path/to/image.jpg"
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "8 de diciembre 2024",
            fechaEstrenoTimestamp = 1704672000000L
        ).toEntity(imagePath)
        
        assertEquals(imagePath, result.imagePath)
    }

    @Test
    fun `toDomain preserves all fields including timestamp`() {
        val entity = com.example.cinescan.data.local.entity.AnalysisRecordEntity(
            id = 1L,
            titulo = "Test Movie",
            tipo = "PELÍCULA",
            plataforma = "NETFLIX",
            fechaEstrenoTexto = "8 de diciembre 2024",
            fechaEstrenoTimestamp = 1704672000000L,
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        val result = entity.toDomain()
        
        assertEquals("Test Movie", result.titulo)
        assertEquals(PosterType.PELÍCULA, result.tipo)
        assertEquals(Platform.NETFLIX, result.plataforma)
        assertEquals("8 de diciembre 2024", result.fechaEstreno)
        assertEquals(1704672000000L, result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toDomain with null timestamp preserves null`() {
        val entity = com.example.cinescan.data.local.entity.AnalysisRecordEntity(
            id = 1L,
            titulo = "Test Movie",
            tipo = "PELÍCULA",
            plataforma = "NETFLIX",
            fechaEstrenoTexto = "Próximamente",
            fechaEstrenoTimestamp = null,
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        val result = entity.toDomain()
        
        assertEquals("Próximamente", result.fechaEstreno)
        assertNull(result.fechaEstrenoTimestamp)
    }
}
