package com.example.cinescan.domain.usecase

/**
 * Calculadora de ventanas de notificación para estrenos.
 * 
 * Define las ventanas de tiempo antes del estreno en las que se programarán notificaciones
 * y calcula cuáles son aplicables según los días restantes hasta el estreno.
 */
object NotificationWindowCalculator {

    /**
     * Ventanas de notificación definidas (en días antes del estreno).
     * Ordenadas de mayor a menor para facilitar el filtrado.
     * Incluye el día 0 (día del estreno).
     */
    val WINDOWS = listOf(30, 20, 10, 5, 3, 2, 1, 0)

    /**
     * Calcula cuántos días completos quedan hasta el estreno.
     * 
     * @param releaseTimestamp Timestamp de la fecha de estreno en milisegundos
     * @return Número de días completos restantes. Retorna 0 o negativo si la fecha ya pasó.
     */
    fun calculateDaysUntilRelease(releaseTimestamp: Long): Long {
        val currentTime = System.currentTimeMillis()
        val differenceMillis = releaseTimestamp - currentTime
        
        // Convertir milisegundos a días (redondeo hacia abajo)
        return differenceMillis / (1000 * 60 * 60 * 24)
    }

    /**
     * Determina qué ventanas de notificación son aplicables según los días restantes.
     * 
     * @param daysUntilRelease Días completos restantes hasta el estreno
     * @return Lista de ventanas aplicables (en días antes del estreno, incluyendo 0 para el día del estreno).
     *         Retorna lista vacía si daysUntilRelease < 0.
     *         Retorna [0] si daysUntilRelease == 0 (día del estreno).
     */
    fun getApplicableWindows(daysUntilRelease: Long): List<Int> {
        // Si la fecha ya pasó (más de 1 día), no hay ventanas aplicables
        if (daysUntilRelease < 0) {
            return emptyList()
        }

        // Filtrar solo las ventanas que aún no han pasado
        // Incluye el día 0 (día del estreno) si daysUntilRelease >= 0
        return WINDOWS.filter { window ->
            daysUntilRelease >= window
        }
    }

    /**
     * Calcula los días restantes y retorna las ventanas aplicables en una sola operación.
     * 
     * @param releaseTimestamp Timestamp de la fecha de estreno en milisegundos
     * @return Lista de ventanas aplicables (incluyendo 0 para el día del estreno).
     *         Retorna lista vacía si no hay fecha válida o si la fecha ya pasó (más de 1 día).
     */
    fun calculateWindows(releaseTimestamp: Long?): List<Int> {
        if (releaseTimestamp == null) {
            return emptyList()
        }
        
        val daysUntilRelease = calculateDaysUntilRelease(releaseTimestamp)
        return getApplicableWindows(daysUntilRelease)
    }
}

