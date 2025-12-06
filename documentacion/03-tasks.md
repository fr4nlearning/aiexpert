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
- [ ] Crear paquetes:
  - [ ] `ui/` (pantallas y componentes Compose).
  - [ ] `ui.viewmodel/` o `presentation/` (ViewModels).
  - [ ] `domain/` (modelos y casos de uso).
  - [ ] `data/` (repositorios y data sources).
  - [ ] `network/` o dentro de `data/remote` para servicios Retrofit.

## 2. Modelado de dominio

### Tarea 2.1: Crear enums y modelos de dominio
- [ ] Crear `PosterType`:
  - [ ] Valores: `PELÍCULA`, `SERIE`, `DESCONOCIDO`.
- [ ] Crear `Platform`:
  - [ ] Valores: `NETFLIX`, `AMAZON`, `DISNEY`, `APPLE`, `DESCONOCIDA`.
- [ ] Crear `PosterAnalysisResult` con:
  - [ ] `titulo: String?`
  - [ ] `tipo: PosterType`
  - [ ] `plataforma: Platform`
  - [ ] `fechaEstreno: String?` (texto, sin parseo de fecha en esta iteración).

### Tarea 2.2: Definir caso de uso
- [ ] Crear `AnalyzePosterUseCase` con:
  - [ ] `suspend operator fun invoke(imageBytes: ByteArray): Result<PosterAnalysisResult>`
- [ ] De momento, solo la firma y la clase vacía para integrarla más tarde.

## 3. Capa de datos – Abacus.AI

### Tarea 3.1: DTO de respuesta de la IA
- [ ] Crear `PosterAnalysisDto` para mapear el JSON de la IA:
  - [ ] `titulo: String?`
  - [ ] `tipo: String?`
  - [ ] `plataforma: String?`
  - [ ] `fecha_estreno: String?`

### Tarea 3.2: Definir interfaz Retrofit
- [ ] Crear `AbacusApiService`:
  - [ ] Método `analyzePoster(...)` que:
    - [ ] Reciba imagen y prompt según formato de Abacus.AI.
    - [ ] Devuelva un tipo de respuesta que contenga el JSON del análisis.
- [ ] Configurar el `Retrofit` builder (baseUrl genérica que luego se rellenará).

### Tarea 3.3: Implementar `AbacusRemoteDataSource`
- [ ] Crear clase que:
  - [ ] Reciba `AbacusApiService`.
  - [ ] Prepare la petición (imagen en base64/multipart + prompt).
  - [ ] Llame al endpoint de Abacus.AI.
  - [ ] Devuelva `PosterAnalysisDto` o lance error si algo falla.

### Tarea 3.4: Mapper DTO → dominio
- [ ] Crear funciones o clases de mapeo:
  - [ ] De `PosterAnalysisDto` a `PosterAnalysisResult`.
  - [ ] Lógica:
    - [ ] `tipo`:
      - [ ] `"pelicula"` → `PosterType.PELÍCULA`
      - [ ] `"serie"` → `PosterType.SERIE`
      - [ ] otros / null → `PosterType.DESCONOCIDO`
    - [ ] `plataforma`:
      - [ ] `"netflix"` → `Platform.NETFLIX`
      - [ ] `"amazon"` → `Platform.AMAZON`
      - [ ] `"disney"` → `Platform.DISNEY`
      - [ ] `"apple"` → `Platform.APPLE`
      - [ ] otros / null → `Platform.DESCONOCIDA`
    - [ ] `fecha_estreno`:
      - [ ] Copiar tal cual a `fechaEstreno` (puede ser null).

### Tarea 3.5: Implementar `PosterRepository`
- [ ] Definir interfaz `PosterRepository`:
  - [ ] `suspend fun analyzePoster(imageBytes: ByteArray): PosterAnalysisResult`
- [ ] Crear `PosterRepositoryImpl`:
  - [ ] Usa `AbacusRemoteDataSource`.
  - [ ] Gestiona errores básicos (propagación de excepciones).

## 4. Capa de dominio – Implementación del caso de uso

