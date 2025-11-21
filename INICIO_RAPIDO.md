# 🚀 Inicio Rápido - Testing Pyramid Spring Boot

## ✅ Estado del Proyecto

**TODOS LOS TESTS PASAN** ✅

```
BUILD SUCCESSFUL
27 tests completed, 0 failed
```

---

## 📋 Archivos de Tests Creados

### 1️⃣ Tests Unitarios (8 tests)
**Archivo:** `src/test/java/com/example/demo/service/ClienteServiceTest.java`

**Tecnología:** JUnit 5 + Mockito

**Tests implementados:**
- ✅ Crear cliente con datos válidos
- ✅ Lanzar excepción cuando cliente no existe
- ✅ Eliminar cliente existente
- ✅ Lanzar excepción cuando cliente ya existe
- ✅ Listar clientes activos
- ✅ Actualizar cliente existente
- ✅ Obtener cliente por nombre
- ✅ Eliminar permanentemente cliente

---

### 2️⃣ Tests de Integración (10 tests)
**Archivo:** `src/test/java/com/example/demo/controller/ClienteControllerIT.java`

**Tecnología:** JUnit 5 + MockMvc + Spring Boot Test

**Tests implementados:**
- ✅ POST - Crear cliente
- ✅ GET - Obtener cliente por ID
- ✅ GET - Listar todos los clientes activos
- ✅ PUT - Actualizar cliente
- ✅ DELETE - Eliminar cliente
- ✅ GET - Retornar 404 si cliente no existe
- ✅ POST - Retornar 400 cuando datos inválidos
- ✅ GET - Buscar cliente por nombre
- ✅ GET - Health check del servicio
- ✅ POST - Retornar 409 cuando cliente ya existe

---

### 3️⃣ Tests E2E (9 tests)
**Archivo:** `src/test/java/com/example/demo/e2e/ClienteE2ETest.java`

**Tecnología:** JUnit 5 + REST Assured

**Tests implementados:**
- ✅ Flujo completo CRUD (crear → consultar → actualizar → eliminar)
- ✅ Crear múltiples clientes y listarlos
- ✅ Retornar error cuando datos inválidos
- ✅ Retornar 404 para cliente inexistente
- ✅ Retornar error para cliente duplicado
- ✅ Buscar cliente por nombre
- ✅ Actualizar cliente con nuevo nombre
- ✅ Health check funcional
- ✅ Crear, eliminar y verificar que no existe

---

## 🚀 Comandos para Ejecutar Tests

### Opción 1: Usando Scripts Interactivos

#### Windows CMD:
```cmd
ejecutar-tests.bat
```

#### PowerShell:
```powershell
.\ejecutar-tests.ps1
```

**Menú interactivo con opciones:**
1. Ejecutar TODOS los tests
2. Ejecutar solo tests UNITARIOS
3. Ejecutar solo tests de INTEGRACIÓN
4. Ejecutar solo tests E2E
5. Ejecutar tests con reporte detallado
6. Limpiar y ejecutar todos los tests
7. Ver reporte HTML en navegador
8. Mostrar resumen de tests

---

### Opción 2: Comandos Directos

#### Ejecutar TODOS los tests:
```bash
.\gradlew.bat test
```

#### Ejecutar solo tests unitarios:
```bash
.\gradlew.bat test --tests *ClienteServiceTest*
```

#### Ejecutar solo tests de integración:
```bash
.\gradlew.bat test --tests *ClienteControllerIT*
```

#### Ejecutar solo tests E2E:
```bash
.\gradlew.bat test --tests *ClienteE2ETest*
```

#### Limpiar y ejecutar tests:
```bash
.\gradlew.bat clean test
```

#### Ejecutar tests con reporte detallado:
```bash
.\gradlew.bat test --console=plain --info
```

---

## 📊 Ver Reportes

### Reporte HTML (Recomendado)
Después de ejecutar los tests, abre en tu navegador:
```
build/reports/tests/test/index.html
```

O ejecuta:
```bash
Start-Process build\reports\tests\test\index.html
```

### Reporte en Consola
Los resultados se muestran automáticamente en la consola después de ejecutar los tests.

---

## 🏗️ Estructura del Proyecto

