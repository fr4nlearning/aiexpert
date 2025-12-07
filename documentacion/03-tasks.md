# 03-tasks.md – Tasks (Desglose de trabajo, sin tests)

## 1. Base del proyecto y configuración

### Tarea 1.1: Crear proyecto Android en Kotlin con Jetpack Compose
- [x] Crear un proyecto nuevo en Android Studio con plantilla de Compose.
- [x] Configurar:
  - [x] `minSdk`, `targetSdk`.
  - [x] Kotlin y Compose en `build.gradle`.
- [x] Comprobar que se muestra una pantalla inicial sencilla.

### Tarea 1.2: Añadir dependencias básicas
- [x] Agregar:
  - [x] Jetpack Compose BOM + módulos de UI, material, etc.
  - [x] Navigation para Compose.
  - [x] Retrofit + OkHttp.
  - [x] Biblioteca de serialización (kotlinx.serialization o Moshi).
  - [x] (Opcional) Hilt o Koin para DI.
- [x] Sincronizar y verificar que el proyecto compila.

### Tarea 1.3: Definir estructura de paquetes
- [x] Crear paquetes:
  - [x] `ui/` (pantallas y componentes Compose).
  - [x] `presentation/` (ViewModels).
  - [x] `domain/` (modelos y casos de uso).
  - [x] `data/` (repositorios y data sources).
  - [x] `data/remote` para servicios Retrofit.

## 2. Modelado de dominio

### Tarea 2.1: Crear enums y modelos de dominio
- [x] Crear `PosterType`:
  - [x] Valores: `PELÍCULA`, `SERIE`, `DESCONOCIDO`.
- [x] Crear `Platform`:
  - [x] Valores: `NETFLIX`, `AMAZON`, `DISNEY`, `APPLE`, `DESCONOCIDA`.
- [x] Crear `PosterAnalysisResult` con:
  - [x] `titulo: String?`
  - [x] `tipo: PosterType`
  - [x] `plataforma: Platform`
  - [x] `fechaEstreno: String?` (texto, sin parseo de fecha en esta iteración).

### Tarea 2.2: Definir caso de uso
- [x] Crear `AnalyzePosterUseCase` con:
  - [x] `suspend operator fun invoke(imageBytes: ByteArray): Result<PosterAnalysisResult>`
- [x] De momento, solo la firma y la clase vacía para integrarla más tarde.

## 3. Capa de datos – Abacus.AI

### Tarea 3.1: DTO de respuesta de la IA
- [x] Crear `PosterAnalysisDto` para mapear el JSON de la IA:
  - [x] `titulo: String?`
  - [x] `tipo: String?`
  - [x] `plataforma: String?`
  - [x] `fecha_estreno: String?`

### Tarea 3.2: Definir interfaz Retrofit
- [x] Crear `AbacusApiService`:
  - [x] Método `analyzePoster(...)` que:
    - [x] Reciba imagen y prompt según formato de Abacus.AI.
    - [x] Devuelva un tipo de respuesta que contenga el JSON del análisis.
- [x] Configurar el `Retrofit` builder (baseUrl genérica que luego se rellenará).

### Tarea 3.3: Implementar `AbacusRemoteDataSource`
- [x] Crear clase que:
  - [x] Reciba `AbacusApiService`.
  - [x] Prepare la petición (imagen en base64/multipart + prompt).
  - [x] Llame al endpoint de Abacus.AI.
  - [x] Devuelva `PosterAnalysisDto` o lance error si algo falla.

### Tarea 3.4: Mapper DTO → dominio
- [x] Crear funciones o clases de mapeo:
  - [x] De `PosterAnalysisDto` a `PosterAnalysisResult`.
  - [x] Lógica:
    - [x] `tipo`:
      - [x] `"pelicula"` → `PosterType.PELÍCULA`
      - [x] `"serie"` → `PosterType.SERIE`
      - [x] otros / null → `PosterType.DESCONOCIDO`
    - [x] `plataforma`:
      - [x] `"netflix"` → `Platform.NETFLIX`
      - [x] `"amazon"` → `Platform.AMAZON`
      - [x] `"disney"` → `Platform.DISNEY`
      - [x] `"apple"` → `Platform.APPLE`
      - [x] otros / null → `Platform.DESCONOCIDA`
    - [x] `fecha_estreno`:
      - [x] Copiar tal cual a `fechaEstreno` (puede ser null).

### Tarea 3.5: Implementar `PosterRepository`
- [x] Definir interfaz `PosterRepository`:
  - [x] `suspend fun analyzePoster(imageBytes: ByteArray): PosterAnalysisResult`
- [x] Crear `PosterRepositoryImpl`:
  - [x] Usa `AbacusRemoteDataSource`.
  - [x] Gestiona errores básicos (propagación de excepciones).

## 4. Capa de dominio – Implementación del caso de uso

