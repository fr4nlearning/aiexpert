# 02-plan.md – Plan (Plan técnico y arquitectura)

## 1. Stack tecnológico

- **Plataforma móvil:** Android.
- **Lenguaje:** Kotlin.
- **UI:** Jetpack Compose.
- **Arquitectura de presentación:** MVVM.
- **Capas de la app:**
  - Capa de presentación (Compose + ViewModels).
  - Capa de dominio (casos de uso, modelos de dominio).
  - Capa de datos (repositorios, fuente de datos remota para Abacus.AI).
- **Networking:** Retrofit/OkHttp (o similar) para llamadas HTTP a la API de Abacus.AI.
- **Serialización:** kotlinx.serialization o Moshi.
- **DI (opcional pero recomendado):** Hilt o Koin.
- **Almacenamiento local:** **No** se incluye en esta primera iteración.

## 2. Arquitectura a alto nivel

### 2.1. Capas

1. **Presentación**
   - Pantallas Compose:
     - `HomeScreen`: selección de imagen (cámara/galería).
     - `PreviewScreen`: vista previa de imagen y botón “Analizar”.
     - `ResultScreen`: muestra resultados de la IA y errores.
   - `ViewModel` principal:
     - `PosterAnalysisViewModel`.

2. **Dominio**
   - Modelos de dominio:
     - `PosterAnalysisResult`:
       - `titulo: String?`
       - `tipo: PosterType` (enum: `PELÍCULA`, `SERIE`, `DESCONOCIDO`)
       - `plataforma: Platform` (enum: `NETFLIX`, `AMAZON`, `DISNEY`, `APPLE`, `DESCONOCIDA`)
       - `fechaEstreno: String?` (se mantiene como texto para simplificar en esta iteración).
   - Caso de uso:
     - `AnalyzePosterUseCase(imageBytes: ByteArray): Result<PosterAnalysisResult>`

3. **Datos**
   - Repositorio:
     - `PosterRepository` con método:
       - `suspend fun analyzePoster(imageBytes: ByteArray): PosterAnalysisResult`
   - Implementación:
     - `PosterRepositoryImpl`: usa `AbacusRemoteDataSource`.
   - Fuente de datos remota:
     - `AbacusRemoteDataSource`:
       - Construye la petición a la API de Abacus.AI con la imagen y el prompt.
       - Recibe la respuesta.
       - Devuelve un DTO que se mapeará a dominio.

## 3. Integración con API de Abacus.AI

*(Los detalles concretos pueden variar según cómo configures tu proyecto en Abacus.AI, pero el plan general es este.)*

### 3.1. Contrato de la respuesta de la IA

Se instruye al modelo para que devuelva **solo un JSON** con esta forma:

```json
{
  "titulo": "string o null",
  "tipo": "pelicula" | "serie" | null,
  "plataforma": "netflix" | "amazon" | "disney" | "apple" | null,
  "fecha_estreno": "string o null"
}
