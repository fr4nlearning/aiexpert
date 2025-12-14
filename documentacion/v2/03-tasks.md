### 03-tasks.md – Iteración 2 (desglose de trabajo, sin código)

## 1. Base del proyecto y configuración

### Tarea 1.1: Revisar y confirmar configuración de Android
- [x] Verificar en el archivo de configuración del módulo app:
  - [x] minSdk = 26.
  - [x] targetSdk = 36.
  - [x] compileSdk = 36.
- [x] Ejecutar la app actual y comprobar que:
  - [x] El flujo de análisis existente sigue funcionando correctamente.

### Tarea 1.2: Preparar entorno de tests unitarios
- [x] Confirmar que existe un directorio de tests locales (por ejemplo, app/src/test/...).
- [x] Añadir y/o revisar dependencias necesarias para:
  - [x] Framework de tests unitarios.
  - [x] Librería de mocks.
  - [x] Soporte para corrutinas en tests.
- [x] Verificar que se puede ejecutar al menos un test "dummy" para validar la configuración.

---

## 2. Tests unitarios sobre lógica actual

### Tarea 2.1: Tests de mapeo de respuesta de IA a modelo interno
- [x] Identificar la función o componente que transforma la respuesta de la IA en el modelo de dominio usado por la app.
- [x] Diseñar casos de prueba para:
  - [x] Diferentes tipos de contenido (película, serie, desconocido).
  - [x] Diferentes plataformas (Ej.: Netflix, otra conocida, desconocida).
  - [x] Distintas combinaciones de campos presentes/ausentes.
- [x] Implementar tests que:
  - [x] Creen respuestas de ejemplo de la IA.
  - [x] Verifiquen que el modelo de dominio resultante contiene los valores esperados.

### Tarea 2.2: Tests del flujo principal de análisis
- [x] Identificar el caso de uso o función principal que:
  - [x] Recibe una imagen.
  - [x] Llama a la IA.
  - [x] Devuelve un resultado de análisis o un error.
- [x] Diseñar casos de prueba para:
  - [x] Llamada a la IA con éxito (respuesta válida).
  - [x] Error de red o excepción.
  - [x] Respuesta inválida/parcial.
- [x] Implementar tests que:
  - [x] Simulen comportamientos de la dependencia de red.
  - [x] Verifiquen que la salida del caso de uso coincide con lo esperado en cada caso.

### Tarea 2.3: Tests de ViewModel (no aplicable - fuera del scope)
**Nota**: Esta tarea no se incluye en este apartado ya que los tests de ViewModel requieren componentes de Android (Context, Application, etc.) y no son tests unitarios puros. Serían tests instrumentados o de integración, que quedan fuera del alcance de este apartado enfocado en tests unitarios puros.

- [ ] ~~Identificar el ViewModel responsable del análisis.~~
- [ ] ~~Diseñar casos para:~~
  - [ ] ~~Estado inicial (sin análisis).~~
  - [ ] ~~Estado durante el análisis (cargando).~~
  - [ ] ~~Estado final de éxito (mostrar resultado).~~
  - [ ] ~~Estado final de error (mostrar mensaje).~~
- [ ] ~~Implementar tests que:~~
  - [ ] ~~Simulen la ejecución del caso de uso.~~
  - [ ] ~~Verifiquen transiciones de estados y valores expuestos a la UI.~~

---

## 3. Diseño e implantación del almacenamiento local

### Tarea 3.1: Definir el modelo de registro de análisis para almacenamiento
- [ ] Especificar qué campos se deben guardar para cada análisis:
  - [ ] Identificador único.
  - [ ] Título.
  - [ ] Tipo (película/serie/desconocido).
  - [ ] Plataforma.
  - [ ] Fecha de estreno en texto (tal como viene de la IA).
  - [ ] Fecha de estreno estructurada (si se puede calcular).
  - [ ] Momento del análisis (marca de tiempo).
  - [ ] Referencia a la imagen (si se decide guardar).
- [ ] Documentar cómo se relacionan estos campos con los modelos ya existentes de la app.

