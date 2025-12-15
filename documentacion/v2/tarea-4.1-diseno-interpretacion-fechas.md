# Tarea 4.1: Diseño de la Utilidad de Interpretación de Fechas

## 1. Recopilación de Ejemplos de Formatos de Fecha

Basándonos en el prompt enviado a la IA y en ejemplos reales de pósters de películas y series, la IA puede devolver fechas en los siguientes formatos:

### Formatos Completos (con año)
- `"2025"` - Solo año
- `"15 octubre 2025"` - Día mes año (español)
- `"octubre 2025"` - Mes año (español)
- `"October 15, 2025"` - Mes día, año (inglés)
- `"15 October 2025"` - Día mes año (inglés)
- `"October 2025"` - Mes año (inglés)

### Formatos Parciales (sin año explícito)
- `"15 octubre"` - Día mes (se asume año actual)
- `"octubre 15"` - Mes día (se asume año actual)
- `"October 15"` - Mes día (inglés, se asume año actual)
- `"15 October"` - Día mes (inglés, se asume año actual)

### Formatos con Abreviaturas
- `"15 oct 2025"` - Con abreviatura de mes
- `"Oct 15, 2025"` - Con abreviatura de mes (inglés)

### Casos No Interpretables
- `"Próximamente"` / `"Coming Soon"`
- `"TBA"` / `"TBD"` (To Be Announced / To Be Determined)
- `"2025"` cuando es solo un año muy lejano o ambiguo
- Textos vacíos o `null`
- Textos que no contienen información de fecha reconocible

## 2. Formatos Candidatos a Interpretar

### 2.1. Formatos en Español

#### Con año completo:
1. **`\d{4}`** - Solo año (ej: "2025")
   - Se considera válido si el año es razonable (1900-2100)
   
2. **`\d{1,2}\s+\w+\s+\d{4}`** - Día mes año (ej: "15 octubre 2025")
   - Día: 1-31
   - Mes: nombre completo en español (enero, febrero, marzo, etc.)
   - Año: 4 dígitos
   
3. **`\w+\s+\d{4}`** - Mes año (ej: "octubre 2025")
   - Mes: nombre completo en español
   - Año: 4 dígitos

#### Sin año (se añade año actual):
4. **`\d{1,2}\s+\w+`** - Día mes (ej: "15 octubre")
   - Se añade el año actual al final
   
5. **`\w+\s+\d{1,2}`** - Mes día (ej: "octubre 15")
   - Se reformatea a "15 de octubre [año actual]"

### 2.2. Formatos en Inglés

#### Traducción automática a español:
- Los meses en inglés se traducen automáticamente a español antes de procesar:
  - `january` → `enero`
  - `february` → `febrero`
  - `march` → `marzo`
  - `april` → `abril`
  - `may` → `mayo`
  - `june` → `junio`
  - `july` → `julio`
  - `august` → `agosto`
  - `september` → `septiembre`
  - `october` → `octubre`
  - `november` → `noviembre`
  - `december` → `diciembre`

#### Formatos equivalentes:
- `"October 15, 2025"` → se normaliza a `"15 octubre 2025"`
- `"15 October 2025"` → se normaliza a `"15 octubre 2025"`
- `"October 2025"` → se normaliza a `"octubre 2025"`
- `"October 15"` → se normaliza a `"15 octubre [año actual]"`

## 3. Reglas de Validez

### 3.1. Fecha Válida y Concreta

Una fecha se considera **válida y concreta** cuando:

1. **Tiene año explícito o inferible:**
   - Año presente en el formato (ej: "2025", "15 octubre 2025")
   - O se puede inferir añadiendo el año actual (ej: "15 octubre" → "15 octubre 2025")

2. **El año es razonable:**
   - Entre 1900 y 2100 (años muy antiguos o futuros se consideran inválidos)

3. **El formato coincide con uno de los patrones definidos:**
   - Coincide con alguna expresión regular de los formatos candidatos