### Tarea 4.1: Implementar `AnalyzePosterUseCase`
- [x] Inyectar `PosterRepository`.
- [x] Implementar:
  - [x] Llamada a `repository.analyzePoster(imageBytes)`.
  - [x] Envolver el resultado en `Result.success`.
  - [x] En caso de excepción, devolver `Result.failure`.

## 5. Capa de presentación – ViewModel y estado

### Tarea 5.1: Definir `PosterUiState`
- [x] Campos:
  - [x] `selectedImage: ImageData?` (tipo a definir según cómo se manejen las imágenes).
  - [x] `isAnalyzing: Boolean`.
  - [x] `analysisResult: PosterAnalysisResult?`.
  - [x] `errorMessage: String?`.

### Tarea 5.2: Implementar `PosterAnalysisViewModel`
- [x] Inyectar `AnalyzePosterUseCase`.
- [x] Funciones:
  - [x] `onImageSelected(image: ImageData)`:
    - [x] Actualiza `selectedImage`.
    - [x] Limpia `analysisResult` y `errorMessage`.
  - [x] `analyzeSelectedImage()`:
    - [x] Si no hay imagen, no hace nada o setea un error.
    - [x] Cambia `isAnalyzing` a `true`.
    - [x] Convierte `ImageData` a `ByteArray`.
    - [x] Llama al caso de uso.
    - [x] Actualiza:
      - [x] `analysisResult` con el valor.
      - [x] `errorMessage` si hay fallo.
    - [x] Pone `isAnalyzing` a `false`.
  - [x] Opcional: `clearError()` para resetear errores.

## 6. Capa de presentación – UI con Compose

### Tarea 6.1: Configurar navegación con `NavHost`
- [x] Crear un `NavHost` con:
  - [x] Ruta `home`.
  - [x] Ruta `preview`.
  - [x] Ruta `result`.
- [x] Gestionar paso de datos (por ejemplo, id/referencia de la imagen en el ViewModel en lugar de pasarla en los argumentos).

### Tarea 6.2: Implementar `HomeScreen`
- [x] Contenido:
  - [x] Botón "Tomar foto".
  - [x] Botón "Elegir de galería".
- [x] Comportamiento:
  - [x] Manejar permisos de cámara/almacenamiento.
  - [x] Al seleccionar una imagen:
    - [x] Llamar a `viewModel.onImageSelected(...)`.
    - [x] Navegar a `preview`.

### Tarea 6.3: Implementar `PreviewScreen`
- [x] Contenido:
  - [x] Muestra la imagen seleccionada desde el estado del ViewModel.
  - [x] Botón "Analizar".
- [x] Comportamiento:
  - [x] Al pulsar "Analizar":
    - [x] Llamar a `viewModel.analyzeSelectedImage()`.
    - [x] Mostrar indicador de carga si `isAnalyzing` es `true`.
    - [x] Cuando haya `analysisResult` o `errorMessage`, navegar a `result`.

### Tarea 6.4: Implementar `ResultScreen`
- [x] Contenido:
  - [x] Muestra:
    - [x] Imagen (reutilizada desde el ViewModel).
    - [x] Título, tipo, plataforma y fecha de estreno.
  - [x] Si `errorMessage` no es null:
    - [x] Mostrar el mensaje de error y un botón "Intentar de nuevo" o "Volver".
- [x] Comportamiento:
  - [x] Botón "Nuevo análisis" para volver a `home`.

## 7. Manejo de errores y UX básica

### Tarea 7.1: Mensajes de error
- [x] Definir algunos mensajes en `strings.xml`:
  - [x] Error de red.
  - [x] Error al interpretar la respuesta de la IA.
  - [x] Permisos denegados.
- [x] Mostrar los mensajes desde la UI usando `errorMessage` del estado.

### Tarea 7.2: Indicadores de carga
- [x] Añadir un componente de loading (spinner) cuando `isAnalyzing` sea `true`, tanto en `PreviewScreen` como en `ResultScreen` si se decide.

## 8. Configuración de API Key y entorno

### Tarea 8.1: Gestión segura de la API Key de Abacus.AI
- [ ] Añadir la API key en un lugar adecuado (por ejemplo `local.properties` + `BuildConfig`) evitando subirla al control de versiones.
- [ ] Usar `BuildConfig` o mecanismo similar para leerla desde el código.

### Tarea 8.2: Parámetros de red
- [ ] Configurar timeouts en OkHttp.
- [ ] Opcional: logging interceptor en modo debug para depurar las llamadas.

---

## Notas finales de esta iteración

- No se implementan tests en esta fase.
- No se planifican mejoras futuras aquí: el objetivo es cerrar un MVP funcional.
- Una vez que esta iteración esté completa y funcionando, se puede abrir una nueva fase de SDD para:
  - Añadir mejoras.
  - Incorporar tests.
  - Expandir funcionalidades.