### Tarea 4.1: Implementar `AnalyzePosterUseCase`
- [ ] Inyectar `PosterRepository`.
- [ ] Implementar:
  - [ ] Llamada a `repository.analyzePoster(imageBytes)`.
  - [ ] Envolver el resultado en `Result.success`.
  - [ ] En caso de excepción, devolver `Result.failure`.

## 5. Capa de presentación – ViewModel y estado

### Tarea 5.1: Definir `PosterUiState`
- [ ] Campos:
  - [ ] `selectedImage: ImageData?` (tipo a definir según cómo se manejen las imágenes).
  - [ ] `isAnalyzing: Boolean`.
  - [ ] `analysisResult: PosterAnalysisResult?`.
  - [ ] `errorMessage: String?`.

### Tarea 5.2: Implementar `PosterAnalysisViewModel`
- [ ] Inyectar `AnalyzePosterUseCase`.
- [ ] Funciones:
  - [ ] `onImageSelected(image: ImageData)`:
    - [ ] Actualiza `selectedImage`.
    - [ ] Limpia `analysisResult` y `errorMessage`.
  - [ ] `analyzeSelectedImage()`:
    - [ ] Si no hay imagen, no hace nada o setea un error.
    - [ ] Cambia `isAnalyzing` a `true`.
    - [ ] Convierte `ImageData` a `ByteArray`.
    - [ ] Llama al caso de uso.
    - [ ] Actualiza:
      - [ ] `analysisResult` con el valor.
      - [ ] `errorMessage` si hay fallo.
    - [ ] Pone `isAnalyzing` a `false`.
  - [ ] Opcional: `clearError()` para resetear errores.

## 6. Capa de presentación – UI con Compose

### Tarea 6.1: Configurar navegación con `NavHost`
- [ ] Crear un `NavHost` con:
  - [ ] Ruta `home`.
  - [ ] Ruta `preview`.
  - [ ] Ruta `result`.
- [ ] Gestionar paso de datos (por ejemplo, id/referencia de la imagen en el ViewModel en lugar de pasarla en los argumentos).

### Tarea 6.2: Implementar `HomeScreen`
- [ ] Contenido:
  - [ ] Botón "Tomar foto".
  - [ ] Botón "Elegir de galería".
- [ ] Comportamiento:
  - [ ] Manejar permisos de cámara/almacenamiento.
  - [ ] Al seleccionar una imagen:
    - [ ] Llamar a `viewModel.onImageSelected(...)`.
    - [ ] Navegar a `preview`.

### Tarea 6.3: Implementar `PreviewScreen`
- [ ] Contenido:
  - [ ] Muestra la imagen seleccionada desde el estado del ViewModel.
  - [ ] Botón "Analizar".
- [ ] Comportamiento:
  - [ ] Al pulsar "Analizar":
    - [ ] Llamar a `viewModel.analyzeSelectedImage()`.
    - [ ] Mostrar indicador de carga si `isAnalyzing` es `true`.
    - [ ] Cuando haya `analysisResult` o `errorMessage`, navegar a `result`.

### Tarea 6.4: Implementar `ResultScreen`
- [ ] Contenido:
  - [ ] Muestra:
    - [ ] Imagen (reutilizada desde el ViewModel).
    - [ ] Título, tipo, plataforma y fecha de estreno.
  - [ ] Si `errorMessage` no es null:
    - [ ] Mostrar el mensaje de error y un botón "Intentar de nuevo" o "Volver".
- [ ] Comportamiento:
  - [ ] Botón "Nuevo análisis" para volver a `home`.

## 7. Manejo de errores y UX básica

### Tarea 7.1: Mensajes de error
- [ ] Definir algunos mensajes en `strings.xml`:
  - [ ] Error de red.
  - [ ] Error al interpretar la respuesta de la IA.
  - [ ] Permisos denegados.
- [ ] Mostrar los mensajes desde la UI usando `errorMessage` del estado.

### Tarea 7.2: Indicadores de carga
- [ ] Añadir un componente de loading (spinner) cuando `isAnalyzing` sea `true`, tanto en `PreviewScreen` como en `ResultScreen` si se decide.

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
