package com.example.cinescan.data.local.mapper

import com.example.cinescan.domain.model.Platform
import com.example.cinescan.domain.model.PosterAnalysisResult
import com.example.cinescan.domain.model.PosterType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AnalysisMapperTest {

    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    @Test
    fun `toEntity with English format December 8 returns Spanish format with current year`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "December 8"
        ).toEntity()
        
        assertEquals("8 de diciembre $currentYear", result.fechaEstrenoTexto)
        assertNotNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with English format January 15 returns Spanish format with current year`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "January 15"
        ).toEntity()
        
        assertEquals("15 de enero $currentYear", result.fechaEstrenoTexto)
        assertNotNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with Spanish format 8 diciembre returns with current year`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "8 diciembre"
        ).toEntity()
        
        assertEquals("8 diciembre $currentYear", result.fechaEstrenoTexto)
        assertNotNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with full date December 8 2024 returns correct Spanish format`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "December 8 2024"
        ).toEntity()
        
        assertEquals("8 de diciembre 2024", result.fechaEstrenoTexto)
        assertNotNull(result.fechaEstrenoTimestamp)
        
        val expectedDate = SimpleDateFormat("dd 'de' MMMM yyyy", Locale("es")).parse("8 de diciembre 2024")
        assertEquals(expectedDate?.time, result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with year only 2024 returns correct format`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "2024"
        ).toEntity()
        
        assertEquals("2024", result.fechaEstrenoTexto)
        assertNotNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with month and year December 2024 returns Spanish format`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "December 2024"
        ).toEntity()
        
        assertEquals("diciembre 2024", result.fechaEstrenoTexto)
        assertNotNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with ambiguous text Próximamente returns null timestamp`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "Próximamente"
        ).toEntity()
        
        assertEquals("Próximamente", result.fechaEstrenoTexto)
        assertNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with ambiguous text Coming Soon returns null timestamp`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "Coming Soon"
        ).toEntity()
        
        assertEquals("Coming Soon", result.fechaEstrenoTexto)
        assertNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with past date January 1 2020 returns valid timestamp`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "January 1 2020"
        ).toEntity()
        
        assertEquals("1 de enero 2020", result.fechaEstrenoTexto)
        assertNotNull(result.fechaEstrenoTimestamp)
        
        val expectedDate = SimpleDateFormat("dd 'de' MMMM yyyy", Locale("es")).parse("1 de enero 2020")
        assertEquals(expectedDate?.time, result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with mixed case DECEMBER 8 returns correct format`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "DECEMBER 8"
        ).toEntity()
        
        assertEquals("8 de diciembre $currentYear", result.fechaEstrenoTexto)
        assertNotNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity with null fechaEstreno returns null values`() {
        val result = PosterAnalysisResult(
            titulo = "Test Movie",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = null
        ).toEntity()
        
        assertNull(result.fechaEstrenoTexto)
        assertNull(result.fechaEstrenoTimestamp)
    }

    @Test
    fun `toEntity preserves titulo tipo and plataforma`() {
        val result = PosterAnalysisResult(
            titulo = "Dune",
            tipo = PosterType.SERIE,
            plataforma = Platform.AMAZON,
            fechaEstreno = "December 8"
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
            fechaEstreno = "December 8"
        ).toEntity()
        val after = System.currentTimeMillis()
        
        assert(result.momentoAnalisis >= before)
        assert(result.momentoAnalisis <= after)
    }
}
