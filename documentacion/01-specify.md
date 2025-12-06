# 01-specify.md – Specify (Especificación de producto)

## 1. Visión del producto

La aplicación permite a una persona sacar una foto o adjuntar una imagen de un póster de una película o serie, enviarla a una IA (vía API de Abacus.AI) y recibir de vuelta información estructurada:

- Título de la película o serie.
- Tipo: película o serie.
- Plataforma (si aplica): Netflix, Amazon, Disney, Apple.
- Fecha de estreno (si aparece en el póster).

La app muestra la carátula junto con estos datos detectados, permitiendo revisar la información de un vistazo.

Esta es **una primera iteración (MVP)**: sin tests automatizados ni funcionalidades avanzadas.

## 2. ¿Quién la va a usar?

- **Cinéfilos / seriéfilos** que ven un póster (en la calle, redes sociales, etc.) y quieren:
  - Recordar el título.
  - Saber en qué plataforma se estrena.
  - Consultar la fecha de estreno.

- **Creadores de contenido / bloggers** de cine y series que necesitan capturar rápido la info de un póster para luego usarla en sus contenidos.

- **Usuarios casuales** que ven un póster interesante pero no quieren buscar manualmente en Google.

## 3. Problema que resuelve

Actualmente, al ver un póster:

- El usuario debe:
  - Descifrar el título (a veces tipografías poco legibles).
  - Interpretar la fecha de estreno (formatos muy variados, idiomas distintos, abreviaturas).
  - Identificar la plataforma (iconos pequeños o mezclados en el diseño).

Esto requiere tiempo, atención y a veces búsquedas extra.

**La app simplifica esto a:**

1. Adjuntar / sacar una foto del póster.
2. Esperar unos segundos.
3. Ver la información clave ya estructurada.

## 4. Historias de usuario clave

### 4.1. Captura rápida desde cámara

**Como** usuario que ve un póster en la calle  
**quiero** sacar una foto del póster desde la app  
**para** que la IA analice la imagen y me muestre título, tipo, plataforma y fecha de estreno.

**Criterios de aceptación:**

- Desde la pantalla principal puedo abrir la cámara.
- Tras tomar la foto, se ve una vista previa y un botón “Analizar”.
- Al pulsar “Analizar”:
  - Se muestra un estado de carga.
  - Cuando la IA responde, se ve:
    - La imagen del póster.
    - Título.
    - Tipo (película/serie o “No claro”).
    - Plataforma (si se detecta).
    - Fecha de estreno (o mensaje de que no aparece).
- Si la IA no puede extraer información útil, se muestra un mensaje claro y la opción de reintentar.

### 4.2. Adjuntar una imagen desde galería

**Como** usuario que tiene imágenes de pósters en la galería  
**quiero** adjuntar una imagen desde el almacenamiento del dispositivo  
**para** que la IA la analice y obtenga la misma información.

**Criterios de aceptación:**

- Desde la pantalla principal puedo elegir imagen desde la galería.
- El flujo posterior es idéntico al de la cámara.

### 4.3. Revisión del resultado

**Como** usuario  
**quiero** ver claramente la imagen analizada y los datos devueltos  
**para** poder revisarlos de un vistazo.

**Criterios de aceptación:**

- Muestra:
  - Imagen ocupando la parte superior o una zona destacada.
  - Campos:
    - “Título”
    - “Tipo”
    - “Plataforma”
    - “Fecha de estreno”
- Los campos pueden mostrar valores `null` como:
  - Tipo no claro → “No claro”.
  - Fecha no detectada → “No encontrada”.
- El usuario puede:
  - Volver a la pantalla principal para analizar otra imagen.

### 4.4. Manejo básico de errores de IA / red

**Como** usuario  
**quiero** que la app me explique cuando hay un error (red, servidor, formato de imagen, etc.)  
**para** saber qué hacer y no quedarme bloqueado.

**Criterios de aceptación:**

- Si falla la conexión:
  - Mensaje tipo: “No se pudo conectar con el servidor. Revisa tu conexión e inténtalo de nuevo.”
  - Botón “Reintentar” o volver a la pantalla principal.
- Si la IA devuelve algo que no es JSON válido o falta algún campo clave:
  - Mensaje tipo: “Hubo un problema al interpretar la respuesta de la IA.”
  - Opción de reintentar.
- Si la imagen no parece un póster:
  - Mensaje tipo: “No parece ser un póster de película o serie. Intenta con otra imagen.”
  - Esta información dependerá de lo que devuelva la IA.

## 5. Flujo principal del usuario

1. **Pantalla de inicio**
   - Botones:
     - “Tomar foto”
     - “Elegir de galería”

2. **Selección de imagen**
   - Cámara o galería.
   - Vista previa de la imagen seleccionada.
   - Botón “Analizar”.

3. **Análisis**
   - Indicador de carga mientras se llama a la API de Abacus.AI.
   - Manejo de estados:
     - Cargando
     - Éxito
     - Error

4. **Resultados**
   - Imagen del póster.
   - Datos JSON interpretados:
     - `titulo`
     - `tipo`
     - `plataforma`
     - `fecha_estreno`
   - Botón para volver a la pantalla de inicio y analizar otra imagen.

## 6. Resultados esperados y definición de éxito

- **Éxito funcional:**
  - Para pósters razonablemente claros:
    - Detectar correctamente el título en la mayoría de los casos.
    - Distinguir película vs serie en la mayoría de los casos.
    - Identificar la plataforma si aparece el logo o texto (Netflix, Amazon, Disney, Apple).
    - Extraer una fecha de estreno legible (aunque no esté totalmente normalizada).

- **Experiencia de usuario:**
  - Flujo sencillo, con pocas acciones.
  - Mensajes claros cuando algo no sale bien.
  - Tiempos de respuesta razonables (limitados por la latencia de la API).

## 7. Límites y fuera de alcance en esta primera iteración

En esta primera iteración NO se incluye:

- Implementación de tests (ni unitarios ni de UI).
- Persistencia de historial de análisis.
- Multi-idioma en la interfaz (la app estará en español).
- Edición manual posterior de los campos.
- Login de usuario / sincronización en la nube.
- Compartir resultados a redes sociales.
- Analizar vídeos o trailers (solo imágenes estáticas).
