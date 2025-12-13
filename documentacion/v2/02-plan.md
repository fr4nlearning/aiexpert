### 02-plan.md – Iteración 2 (plan técnico, sin código concreto)

## 1. Contexto técnico de partida

- App Android en Kotlin con Jetpack Compose.
- Arquitectura tipo MVVM.
- Integración ya existente con un modelo de IA remoto (Abacus.AI) para analizar pósters.
- Configuración Android:
  - compileSdk = 36.
  - targetSdk = 36.
  - minSdk = 26.

La app ya tiene:

- Pantalla principal donde se selecciona/captura imagen y se lanza el análisis.
- Lógica de red para enviar la imagen a la IA y obtener un resultado.
- Una representación interna de ese resultado (modelo de dominio).

---

## 2. Objetivos técnicos

1. Tests unitarios:
   - Cubrir lógica central (mapeos, casos de uso).
2. Almacenamiento local:
   - Persistir resultados de análisis en el dispositivo.
3. Notificaciones programadas:
   - Basadas en fechas de estreno de los títulos almacenados.

---

## 3. Enfoque para los tests unitarios

### 3.1. Qué partes se van a testear

- Conversión de los datos devueltos por la IA a los modelos internos:
  - Campos como título, tipo, plataforma.
  - Interpretación del campo “fecha de estreno” textual.
- Lógica del flujo de análisis:
  - Qué ocurre cuando la IA responde correctamente.
  - Qué ocurre cuando falla la llamada o la respuesta no es válida.
- (Opcional) Estados del ViewModel principal:
  - Estados de “cargando”, “éxito” y “error”.

### 3.2. Herramientas (a alto nivel)

- Framework de tests unitarios de Kotlin/Java (JUnit u otro estándar de Android).
- Librería de mocks para simular comportamientos de dependencias (por ejemplo, repositorios o fuentes de datos).
- Herramientas específicas para probar corrutinas y flujos (para funciones asíncronas).

### 3.3. Organización

- Tests unitarios ubicados en el módulo de la app (carpeta de tests locales).
- Estructura de paquetes para tests que refleje la de producción:
  - Dominio.
  - Datos.
  - Presentación (si se cubre ViewModel).

---

## 4. Enfoque para el almacenamiento local

### 4.1. Modelo de almacenamiento

- Se define un modelo de “registro de análisis” que represente:
  - Qué título se analizó.
  - De qué tipo es (película/serie).
  - En qué plataforma se emite, si se conoce.
  - Qué texto de fecha de estreno proporcionó la IA.
  - Qué fecha de estreno se ha podido interpretar y guardar de forma estructurada (si la hay).
  - Cuándo se realizó el análisis.
  - (Opcional) Cómo localizar la imagen en el dispositivo.

Este modelo será la base de la tabla principal del almacenamiento local.

### 4.2. Capa de acceso

- Una capa de acceso a datos local que se encargue de:
  - Insertar nuevos registros de análisis.
  - Consultar todos los análisis ordenados por fecha de análisis (para el historial).
  - Consultar un análisis concreto por su identificador (para la vista de detalle).
  - (Opcionalmente) soportar borrados futuros.

### 4.3. Integración con la lógica existente

- El flujo de análisis se extenderá para que:
  - Cuando llegue un resultado válido de la IA:
    - Ese resultado se traduzca al modelo interno.
    - Se persista en el almacenamiento local a través de una capa intermedia (repositorio).
- La capa de presentación consumirá los datos:
  - Historial: un flujo o lista que provenga del repositorio y se adapte fácilmente a Compose.
  - Detalle: lectura por id del elemento seleccionado.

---

## 5. Manejo de fechas de estreno

### 5.1. Representación

- Se mantendrá:
  - La fecha de estreno en formato “texto original” tal como la devuelve la IA.
  - Una representación “estructurada” cuando sea posible:
    - Por ejemplo, un tipo de dato de fecha que permita cálculos (días hasta el estreno).

