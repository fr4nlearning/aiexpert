package com.example.cinescan.data.notification

import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationHelperTest {

    @Test
    fun `getNotificationMessage returns correct message for release day`() {
        val message = NotificationHelper.getNotificationMessage(0, "Netflix")
        assertEquals("¡Hoy es el estreno en Netflix!", message)
    }

    @Test
    fun `getNotificationMessage returns correct message for release day without platform`() {
        val message = NotificationHelper.getNotificationMessage(0, "")
        assertEquals("¡Hoy es el estreno!", message)
    }

    @Test
    fun `getNotificationMessage returns correct message for tomorrow`() {
        val message = NotificationHelper.getNotificationMessage(1, "HBO Max")
        assertEquals("Mañana es el estreno en HBO Max", message)
    }

    @Test
    fun `getNotificationMessage returns correct message for tomorrow without platform`() {
        val message = NotificationHelper.getNotificationMessage(1, "")
        assertEquals("Mañana es el estreno", message)
    }

    @Test
    fun `getNotificationMessage returns correct message for multiple days`() {
        val message = NotificationHelper.getNotificationMessage(5, "Disney+")
        assertEquals("Faltan 5 días para el estreno en Disney+", message)
    }

    @Test
    fun `getNotificationMessage returns correct message for multiple days without platform`() {
        val message = NotificationHelper.getNotificationMessage(10, "")
        assertEquals("Faltan 10 días para el estreno", message)
    }
}
