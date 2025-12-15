package com.example.cinescan.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cinescan.data.local.AppDatabase
import com.example.cinescan.data.local.entity.AnalysisRecordEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class AnalysisRecordDaoTest {
    
    private lateinit var database: AppDatabase
    private lateinit var dao: AnalysisRecordDao
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        
        dao = database.analysisRecordDao()
    }
    
    @After
    fun teardown() {
        database.close()
    }
    
    @Test
    fun insertAndGetById() = runTest {
        val record = AnalysisRecordEntity(
            titulo = "Test Movie",
            tipo = "Película",
            plataforma = "Netflix",
            fechaEstrenoTexto = "2024",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = "/path/to/image.jpg"
        )
        
        val id = dao.insert(record)
        val retrieved = dao.getById(id)
        
        assertNotNull(retrieved)
        assertEquals("Test Movie", retrieved?.titulo)
        assertEquals("Película", retrieved?.tipo)
        assertEquals("Netflix", retrieved?.plataforma)
    }
    
    @Test
    fun updateRecord() = runTest {
        val record = AnalysisRecordEntity(
            titulo = "Original Title",
            tipo = "Película",
            plataforma = "Netflix",
            fechaEstrenoTexto = "2024",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        val id = dao.insert(record)
        val updated = record.copy(id = id, titulo = "Updated Title")
        dao.update(updated)
        
        val retrieved = dao.getById(id)
        assertEquals("Updated Title", retrieved?.titulo)
    }
    
    @Test
    fun deleteRecord() = runTest {
        val record = AnalysisRecordEntity(
            titulo = "To Delete",
            tipo = "Serie",
            plataforma = "HBO",
            fechaEstrenoTexto = null,
            fechaEstrenoTimestamp = null,
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        val id = dao.insert(record)
        val inserted = dao.getById(id)
        assertNotNull(inserted)
        
        dao.delete(inserted!!)
        val deleted = dao.getById(id)
        assertNull(deleted)
    }
    
    @Test
    fun getAllRecords() = runTest {
        val record1 = AnalysisRecordEntity(
            titulo = "Movie 1",
            tipo = "Película",
            plataforma = "Netflix",
            fechaEstrenoTexto = "2024",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        val record2 = AnalysisRecordEntity(
            titulo = "Movie 2",
            tipo = "Serie",
            plataforma = "HBO",
            fechaEstrenoTexto = "2023",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        dao.insert(record1)
        dao.insert(record2)
        
        val allRecords = dao.getAllRecords().first()
        assertEquals(2, allRecords.size)
    }
    
    @Test
    fun searchByTitle() = runTest {
        val record1 = AnalysisRecordEntity(
            titulo = "The Matrix",
            tipo = "Película",
            plataforma = "Netflix",
            fechaEstrenoTexto = "1999",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        val record2 = AnalysisRecordEntity(
            titulo = "Inception",
            tipo = "Película",
            plataforma = "HBO",
            fechaEstrenoTexto = "2010",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        dao.insert(record1)
        dao.insert(record2)
        
        val searchResults = dao.searchByTitle("Matrix").first()
        assertEquals(1, searchResults.size)
        assertEquals("The Matrix", searchResults[0].titulo)
    }
    
    @Test
    fun getByType() = runTest {
        val record1 = AnalysisRecordEntity(
            titulo = "Movie",
            tipo = "Película",
            plataforma = "Netflix",
            fechaEstrenoTexto = "2024",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        val record2 = AnalysisRecordEntity(
            titulo = "Series",
            tipo = "Serie",
            plataforma = "HBO",
            fechaEstrenoTexto = "2023",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        dao.insert(record1)
        dao.insert(record2)
        
        val movies = dao.getByType("Película").first()
        assertEquals(1, movies.size)
        assertEquals("Movie", movies[0].titulo)
    }
    
    @Test
    fun getByPlatform() = runTest {
        val record1 = AnalysisRecordEntity(
            titulo = "Netflix Show",
            tipo = "Serie",
            plataforma = "Netflix",
            fechaEstrenoTexto = "2024",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        val record2 = AnalysisRecordEntity(
            titulo = "HBO Show",
            tipo = "Serie",
            plataforma = "HBO",
            fechaEstrenoTexto = "2023",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        dao.insert(record1)
        dao.insert(record2)
        
        val netflixShows = dao.getByPlatform("Netflix").first()
        assertEquals(1, netflixShows.size)
        assertEquals("Netflix Show", netflixShows[0].titulo)
    }
    
    @Test
    fun deleteAll() = runTest {
        val record1 = AnalysisRecordEntity(
            titulo = "Movie 1",
            tipo = "Película",
            plataforma = "Netflix",
            fechaEstrenoTexto = "2024",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        val record2 = AnalysisRecordEntity(
            titulo = "Movie 2",
            tipo = "Serie",
            plataforma = "HBO",
            fechaEstrenoTexto = "2023",
            fechaEstrenoTimestamp = System.currentTimeMillis(),
            momentoAnalisis = System.currentTimeMillis(),
            imagePath = null
        )
        
        dao.insert(record1)
        dao.insert(record2)
        
        dao.deleteAll()
        
        val allRecords = dao.getAllRecords().first()
        assertEquals(0, allRecords.size)
    }
}