```
Ejercicio_Dos/
│
├── src/
│   ├── main/java/com/example/demo/
│   │   ├── controller/
│   │   │   └── ClienteController.java
│   │   ├── service/
│   │   │   ├── ClienteService.java
│   │   │   └── impl/ClienteServiceImpl.java
│   │   ├── repository/
│   │   │   └── ClienteRepository.java
│   │   ├── model/
│   │   │   ├── entity/Cliente.java
│   │   │   └── dto/
│   │   └── ...
│   │
│   └── test/java/com/example/demo/
│       ├── service/
│       │   └── ClienteServiceTest.java          ← 8 tests unitarios
│       ├── controller/
│       │   └── ClienteControllerIT.java         ← 10 tests integración
│       └── e2e/
│           └── ClienteE2ETest.java              ← 9 tests E2E
│
├── build.gradle                                  ← Configuración Gradle
├── gradlew.bat                                   ← Gradle Wrapper
├── ejecutar-tests.bat                            ← Script CMD
├── ejecutar-tests.ps1                            ← Script PowerShell
├── EJERCICIO_COMPLETADO.md                       ← Documentación completa
├── INICIO_RAPIDO.md                              ← Este archivo
└── README.md                                     ← Documentación del proyecto
```

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Propósito |
|-----------|-----------|
| **Java 21** | Lenguaje de programación |
| **Spring Boot 3.2.0** | Framework principal |
| **Gradle 8.5** | Gestión de dependencias |
| **JUnit 5** | Framework de testing |
| **Mockito** | Mocking para tests unitarios |
| **MockMvc** | Simulación de peticiones HTTP |
| **REST Assured** | Tests E2E con HTTP real |
| **H2 Database** | Base de datos en memoria |

---

## 📈 Pirámide de Testing

```
         /\         
        /  \        E2E (9 tests)
       /E2E \       REST Assured
      /──────\      
     /        \     
    /   IT     \    Integration (10 tests)
   /Integration\    MockMvc
  /─────────────\   
 /               \  
/   Unit Tests   \  Unit (8 tests)
/─────────────────\ Mockito
```

**Total:** 27 tests ✅

---

## ✅ Checklist de Verificación

Antes de comenzar, verifica que tienes:

- [x] Java 21 instalado
  ```bash
  java -version
  ```

- [x] Gradle Wrapper configurado
  ```bash
  .\gradlew.bat --version
  ```

- [x] Tests creados
  - [x] ClienteServiceTest.java
  - [x] ClienteControllerIT.java
  - [x] ClienteE2ETest.java

- [x] Todos los tests pasan
  ```bash
  .\gradlew.bat test
  ```

---

## 🎯 Ejemplos de Uso

### 1. Primera vez - Ejecutar todos los tests
```bash
# Opción A: Script interactivo
.\ejecutar-tests.ps1

# Opción B: Comando directo
.\gradlew.bat test
```

### 2. Desarrollo - Solo tests unitarios (más rápido)
```bash
.\gradlew.bat test --tests *ClienteServiceTest*
```

### 3. Antes de commit - Todos los tests
```bash
.\gradlew.bat clean test
```

### 4. Ver resultados
```bash
Start-Process build\reports\tests\test\index.html
```

---

## 🐛 Solución de Problemas

### Error: "gradlew no se reconoce"
**Solución:** Usa `.\gradlew.bat` en lugar de `gradlew`

### Error: "Puerto 8080 en uso"
**Solución:** Los tests usan puerto aleatorio, no deberías tener este problema

### Error: "Tests fallan"
**Solución 1:** Limpia el proyecto
```bash
.\gradlew.bat clean test
```

**Solución 2:** Verifica que Java 21 esté instalado
```bash
java -version
```

### Error: "Permiso denegado en PowerShell"
**Solución:** Ejecuta este comando primero:
```powershell
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
```

---

## 📚 Documentación Completa

Para más detalles, consulta:

- **EJERCICIO_COMPLETADO.md** - Documentación detallada de todos los tests
- **README.md** - Documentación principal del proyecto
- **ARQUITECTURA.md** - Arquitectura en capas del proyecto
- **TESTING_CHEATSHEET.md** - Referencia rápida de sintaxis

---

## 🎓 Conceptos Aprendidos

### Tests Unitarios
- ✅ Uso de `@Mock` y `@InjectMocks`
- ✅ Configurar comportamiento con `when().thenReturn()`
- ✅ Verificar llamadas con `verify()`
- ✅ Testing de excepciones

### Tests de Integración
- ✅ Uso de `@SpringBootTest` y `@AutoConfigureMockMvc`
- ✅ Simulación de HTTP con `MockMvc`
- ✅ Verificación de JSON con `jsonPath()`
- ✅ Testing con base de datos en memoria

### Tests E2E
- ✅ Uso de REST Assured
- ✅ Sintaxis Given-When-Then
- ✅ Peticiones HTTP reales
- ✅ Testing de flujos completos

---

## 🎉 ¡Listo para Comenzar!

Ejecuta tu primer test:

```bash
.\ejecutar-tests.ps1
```

O directamente:

```bash
.\gradlew.bat test
```

**¡Éxito! 🚀**

---

*"El código sin tests es código legacy desde el día 1"* - Michael Feathers