### 5.2. Interpretación de textos de fecha

- Se diseñará una pequeña utilidad encargada de:
  - Recibir el texto de fecha (por ejemplo, “15 March 2025”, “15 de marzo de 2025”, etc.).
  - Intentar interpretarlo probando varios formatos típicos en inglés y español.
  - Devolver:
    - Una fecha válida cuando es posible.
    - O “sin fecha estructurada” cuando el texto es muy ambiguo (“Próximamente”, etc.).

Esta utilidad será clave para:

- Decidir si se puede o no programar notificaciones para ese título.
- Calcular los días que faltan hasta la fecha de estreno.

---

## 6. Notificaciones programadas

### 6.1. Estrategia general

- Cuando se guarde un análisis con fecha de estreno válida y futura:
  - Se calculará cuántos días faltan desde el momento actual.
  - Se decidirá qué avisos tienen sentido según las ventanas definidas:
    - 4 semanas (28 días) antes.
    - 3 semanas (21 días) antes.
    - 2 semanas (14 días) antes.
    - 1 semana (7 días) antes.
    - 5 días antes.
    - 3 días antes.
    - 1 día antes.
- Para cada ventana relevante:
  - Se programará una tarea en segundo plano que, llegado el momento, muestre una notificación local.

### 6.2. Soporte técnico (a alto nivel)

- Uso de la infraestructura de tareas en segundo plano de Android que:
  - Permite programar ejecuciones diferidas.
  - Sobrevive a cierres de la app y reinicios del dispositivo (dentro de las limitaciones del sistema).
- Un componente de “trabajo en segundo plano” dedicado a:
  - Recibir información mínima necesaria (id del título, título, plataforma, días restantes).
  - Construir y mostrar la notificación en el momento adecuado.
- Un “orquestador de notificaciones” que:
  - Desde la lógica de alto nivel (cuando se guarda un análisis con fecha):
    - Calcule los delays.
    - Programe las tareas con los datos apropiados.

### 6.3. Comportamiento en escenarios límite

- Si el usuario analiza un póster cuando ya faltan menos de 7 días:
  - Solo se programarán las notificaciones relevantes (5, 3, 1 día, según aplique).
- Si la fecha ya ha pasado cuando se intenta interpretar:
  - No se programa ninguna notificación.
- Si la fecha es “ininterpretable”:
  - No se programa nada, pero el análisis sí se puede guardar en el historial.

---

## 7. Permisos de notificaciones (Android 13+)

- Se declarará el permiso de notificaciones en el manifiesto.
- En tiempo de ejecución:
  - Solo en dispositivos con Android 13 o superior se pedirá el permiso explícito.
  - Se elegirá un momento razonable para pedirlo (por ejemplo, la primera vez que realmente se vaya a programar notificaciones).
- Si el usuario lo deniega:
  - Se asumirá que no quiere notificaciones.
  - El resto de funciones (analizar, guardar, ver historial) no se verán afectadas.

---

## 8. Cambios en la experiencia de UI (alto nivel)

- Nueva vista de “Historial”:
  - Lista de análisis guardados.
  - Acceso desde la pantalla principal.
- Vista de “Detalle de análisis”:
  - Información completa de un análisis concreto.
- Integración con notificaciones:
  - Al pulsar una notificación, la app abre:
    - Como mínimo, la pantalla principal.
    - Idealmente, el detalle del título correspondiente (esto puede aclararse más durante la implementación).

---

## 9. Fuera de alcance en esta iteración

- Gestión avanzada del historial:
  - Edición de registros.
  - Borrado manual desde la UI.
  - Filtros por plataforma, tipo, etc.
- Configuración de notificaciones por parte del usuario (activar/desactivar por título, frecuencia personalizada…).
- Configuración de parámetros de la IA (API key, prompt, endpoint) desde la UI.
- Sincronización de datos entre dispositivos o copia de seguridad en la nube.
