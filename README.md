# CineScan

Aplicación Android para analizar pósters de películas y series usando inteligencia artificial. La aplicación utiliza Abacus.AI para extraer información como título, tipo (película/serie), plataforma de streaming y fecha de estreno.

## Características

-   Selección de imágenes desde la cámara o galería
- 🤖 Análisis automático de pósters usando IA
-   Interfaz moderna con Jetpack Compose
- 🔒 Gestión segura de API Key
-   Arquitectura limpia y escalable

## ️ Tecnologías

- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - Framework de UI
- **Hilt** - Inyección de dependencias
- **Retrofit + OkHttp** - Cliente HTTP
- **Kotlinx Serialization** - Serialización JSON
- **Coil** - Carga de imágenes
- **Navigation Compose** - Navegación entre pantallas
- **Abacus.AI** - API de análisis de imágenes con IA

##  Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 11 o superior
- Android SDK mínimo: API 26 (Android 8.0)
- Android SDK objetivo: API 36
- API Key de Abacus.AI ([obtener aquí](https://abacus.ai/))

## ️ Configuración

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd aiexpert
```

### 2. Configurar la API Key

1. Copia el archivo de ejemplo:
   ```bash
   cp local.properties.example local.properties
   ```

2. Edita `local.properties` y añade tu API Key de Abacus.AI:
   ```properties
   sdk.dir=/path/to/your/Android/Sdk
   ABACUS_API_KEY=tu_api_key_aqui
   ```

   ⚠️ **Importante**: El archivo `local.properties` está en `.gitignore` y no se subirá al repositorio. Nunca compartas tu API Key públicamente.

### 3. Sincronizar el proyecto

Abre el proyecto en Android Studio y haz clic en **"Sync Project with Gradle Files"** (icono de elefante con flecha circular en la barra superior, o `File → Sync Project with Gradle Files`, o `Ctrl+Shift+O` en Linux).

## ️ Compilar y ejecutar

### Desde Android Studio

1. Abre el proyecto en Android Studio
2. Conecta un dispositivo físico o inicia un emulador
3. Haz clic en el botón "Run" (▶️) o presiona `Shift+F10`

### Desde la línea de comandos

```bash
# Compilar el proyecto
./gradlew assembleDebug

# Instalar en dispositivo conectado
./gradlew installDebug

# Ejecutar tests (cuando estén implementados)
./gradlew test
```

##  Estructura del proyecto

```
app/src/main/java/com/example/cinescan/
├── ui/                          # Capa de UI (Compose)
│   ├── navigation/              # Configuración de navegación
│   └── screens/                 # Pantallas de la aplicación
│       ├── HomeScreen.kt
│       ├── PreviewScreen.kt
│       └── ResultScreen.kt
├── presentation/                 # Capa de presentación
│   ├── PosterUiState.kt        # Estado de la UI
│   └── PosterAnalysisViewModel.kt
├── domain/                       # Capa de dominio
│   ├── model/                    # Modelos de dominio
│   │   ├── PosterType.kt
│   │   ├── Platform.kt
│   │   └── PosterAnalysisResult.kt
│   └── usecase/                 # Casos de uso
│       └── AnalyzePosterUseCase.kt
├── data/                         # Capa de datos
│   ├── remote/                   # Fuentes de datos remotas
│   │   ├── dto/                  # Data Transfer Objects
│   │   ├── AbacusApiService.kt   # Interfaz Retrofit
│   │   ├── AbacusRemoteDataSource.kt
│   │   └── di/                   # Módulos de Hilt
│   ├── repository/               # Implementación de repositorios
│   └── mapper/                   # Mappers DTO → Dominio
└── CinescanApplication.kt       # Clase Application con Hilt
```

## ️ Arquitectura

La aplicación sigue los principios de **Clean Architecture** con las siguientes capas:

- **UI**: Pantallas y componentes de Compose
- **Presentation**: ViewModels y estado de la UI
- **Domain**: Modelos de negocio y casos de uso
- **Data**: Repositorios, fuentes de datos y mapeo

##  Seguridad

- La API Key se almacena en `local.properties` (no versionado)
- Se expone a través de `BuildConfig` solo en tiempo de compilación
- Se añade automáticamente a las peticiones HTTP mediante un interceptor

##  Solución de problemas

### El proyecto no compila

1. Asegúrate de haber ejecutado **"Sync Project with Gradle Files"**
2. Verifica que `local.properties` existe y contiene `ABACUS_API_KEY`
3. Limpia y reconstruye el proyecto: `./gradlew clean build`

### Errores de referencias no resueltas (BuildConfig, etc.)

Si compila desde terminal pero Android Studio muestra errores:
- Haz clic en **"Sync Project with Gradle Files"** en Android Studio
- O usa `File → Sync Project with Gradle Files` (o `Ctrl+Shift+O` en Linux)

### La aplicación se cierra al analizar una imagen

- Verifica que la API Key está correctamente configurada en `local.properties`
- Comprueba los logs con `adb logcat | grep -i "cinescan\|error"`
- Asegúrate de tener conexión a Internet

##  Licencia

Este proyecto es privado y está destinado únicamente para uso educativo y de desarrollo.

##  Contribuciones

Este es un proyecto personal. Si tienes sugerencias o encuentras problemas, por favor abre un issue en el repositorio.

---

**Desarrollado con ❤️ usando Kotlin y Jetpack Compose**

