package com.example.cinescan.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

/**
 * Tests unitarios para NotificationWindowCalculator.
 */
class NotificationWindowCalculatorTest {

    @Test
    fun `calculateDaysUntilRelease returns correct days for future date`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 30)
        val futureTimestamp = calendar.timeInMillis

        val days = NotificationWindowCalculator.calculateDaysUntilRelease(futureTimestamp)

        // Debe retornar aproximadamente 30 días (puede variar por milisegundos)
        assertTrue("Debe retornar aproximadamente 30 días", days >= 29 && days <= 30)
    }

    @Test
    fun `calculateDaysUntilRelease returns zero or negative for past date`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -5)
        val pastTimestamp = calendar.timeInMillis

        val days = NotificationWindowCalculator.calculateDaysUntilRelease(pastTimestamp)

        assertTrue("Debe retornar 0 o negativo para fecha pasada", days <= 0)
    }

    @Test
    fun `getApplicableWindows returns all windows when more than 30 days remain`() {
        val daysUntilRelease = 35L

        val windows = NotificationWindowCalculator.getApplicableWindows(daysUntilRelease)

        assertEquals("Debe retornar todas las ventanas incluyendo día del estreno", listOf(30, 20, 10, 5, 3, 2, 1, 0), windows)
    }

    @Test
    fun `getApplicableWindows returns only future windows when less than 30 days remain`() {
        val daysUntilRelease = 10L

        val windows = NotificationWindowCalculator.getApplicableWindows(daysUntilRelease)

        assertEquals("Debe retornar solo ventanas futuras incluyendo día del estreno", listOf(10, 5, 3, 2, 1, 0), windows)
    }

    @Test
    fun `getApplicableWindows returns only 2 and 1 day windows plus release day when 2 days remain`() {
        val daysUntilRelease = 2L

        val windows = NotificationWindowCalculator.getApplicableWindows(daysUntilRelease)

        assertEquals("Debe retornar ventanas de 2, 1 día y día del estreno", listOf(2, 1, 0), windows)
    }

    @Test
    fun `getApplicableWindows returns release day when 0 days remain`() {
        val daysUntilRelease = 0L

        val windows = NotificationWindowCalculator.getApplicableWindows(daysUntilRelease)

        assertEquals("Debe retornar solo el día del estreno", listOf(0), windows)
    }

    @Test
    fun `getApplicableWindows returns empty list for negative days`() {
        val daysUntilRelease = -5L

        val windows = NotificationWindowCalculator.getApplicableWindows(daysUntilRelease)

        assertTrue("Debe retornar lista vacía para días negativos", windows.isEmpty())
    }

    @Test
    fun `calculateWindows returns all windows for future date`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 35)
        val futureTimestamp = calendar.timeInMillis

        val windows = NotificationWindowCalculator.calculateWindows(futureTimestamp)

        assertEquals("Debe retornar todas las ventanas incluyendo día del estreno", listOf(30, 20, 10, 5, 3, 2, 1, 0), windows)
    }

    @Test
    fun `calculateWindows returns empty list for null timestamp`() {
        val windows = NotificationWindowCalculator.calculateWindows(null)

        assertTrue("Debe retornar lista vacía para timestamp null", windows.isEmpty())
    }

    @Test
    fun `calculateWindows returns empty list for past date`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -5)
        val pastTimestamp = calendar.timeInMillis

        val windows = NotificationWindowCalculator.calculateWindows(pastTimestamp)

        assertTrue("Debe retornar lista vacía para fecha pasada", windows.isEmpty())
    }

    @Test
    fun `calculateWindows returns partial windows for intermediate date`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 10)
        val intermediateTimestamp = calendar.timeInMillis

        val windows = NotificationWindowCalculator.calculateWindows(intermediateTimestamp)

        assertEquals("Debe retornar solo ventanas futuras incluyendo día del estreno", listOf(10, 5, 3, 2, 1, 0), windows)
    }

    @Test
    fun `windows are ordered from largest to smallest`() {
        val windows = NotificationWindowCalculator.WINDOWS

        assertEquals("Primera ventana debe ser 30", 30, windows[0])
        assertEquals("Última ventana debe ser 0 (día del estreno)", 0, windows[windows.size - 1])
        
        // Verificar que están ordenadas de mayor a menor
        for (i in 0 until windows.size - 1) {
            assertTrue(
                "Las ventanas deben estar ordenadas de mayor a menor",
                windows[i] > windows[i + 1]
            )
        }
    }
    
    @Test
    fun `calculateWindows returns release day when release is today`() {
        val calendar = Calendar.getInstance()
        // Establecer a las 00:00:00 del día actual para asegurar que calculateDaysUntilRelease retorne 0
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayTimestamp = calendar.timeInMillis

        val windows = NotificationWindowCalculator.calculateWindows(todayTimestamp)

        // Puede retornar [0] o [1, 0] dependiendo de la hora del día, pero debe incluir 0
        assertTrue("Debe incluir el día del estreno (0)", windows.contains(0))
    }
}

