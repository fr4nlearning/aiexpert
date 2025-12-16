# Tarea 5.1: Reglas de Programación de Notificaciones

Este documento define las reglas y lógica para calcular qué ventanas de notificación deben programarse para cada análisis con fecha de estreno estructurada.

## 1. Ventanas de Notificación Definidas

Las siguientes ventanas de tiempo se programarán antes del estreno:

- **30 días antes** del estreno
- **20 días antes** del estreno
- **10 días antes** del estreno
- **5 días antes** del estreno
- **3 días antes** del estreno
- **2 días antes** del estreno
- **1 día antes** del estreno
- **El día del estreno** (0 días)

## 2. Cálculo de Días Restantes

Para cada análisis guardado con `fechaEstrenoTimestamp` válido:

1. Se obtiene el timestamp actual (`System.currentTimeMillis()`).
2. Se calcula la diferencia entre `fechaEstrenoTimestamp` y el timestamp actual.
3. Se convierte la diferencia de milisegundos a días:
   - `días = (fechaEstrenoTimestamp - timestampActual) / (1000 * 60 * 60 * 24)`
   - Se redondea hacia abajo para obtener días completos.

## 3. Determinación de Ventanas Aplicables

### Caso 1: Fecha Futura con Suficiente Tiempo
Si `díasRestantes >= 30`:
- Se programan **todas** las ventanas: 30, 20, 10, 5, 3, 2, 1 días antes, y el día del estreno (0).

### Caso 2: Fecha Futura pero con Tiempo Limitado
Si `díasRestantes < 30` pero `díasRestantes > 0`:
- Se programan **solo las ventanas que aún no han pasado**, incluyendo el día del estreno.
- Ejemplo: Si faltan 10 días, se programan: 10, 5, 3, 2, 1 días antes y el día del estreno (0) (se omiten 30, 20).

### Caso 3: Día del Estreno
Si `díasRestantes == 0`:
- Se programa **solo la notificación del día del estreno** (0).

### Caso 4: Fecha Pasada
Si `díasRestantes < 0`:
- **No se programa ninguna notificación**.
- El análisis se guarda normalmente, pero sin notificaciones.

### Caso 5: Sin Fecha Estructurada
Si `fechaEstrenoTimestamp == null`:
- **No se programa ninguna notificación**.
- Solo se guarda el análisis con el texto de fecha original.

## 4. Ejemplos de Cálculo

### Ejemplo 1: Estreno en 35 días
- Días restantes: 35
- Ventanas aplicables: [30, 20, 10, 5, 3, 2, 1, 0]
- Todas las ventanas se programan, incluyendo el día del estreno.

### Ejemplo 2: Estreno en 10 días
- Días restantes: 10
- Ventanas aplicables: [10, 5, 3, 2, 1, 0]
- Se omiten: [30, 20] (ya pasaron).

### Ejemplo 3: Estreno en 2 días
- Días restantes: 2
- Ventanas aplicables: [2, 1, 0]
- Se omiten: [30, 20, 10, 5, 3] (ya pasaron).

### Ejemplo 4: Estreno hoy
- Días restantes: 0
- Ventanas aplicables: [0]
- Solo se programa la notificación del día del estreno.

### Ejemplo 5: Estreno ya pasó
- Días restantes: negativo
- Ventanas aplicables: []
- No se programa ninguna notificación.

## 5. Implementación

La lógica se implementa en la clase `NotificationWindowCalculator` ubicada en:
- `app/src/main/java/com/example/cinescan/domain/usecase/NotificationWindowCalculator.kt`

### Funciones Principales

1. **`calculateDaysUntilRelease(releaseTimestamp: Long): Long`**
   - Calcula los días restantes hasta el estreno.
   - Retorna días completos (redondeado hacia abajo).
   - Si la fecha ya pasó, retorna 0 o negativo.

2. **`getApplicableWindows(daysUntilRelease: Long): List<Int>`**
   - Recibe los días restantes.
   - Retorna lista de ventanas aplicables (en días).
   - Si `daysUntilRelease < 0`, retorna lista vacía.
   - Si `daysUntilRelease == 0`, retorna solo [0] (día del estreno).
   - Si `daysUntilRelease > 0`, retorna las ventanas futuras incluyendo el día del estreno.

## 6. Consideraciones Adicionales

- **Zona horaria**: Los cálculos se realizan en milisegundos UTC para evitar problemas de zona horaria.
- **Precisión**: Se usa redondeo hacia abajo para días completos (no se consideran horas/minutos).
- **Actualización**: Si un análisis se guarda múltiples veces, las notificaciones anteriores deben cancelarse antes de programar nuevas (esto se manejará en tareas posteriores).

## 7. Tests Unitarios

Se deben cubrir los siguientes casos:
- Estreno en más de 30 días → todas las ventanas aplicables incluyendo día del estreno
- Estreno entre ventanas intermedias → solo ventanas futuras incluyendo día del estreno
- Estreno hoy (día 0) → solo notificación del día del estreno
- Fecha pasada → ninguna ventana
- Timestamp null → ninguna ventana (validación previa)

