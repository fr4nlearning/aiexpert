### 01-specify.md – Iteración 2 (tests, almacenamiento local y notificaciones)

## 1. Contexto de la app y de la iteración

La app actual (Iteración 1) ya permite:

- Analizar un póster de película o serie (desde galería o cámara).
- Enviar la imagen a un modelo de IA (Abacus.AI).
- Mostrar la información extraída: título, tipo (película/serie), plataforma, posible fecha de estreno, etc.

Esta nueva iteración no cambia el “core” (analizar un póster con IA), sino que añade tres capacidades:

1. Tests unitarios para mejorar la confianza en la evolución del código.
2. Almacenamiento local de los resultados de análisis en el dispositivo.
3. Notificaciones programadas cuando se acerquen las fechas de estreno de títulos guardados.

Restricciones técnicas relevantes del proyecto:

- compileSdk = 36 (Android 15).
- targetSdk = 36 (Android 15).
- minSdk = 26 (Android 8.0).

La app ya está en Kotlin y usa Android moderno, lo que permite usar librerías actuales de persistencia y background.

---

## 2. Usuarios y problemas que aborda esta iteración

### 2.1. Usuarios

- Personas aficionadas a cine y series que:
  - Descubren títulos viendo pósters (online u offline).
  - Quieren recordar qué han visto en pósters y cuándo salen.
- Usuarios que ya usan la versión actual de la app para:
  - Identificar rápidamente un póster.
  - Saber en qué plataforma se verá o se está emitiendo.

### 2.2. Problemas actuales (tras Iteración 1)

1. Falta de “memoria”:
   - Cada análisis es efímero: cuando cierran la app, pierden el rastro de todo lo analizado.
   - No hay historial ni forma de volver a ver lo que ya se analizó.

2. Olvidar las fechas de estreno:
   - Aunque la IA devuelva una fecha de estreno, el usuario debe:
     - Apuntarla en otro sitio, o
     - Memorizarla.
   - No hay avisos automáticos cuando se acerca el día.

3. Riesgo técnico:
   - No hay una base de tests que permita refactorizar o agregar nuevas funciones con seguridad.
   - Un cambio puede romper la lógica de análisis sin que el desarrollador se dé cuenta enseguida.

---

## 3. Qué se quiere conseguir (visión de esta iteración)

### 3.1. Para el usuario final

- Que cada póster analizado quede guardado en el dispositivo:
  - Para consultar más tarde qué series/películas le interesaron.
  - Para ver un listado tipo “historial” con los últimos pósters analizados.
- Que la app “se acuerde” de las fechas de estreno:
  - Que reciba avisos cuando se acerque el estreno de algunas películas/series guardadas.
  - Que no tenga que crear recordatorios manualmente en su calendario.

### 3.2. Para el desarrollador / mantenimiento

- Tener un conjunto mínimo pero sólido de tests unitarios:
  - Que garantice que los datos de la IA se interpretan y transforman correctamente.
  - Que permita cambiar la implementación interna sin perder la funcionalidad.

---

## 4. Experiencia de usuario deseada

### 4.1. Flujo: analizar y guardar automáticamente

1. El usuario abre la app y analiza un póster igual que antes.
2. Ve la pantalla de resultados (título, tipo, plataforma, fecha estimada de estreno…).
3. Sin tener que pulsar nada extra:
   - Esa información se guarda localmente en un “registro” de análisis.
   - Si hay fecha de estreno reconocible:
     - La app decide internamente si puede programar recordatorios (según cuántos días falten).

Desde la perspectiva del usuario:
- No necesita “entender” la base de datos ni la mecánica interna.
- Simplemente sabe que “a partir de ahora, la app se acuerda de lo que he analizado”.

---

### 4.2. Flujo: ver historial

1. Desde la pantalla principal o menú, el usuario entra en “Historial”.
2. Allí ve una lista de pósters analizados, ordenados del más reciente al más antiguo.
3. En cada elemento, ve al menos:
   - Título.
   - Tipo (película/serie).
   - Plataforma (si se conoce).
   - Una indicación de la fecha de estreno (texto) cuando está disponible.
4. Al tocar uno de los elementos:
   - Ve un detalle con la información completa que la app guardó.
   - Desde ahí puede volver atrás al historial o a la pantalla principal.

En esta iteración no es obligatorio poder editar o borrar elementos del historial (puede quedar para futuras mejoras).

---

### 4.3. Flujo: recibir notificaciones de estreno

1. Después de haber analizado varios pósters, algunos tienen fecha de estreno clara y futura.
2. Días o semanas antes de esas fechas:
   - El móvil muestra una notificación aunque la app no esté abierta.
   - El texto indica algo como:
     - “Quedan 3 días para el estreno de <Título> en <Plataforma>”.
3. El usuario puede:
   - Deslizar la notificación para descartarla.
   - Pulsarla para abrir la app (en esta iteración, con comportamiento mínimo razonable: abrir la app, idealmente al detalle de ese título o al historial).

Los puntos temporales en los que se quiere recordar al usuario son:

- 4 semanas antes del estreno.
- 3 semanas antes.
- 2 semanas antes.
- 1 semana antes.
- 5 días antes.
- 3 días antes.
- 1 día antes.

La app solo deberá programar las que tengan sentido en función de cuándo se analizó y guardó el póster (no tiene sentido programar un aviso “4 semanas antes” si ya solo faltan 5 días).

---

### 4.4. Flujo: permisos de notificaciones (Android 13+)

En dispositivos con Android 13 o superior:

1. La primera vez que la app necesite mostrar notificaciones (por ejemplo, justo cuando detecta que tiene que programar recordatorios):
   - Explica brevemente al usuario que necesita permiso para avisarle de estrenos.
2. Pide el permiso de notificaciones del sistema.
3. Si el usuario:
   - Acepta:
     - La app puede mostrar notificaciones en las fechas programadas.
   - Rechaza:
     - La app no insiste (en esta iteración).
     - El resto de funcionalidades (análisis, historial) siguen funcionando.

---

### 4.5. Expectativas de calidad (tests)

Desde el punto de vista del desarrollador:

- Al cambiar o extender la lógica:
  - Los tests servirán como “alarma temprana” si algo se rompe.
- Se cubren al menos estos aspectos:
  - Transformación de la respuesta de la IA a los modelos internos.
  - Comportamiento del flujo principal de análisis (casos de éxito y de error).
- Si en el futuro se agrega más complejidad a la lógica de notificaciones o al historial:
  - Esta base de tests será el punto de partida.

---

## 5. Criterios de éxito de la iteración

La iteración se considerará exitosa si:

- Un usuario puede:
  - Analizar un póster.
  - Cerrar la app.
  - Volver más tarde y ver ese análisis en un historial.
- Un usuario que analizó pósters con fecha de estreno futura:
  - Recibe al menos una notificación útil antes del estreno (según las ventanas definidas).
- El equipo de desarrollo:
  - Dispone de tests unitarios que validan la lógica central sin necesidad de ejecutar la app en un dispositivo.
  - Puede hacer pequeños refactors o ajustes con mayor tranquilidad.