4. **Se puede parsear correctamente:**
   - `SimpleDateFormat` puede parsear la fecha sin lanzar excepción
   - El resultado del parseo no es `null`

5. **La fecha es futura o muy reciente (opcional, para notificaciones):**
   - Para el contexto de notificaciones, se puede considerar solo fechas futuras
   - O fechas muy recientes (últimos 30 días) para estrenos ya pasados

### 3.2. Fecha No Interpretable

Una fecha se clasifica como **"no interpretable"** cuando:

1. **Es un texto conocido que indica ausencia de fecha:**
   - `"Próximamente"` / `"Coming Soon"`
   - `"TBA"` / `"TBD"` / `"TBC"` (To Be Confirmed)
   - `"Por anunciar"` / `"A confirmar"`
   - `"Sin fecha"` / `"No date"`

2. **Es `null` o vacío:**
   - `null`
   - `""` (cadena vacía)
   - Solo espacios en blanco

3. **No coincide con ningún patrón reconocido:**
   - No coincide con ninguna expresión regular de los formatos candidatos
   - Contiene texto que no es reconocible como fecha

4. **El parseo falla:**
   - `SimpleDateFormat.parse()` lanza excepción
   - El resultado del parseo es `null`

5. **El año está fuera del rango razonable:**
   - Año < 1900 o > 2100

## 4. Comportamiento de la Función de Interpretación

### 4.1. Entrada
- **Parámetro:** `String?` - Texto de fecha devuelto por la IA
- Puede ser `null` o contener cualquier texto

### 4.2. Salida
- **Retorno:** `Pair<String, Long?>?`
  - `Pair.first`: Texto normalizado de la fecha (en español) o texto original si no es interpretable
  - `Pair.second`: Timestamp (Long) si la fecha es válida y concreta, `null` si no es interpretable
  - `null`: Si la entrada es `null` o vacía

### 4.3. Proceso de Normalización

1. **Limpieza inicial:**
   - `trim()` para eliminar espacios al inicio y final
   - Convertir a minúsculas para comparación

2. **Traducción de meses:**
   - Reemplazar nombres de meses en inglés por sus equivalentes en español
   - Ejemplo: "october" → "octubre"

3. **Detección de patrón:**
   - Probar cada expresión regular en orden de especificidad (más específico primero)
   - Detener en el primer patrón que coincida

4. **Parseo y validación:**
   - Intentar parsear con `SimpleDateFormat` usando el formato correspondiente
   - Validar que el resultado no es `null`
   - Validar que el año está en rango razonable (1900-2100)

5. **Manejo de errores:**
   - Si el parseo falla o el patrón no coincide, retornar `Pair(textoOriginal, null)`
   - Esto indica que la fecha no es interpretable pero se conserva el texto original

## 5. Ejemplos de Uso

### Casos Válidos:
```kotlin
"2025" → Pair("2025", timestamp_2025-01-01)
"15 octubre 2025" → Pair("15 octubre 2025", timestamp_2025-10-15)
"octubre 2025" → Pair("octubre 2025", timestamp_2025-10-01)
"October 15, 2025" → Pair("15 octubre 2025", timestamp_2025-10-15)
"15 octubre" → Pair("15 octubre 2025", timestamp_2025-10-15) // año actual añadido
```

### Casos No Interpretables:
```kotlin
"Próximamente" → Pair("Próximamente", null)
"TBA" → Pair("TBA", null)
null → null
"" → null
"texto sin fecha" → Pair("texto sin fecha", null)
```

## 6. Notas de Implementación

- La función `normalizeDate()` ya implementada en `AnalysisMapper.kt` cubre la mayoría de estos casos
- Se recomienda extraer esta lógica a una clase/utilidad dedicada para mejor mantenibilidad
- Considerar añadir tests unitarios exhaustivos para cada formato y caso límite
- La validación de "fecha futura" puede añadirse en una capa superior si es necesario para notificaciones

