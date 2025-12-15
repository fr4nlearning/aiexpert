# Tarea 3.1: Modelo de Registro de Análisis para Almacenamiento

## Especificación de Campos

El modelo de almacenamiento `AnalysisRecordEntity` (entidad de Room) contiene los siguientes campos:

### Campos del Modelo de Almacenamiento

| Campo | Tipo | Descripción | Requerido |
|-------|------|-------------|-----------|
| `id` | `Long` | Identificador único generado automáticamente por Room | Sí (auto-generado) |
| `titulo` | `String?` | Título del contenido (película/serie) | No |
| `tipo` | `String` | Tipo de contenido: "PELÍCULA", "SERIE" o "DESCONOCIDO" | Sí |
| `plataforma` | `String` | Plataforma de streaming: "NETFLIX", "AMAZON", "DISNEY", "APPLE" o "DESCONOCIDA" | Sí |
| `fechaEstrenoTexto` | `String?` | Fecha de estreno en texto tal como viene de la IA (ej: "2024", "15 de marzo de 2024") | No |
| `fechaEstrenoTimestamp` | `Long?` | Fecha de estreno estructurada como timestamp (milisegundos desde epoch) si se puede calcular | No |
| `momentoAnalisis` | `Long` | Marca de tiempo del momento en que se realizó el análisis (milisegundos desde epoch) | Sí |
| `imagePath` | `String?` | Ruta de referencia a la imagen analizada (si se decide guardar) | No |

## Relación con Modelos Existentes

### Relación con `PosterAnalysisResult` (Modelo de Dominio)

El modelo de dominio `PosterAnalysisResult` contiene:
- `titulo: String?` → Mapea directamente a `AnalysisRecordEntity.titulo`
- `tipo: PosterType` → Se convierte a `String` usando `tipo.name` → `AnalysisRecordEntity.tipo`
- `plataforma: Platform` → Se convierte a `String` usando `plataforma.name` → `AnalysisRecordEntity.plataforma`
- `fechaEstreno: String?` → Mapea directamente a `AnalysisRecordEntity.fechaEstrenoTexto`

### Relación con `PosterType` (Enum)

El enum `PosterType` tiene los valores:
- `PELÍCULA`
- `SERIE`
- `DESCONOCIDO`

**Conversión a almacenamiento:**
- Se guarda como `String` usando `PosterType.name` (ej: "PELÍCULA")
- Se recupera usando `PosterType.valueOf(tipo)` donde `tipo` es el `String` almacenado

### Relación con `Platform` (Enum)

El enum `Platform` tiene los valores:
- `NETFLIX`
- `AMAZON`
- `DISNEY`
- `APPLE`
- `DESCONOCIDA`

**Conversión a almacenamiento:**
- Se guarda como `String` usando `Platform.name` (ej: "NETFLIX")
- Se recupera usando `Platform.valueOf(plataforma)` donde `plataforma` es el `String` almacenado

## Campos Adicionales del Modelo de Almacenamiento

### `id: Long`
- **Propósito**: Identificador único para cada registro de análisis
- **Generación**: Auto-generado por Room usando `@PrimaryKey(autoGenerate = true)`
- **Uso**: Permite identificar de forma única cada análisis guardado para operaciones CRUD

### `fechaEstrenoTimestamp: Long?`
- **Propósito**: Fecha de estreno estructurada calculada desde `fechaEstrenoTexto`
- **Cálculo**: Se intenta parsear `fechaEstrenoTexto` usando diferentes formatos:
  - Año completo: "2024" → `SimpleDateFormat("yyyy")`
  - Día y mes: "15 de marzo de 2024" → `SimpleDateFormat("dd MMMM yyyy", Locale("es"))`
  - Mes y año: "marzo 2024" → `SimpleDateFormat("MMMM yyyy", Locale("es"))`
- **Valor**: `null` si no se puede parsear la fecha
- **Uso**: Permite ordenar y filtrar análisis por fecha de estreno de forma eficiente

### `momentoAnalisis: Long`
- **Propósito**: Marca de tiempo del momento en que se realizó el análisis
- **Valor**: `System.currentTimeMillis()` al momento de guardar
- **Uso**: Permite ordenar el historial por fecha de análisis (más recientes primero)

### `imagePath: String?`
- **Propósito**: Referencia opcional a la imagen analizada
- **Valor**: `null` por defecto (no se guarda la imagen por ahora)
- **Uso futuro**: Si se decide guardar las imágenes localmente, este campo almacenará la ruta del archivo

## Mappers Existentes

### `PosterAnalysisResult.toEntity()`
Función de extensión que convierte un `PosterAnalysisResult` a `AnalysisRecordEntity`:
- Convierte `PosterType` y `Platform` a `String` usando `.name`
- Calcula `fechaEstrenoTimestamp` desde `fechaEstreno` si es posible
- Establece `momentoAnalisis` con `System.currentTimeMillis()`
- Permite pasar opcionalmente `imagePath`

### `AnalysisRecordEntity.toDomain()`
Función de extensión que convierte un `AnalysisRecordEntity` a `PosterAnalysisResult`:
- Convierte `String` de `tipo` a `PosterType` usando `PosterType.valueOf()`
- Convierte `String` de `plataforma` a `Platform` usando `Platform.valueOf()`
- Usa `fechaEstrenoTexto` como `fechaEstreno` en el modelo de dominio
- **Nota**: No incluye `fechaEstrenoTimestamp` ni `momentoAnalisis` en el modelo de dominio (son específicos del almacenamiento)

## Consideraciones de Diseño

1. **Separación de responsabilidades**: El modelo de almacenamiento (`AnalysisRecordEntity`) incluye campos adicionales (`id`, `momentoAnalisis`, `fechaEstrenoTimestamp`) que no están en el modelo de dominio (`PosterAnalysisResult`), manteniendo la separación entre capas.

2. **Persistencia de enums**: Los enums se almacenan como `String` para facilitar la migración y evitar problemas con cambios en los enums en futuras versiones.

3. **Fechas**: Se almacenan tanto en texto (original de la IA) como en timestamp (estructurado) para permitir:
   - Mostrar el texto original al usuario
   - Realizar operaciones de ordenamiento y filtrado eficientes

4. **Imágenes**: Por ahora no se guardan las imágenes (`imagePath` es opcional y `null`), pero el campo está preparado para futuras implementaciones.



