package com.example.cinescan.data.remote.mapper

import com.example.cinescan.data.remote.dto.PosterAnalysisDto
import com.example.cinescan.domain.model.Platform
import com.example.cinescan.domain.model.PosterType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PosterAnalysisMapperTest {

    @Test
    fun `toDomain maps pelicula and netflix case-insensitively`() {
        val dto = PosterAnalysisDto(
            titulo = "Dune",
            tipo = "PELICULA",
            plataforma = "NetFlix",
            fechaEstreno = "2025-11-20"
        )

        val result = PosterAnalysisMapper.toDomain(dto)

        assertEquals("Dune", result.titulo)
        assertEquals(PosterType.PELÍCULA, result.tipo)
        assertEquals(Platform.NETFLIX, result.plataforma)
        assertEquals("2025-11-20", result.fechaEstreno)
    }

    @Test
    fun `toDomain maps serie and amazon`() {
        val dto = PosterAnalysisDto(
            titulo = "The Rings",
            tipo = "serie",
            plataforma = "amazon",
            fechaEstreno = "2023-09-01"
        )

        val result = PosterAnalysisMapper.toDomain(dto)

        assertEquals(PosterType.SERIE, result.tipo)
        assertEquals(Platform.AMAZON, result.plataforma)
    }

    @Test
    fun `toDomain resolves disney apple and other known platforms`() {
        val disneyResult = PosterAnalysisMapper.toDomain(
            PosterAnalysisDto(tipo = "pelicula", plataforma = "disney")
        )
        val appleResult = PosterAnalysisMapper.toDomain(
            PosterAnalysisDto(tipo = "pelicula", plataforma = "apple")
        )
        val otherResult = PosterAnalysisMapper.toDomain(
            PosterAnalysisDto(tipo = "pelicula", plataforma = "otra")
        )

        assertEquals(Platform.DISNEY, disneyResult.plataforma)
        assertEquals(Platform.APPLE, appleResult.plataforma)
        assertEquals(Platform.DESCONOCIDA, otherResult.plataforma)
    }

    @Test
    fun `toDomain defaults unknown or missing values`() {
        val dto = PosterAnalysisDto(
            titulo = null,
            tipo = "documental",
            plataforma = null,
            fechaEstreno = null
        )

        val result = PosterAnalysisMapper.toDomain(dto)

        assertEquals(PosterType.DESCONOCIDO, result.tipo)
        assertEquals(Platform.DESCONOCIDA, result.plataforma)
        assertNull(result.titulo)
        assertNull(result.fechaEstreno)
    }
}