### Tarea 3.2: Definir la interfaz de acceso local
- [ ] Decidir qué operaciones de alto nivel se necesitan:
  - [ ] Guardar un análisis.
  - [ ] Obtener todos los análisis ordenados por fecha.
  - [ ] Obtener un análisis concreto por id.
- [ ] Especificar cómo devolverán los datos para que sean consumibles por la UI:
  - [ ] Listas simples.
  - [ ] Flujos reactivos (por ejemplo, actualizaciones en tiempo real para el historial).

### Tarea 3.3: Integrar el guardado en el flujo de análisis
- [ ] Modificar el flujo de análisis existente para que:
  - [ ] Tras un resultado exitoso, transforme el modelo de dominio en el modelo de almacenamiento.
  - [ ] Llame a la interfaz de acceso local para guardar el registro.
- [ ] Asegurarse de que:
  - [ ] Los errores de guardado local no bloquean completamente el uso de la app, pero quedan registrados para diagnóstico (si aplica).

### Tarea 3.4: Preparar datos para la pantalla de historial
- [ ] Definir una representación amigable para la UI del historial:
  - [ ] Título mostrado.
  - [ ] Etiqueta de tipo (película/serie).
  - [ ] Plataforma visible.
  - [ ] Fecha de estreno en forma de texto.
- [ ] Ajustar la capa de dominio/repositorio para:
  - [ ] Exponer la colección de registros preparados para ser mostrados.

---

## 4. Manejo de fechas de estreno para notificaciones

### Tarea 4.1: Diseñar la utilidad de interpretación de fechas
- [ ] Recopilar ejemplos de formatos de fecha que el modelo de IA puede devolver.
- [ ] Definir una serie de formatos candidatos a interpretar (en inglés y español).
- [ ] Establecer reglas:
  - [ ] Cuándo se considera que una fecha es válida y concreta.
  - [ ] Cuándo se clasifica como “no interpretable” (ej.: “Próximamente”).

### Tarea 4.2: Implementar y validar la lógica de interpretación
- [ ] Implementar una función que:
  - [ ] Reciba el texto devuelto por la IA.
  - [ ] Devuelva una fecha estructurada (o indique que no hay fecha interpretable).
- [ ] Crear tests unitarios para esta lógica:
  - [ ] Caso con formato inglés correcto.
  - [ ] Caso con formato español correcto.
  - [ ] Caso con texto ambiguo.
  - [ ] Caso con fecha ya pasada.

### Tarea 4.3: Integrar la fecha estructurada en el modelo de dominio
- [ ] Ampliar el modelo de dominio actual del análisis para:
  - [ ] Incluir tanto el texto original de fecha.
  - [ ] Como la fecha estructurada (si existe).
- [ ] Ajustar mapeos desde la respuesta de la IA para rellenar estos nuevos campos.

---

## 5. Diseño e implantación de notificaciones programadas

### Tarea 5.1: Definir reglas de programación de notificaciones
- [ ] Para cada análisis con fecha estructurada:
  - [ ] Calcular cuántos días quedan hasta el estreno en el momento de guardar.
  - [ ] Determinar cuáles de las siguientes ventanas son aplicables:
    - [ ] 28 días antes.
    - [ ] 21 días antes.
    - [ ] 14 días antes.
    - [ ] 7 días antes.
    - [ ] 5 días antes.
    - [ ] 3 días antes.
    - [ ] 1 día antes.
- [ ] Documentar qué hacer cuando:
  - [ ] Ya solo faltan menos días que alguna de las ventanas.
  - [ ] La fecha ya ha pasado.

### Tarea 5.2: Definir el componente que orquesta las notificaciones
- [ ] Especificar una interfaz de “planificador de notificaciones” que:
  - [ ] Reciba:
    - [ ] Identificador del análisis.
    - [ ] Título.
    - [ ] Plataforma.
    - [ ] Fecha de estreno estructurada.
  - [ ] Se encargue de:
    - [ ] Calcular los delays.
    - [ ] Programar las notificaciones en el sistema (una por cada ventana de tiempo aplicable).

### Tarea 5.3: Definir el comportamiento de la notificación
- [ ] Decidir qué información se mostrará:
  - [ ] Título.
  - [ ] Mensaje con “X días para el estreno” y, si aplica, la plataforma.
- [ ] Decidir qué pantalla se abre al pulsar la notificación en esta iteración:
  - [ ] Opción mínima: pantalla principal.
  - [ ] Opción preferible: detalle del análisis correspondiente.

### Tarea 5.4: Integrar el planificador con el guardado de análisis
- [ ] En la lógica que guarda un análisis:
  - [ ] Si hay fecha estructurada futura:
    - [ ] Llamar al planificador de notificaciones con los datos necesarios.
- [ ] Asegurar que:
  - [ ] El fallo en la programación de una notificación no impida guardar el análisis.

---

## 6. Gestión del permiso de notificaciones (Android 13+)

### Tarea 6.1: Declarar el permiso
- [ ] Verificar que el permiso de notificaciones está declarado en el manifiesto de la app.

### Tarea 6.2: Definir el momento para solicitar el permiso
- [ ] Elegir el punto del flujo donde tenga más sentido:
  - [ ] Por ejemplo, la primera vez que se guarde un título con fecha de estreno válida.
- [ ] Diseñar un mensaje corto de explicación para el usuario:
  - [ ] “La app necesita permiso de notificaciones para avisarte cuando se acerquen los estrenos”, u otro texto apropiado.

### Tarea 6.3: Manejo de la respuesta del usuario
- [ ] Si el usuario concede el permiso:
  - [ ] Permitir la programación y visualización de notificaciones.
- [ ] Si el usuario lo deniega:
  - [ ] No insistir de manera agresiva en esta iteración.
  - [ ] Continuar permitiendo el uso normal de análisis e historial sin notificaciones.

---

## 7. UI de historial y detalle (alto nivel)

### Tarea 7.1: Definir la vista de historial
- [ ] Diseñar la estructura de la lista:
  - [ ] Qué campos se ven de cada análisis.
  - [ ] Orden por fecha de análisis descendente.
- [ ] Añadir un punto de entrada a la vista de historial:
  - [ ] Desde la pantalla principal (botón, menú, etc.).

### Tarea 7.2: Definir la vista de detalle de análisis
- [ ] Elegir qué información se muestra:
  - [ ] Título, tipo, plataforma.
  - [ ] Texto de fecha de estreno.
  - [ ] Indicación (si se desea) de que hay notificaciones programadas.
- [ ] Asegurar que:
  - [ ] Desde el historial se puede navegar al detalle.
  - [ ] Desde el detalle se puede volver atrás sin confusión.

### Tarea 7.3: Integración con notificaciones
- [ ] Definir qué hace la app cuando el usuario toca una notificación:
  - [ ] Abrir la app y, si es posible, navegar al detalle del análisis correspondiente.
- [ ] Documentar este comportamiento para futuras iteraciones, por si se quiere hacer más sofisticado.

---

## 8. Validación final de la iteración

### Tarea 8.1: Pruebas manuales de flujo completo
- [ ] Analizar un póster con fecha de estreno futura.
- [ ] Verificar que:
  - [ ] Se guarda en el historial.
  - [ ] Se han definido internamente las notificaciones (aunque sea validado de forma indirecta).
- [ ] Simular (en entorno de desarrollo) la llegada de los momentos de notificación:
  - [ ] Comprobar que el texto y el comportamiento son correctos.

### Tarea 8.2: Ejecutar el conjunto de tests unitarios
- [ ] Ejecutar todos los tests.
- [ ] Confirmar que:
  - [ ] Pasan sin errores.
  - [ ] Cubren los escenarios contemplados (mapeos, flujo de análisis, fechas).

### Tarea 8.3: Revisar criterios de éxito
- [ ] Validar que:
  - [ ] El historial funciona según lo esperado.
  - [ ] Las notificaciones se comportan de acuerdo a las ventanas definidas.
  - [ ] Los tests añaden confianza real para seguir evolucionando el proyecto.
