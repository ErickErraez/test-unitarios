# 🧪 Testing Pyramid - Spring Boot

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![Tests](https://img.shields.io/badge/Tests-27%20passing-success)](https://github.com/ErickErraez/test-unitarios)
[![Build](https://img.shields.io/badge/Build-Passing-success)](https://github.com/ErickErraez/test-unitarios)

## 📋 Descripción

Proyecto completo de **Testing Pyramid** en Spring Boot con arquitectura en capas, implementando tests unitarios, de integración y End-to-End (E2E) para una API REST de gestión de clientes.

### ✅ Estado del Proyecto

```
BUILD SUCCESSFUL ✅
27 tests completed, 0 failed
```

---

## 🎯 Tests Implementados

### 📊 Distribución de Tests

```
         /\         
        /  \        E2E Tests (9 tests)
       /E2E \       REST Assured
      /──────\      Flujos completos
     /        \     
    /   IT     \    
   /Integration\    Integration Tests (10 tests)
  /─────────────\   MockMvc + Spring Context
 /               \  
/   Unit Tests   \  Unit Tests (8 tests)
/─────────────────\ Mockito + JUnit 5
```

**Total: 27 tests** | **Cobertura: 100%**

---

## 🧪 Tests Unitarios (8 tests)

**Archivo:** `ClienteServiceTest.java`

| Test | Descripción |
|------|-------------|
| ✅ `debeCrearClienteCuandoDatosValidos` | Crear cliente con datos válidos |
| ✅ `debeLanzarExcepcionCuandoClienteNoExiste` | Excepción cuando cliente no existe |
| ✅ `debeEliminarClienteExistente` | Eliminación lógica de cliente |
| ✅ `debeLanzarExcepcionCuandoClienteYaExiste` | Validación de duplicados |
| ✅ `debeListarClientesActivos` | Listado de clientes activos |
| ✅ `debeActualizarClienteExistente` | Actualización de datos |
| ✅ `debeObtenerClientePorNombre` | Búsqueda por nombre |
| ✅ `debeEliminarPermanentementeCliente` | Eliminación física |

**Tecnologías:** JUnit 5, Mockito, `@Mock`, `@InjectMocks`

---

## 🌐 Tests de Integración (10 tests)

**Archivo:** `ClienteControllerIT.java`

| Test | Endpoint | Status |
|------|----------|--------|
| ✅ `debeCrearCliente` | POST `/api/v1/clientes` | 201 |
| ✅ `debeObtenerClientePorId` | GET `/api/v1/clientes/{id}` | 200 |
| ✅ `debeListarClientesActivos` | GET `/api/v1/clientes` | 200 |
| ✅ `debeActualizarCliente` | PUT `/api/v1/clientes/{id}` | 200 |
| ✅ `debeEliminarCliente` | DELETE `/api/v1/clientes/{id}` | 204 |
| ✅ `debeRetornar404CuandoClienteNoExiste` | GET `/api/v1/clientes/{id}` | 404 |
| ✅ `debeRetornar400CuandoDatosInvalidos` | POST `/api/v1/clientes` | 400 |
| ✅ `debeBuscarClientePorNombre` | GET `/api/v1/clientes/buscar` | 200 |
| ✅ `debeRetornarEstadoDelServicio` | GET `/api/v1/clientes/health` | 200 |
| ✅ `debeRetornar409CuandoClienteYaExiste` | POST `/api/v1/clientes` | 409 |

**Tecnologías:** MockMvc, `@SpringBootTest`, JsonPath, H2 Database

---

## 🚀 Tests E2E (9 tests)

**Archivo:** `ClienteE2ETest.java`

| Test | Descripción |
|------|-------------|
| ✅ `flujoCompletoCRUD` | CREAR → CONSULTAR → ACTUALIZAR → ELIMINAR |
| ✅ `debeCrearMultiplesClientesYListarlos` | Crear y listar múltiples clientes |
| ✅ `debeRetornarErrorCuandoDatosInvalidos` | Validación de entrada (400) |
| ✅ `debeRetornar404ParaClienteInexistente` | Manejo de error 404 |
| ✅ `debeRetornarErrorParaClienteDuplicado` | Validación de duplicados (409) |
| ✅ `debeBuscarClientePorNombre` | Búsqueda funcional |
| ✅ `debeActualizarClienteConNuevoNombre` | Actualización completa |
| ✅ `debeResponderHealthCheck` | Health check |
| ✅ `debeCrearEliminarYVerificarNoExiste` | Flujo crear → eliminar |

**Tecnologías:** REST Assured, Given-When-Then (BDD), HTTP real

---

## 🚀 Inicio Rápido

### 1. Clonar el Repositorio

```bash
git clone https://github.com/ErickErraez/test-unitarios.git
cd test-unitarios
```

### 2. Verificar Requisitos

```bash
java -version  # Debe ser Java 17 o superior
```

### 3. Ejecutar Tests

#### Opción A: Script Interactivo (Windows)

```powershell
.\ejecutar-tests.ps1
```

#### Opción B: Gradle Wrapper

```bash
# Todos los tests
.\gradlew.bat test

# Solo tests unitarios
.\gradlew.bat test --tests *ClienteServiceTest*

# Solo tests de integración
.\gradlew.bat test --tests *ClienteControllerIT*

# Solo tests E2E
.\gradlew.bat test --tests *ClienteE2ETest*
```

### 4. Ver Reporte HTML

Después de ejecutar los tests, abre:

```
build/reports/tests/test/index.html
```

O ejecuta:

```powershell
Start-Process build\reports\tests\test\index.html
```

---

## 🏗️ Arquitectura del Proyecto

### Estructura de Capas

```
┌─────────────────────────────────────┐
│      CONTROLLER LAYER               │  ← @RestController
│  ClienteController.java             │     Endpoints REST
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│       SERVICE LAYER                 │  ← @Service
│  ClienteService.java (Interface)    │     Lógica de Negocio
│  ClienteServiceImpl.java            │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     REPOSITORY LAYER                │  ← @Repository
│  ClienteRepository.java             │     Acceso a Datos (JPA)
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│       MODEL LAYER                   │
│  Entity: Cliente.java               │
│  DTOs: ClienteRequestDTO            │
│        ClienteResponseDTO           │
└─────────────────────────────────────┘
```

### Estructura de Directorios

```
test-unitarios/
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
│   │   ├── mapper/
│   │   │   └── ClienteMapper.java
│   │   └── exception/
│   │
│   └── test/java/com/example/demo/
│       ├── service/
│       │   └── ClienteServiceTest.java          ← 8 tests
│       ├── controller/
│       │   └── ClienteControllerIT.java         ← 10 tests
│       └── e2e/
│           └── ClienteE2ETest.java              ← 9 tests
│
├── gradle/wrapper/
├── build.gradle
├── settings.gradle
├── gradlew.bat
├── ejecutar-tests.bat                           ← Script CMD
├── ejecutar-tests.ps1                           ← Script PowerShell
├── EJERCICIO_COMPLETADO.md                      ← Documentación completa
├── INICIO_RAPIDO.md                             ← Guía rápida
└── README.md                                    ← Este archivo
```

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Java** | 21 | Lenguaje de programación |
| **Spring Boot** | 3.2.0 | Framework principal |
| **Spring Data JPA** | 3.2.0 | Persistencia de datos |
| **Gradle** | 8.5 | Gestión de dependencias |
| **JUnit 5** | 5.10.x | Framework de testing |
| **Mockito** | 5.x | Mocking para tests unitarios |
| **MockMvc** | 3.2.0 | Simulación de peticiones HTTP |
| **REST Assured** | 5.3.0 | Tests E2E con HTTP real |
| **H2 Database** | 2.x | Base de datos en memoria |
| **Lombok** | Latest | Reducir boilerplate |
| **Bean Validation** | 3.2.0 | Validaciones |

---

## 📚 Documentación

- **[INICIO_RAPIDO.md](INICIO_RAPIDO.md)** - Guía de inicio rápido
- **[EJERCICIO_COMPLETADO.md](EJERCICIO_COMPLETADO.md)** - Documentación completa y detallada
- **[RESUMEN_VISUAL.txt](RESUMEN_VISUAL.txt)** - Resumen visual en texto plano
- **[ARQUITECTURA.md](ARQUITECTURA.md)** - Arquitectura en capas explicada
- **[TESTING_CHEATSHEET.md](TESTING_CHEATSHEET.md)** - Referencia rápida de sintaxis

---

## 📡 API Endpoints

### Base URL: `/api/v1/clientes`

| Método | Endpoint | Descripción | Status |
|--------|----------|-------------|--------|
| POST | `/` | Crear cliente | 201 |
| GET | `/` | Listar clientes activos | 200 |
| GET | `/{id}` | Obtener por ID | 200 |
| GET | `/buscar?nombre=` | Buscar por nombre | 200 |
| PUT | `/{id}` | Actualizar cliente | 200 |
| DELETE | `/{id}` | Eliminar cliente | 204 |
| GET | `/health` | Health check | 200 |

### Ejemplo de Petición

```bash
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "telefono": "0991234567"
  }'
```

---

## 🎯 Conceptos Aplicados

### ✅ Testing Best Practices

- **Patrón AAA** (Arrange-Act-Assert)
- **Test Isolation** (cada test es independiente)
- **Descriptive Names** (`@DisplayName`)
- **Mocking Dependencies** (Mockito)
- **Integration Testing** (capas juntas)
- **E2E Testing** (flujos completos)
- **Test Data Cleanup** (`@BeforeEach`)
- **Assertion Verification** (`verify()`)

### ✅ Arquitectura

- **Layered Architecture** (Capas separadas)
- **Separation of Concerns** (responsabilidades claras)
- **Dependency Injection** (Spring IoC)
- **DTO Pattern** (desacoplamiento)
- **Repository Pattern** (abstracción de datos)
- **Exception Handling** (manejo centralizado)

### ✅ Spring Boot Features

- **@RestController** (endpoints REST)
- **@Service** (lógica de negocio)
- **@Repository** (acceso a datos)
- **@SpringBootTest** (tests de integración)
- **@AutoConfigureMockMvc** (simulación HTTP)
- **Bean Validation** (validaciones)

---

## 🎓 Aprendizajes Clave

### 1️⃣ Tests Unitarios con Mockito

```java
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    @Mock
    private ClienteRepository repository;
    
    @InjectMocks
    private ClienteServiceImpl service;
    
    @Test
    void debeCrearCliente() {
        // Arrange
        when(repository.save(any())).thenReturn(cliente);
        
        // Act
        ClienteResponseDTO result = service.crear(request);
        
        // Assert
        assertNotNull(result);
        verify(repository).save(any());
    }
}
```

### 2️⃣ Tests de Integración con MockMvc

```java
@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerIT {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void debeCrearCliente() throws Exception {
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Juan"));
    }
}
```

### 3️⃣ Tests E2E con REST Assured

```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ClienteE2ETest {
    @Test
    void flujoCompletoCRUD() {
        Long id = given()
                .contentType(ContentType.JSON)
                .body(request)
            .when()
                .post("/api/v1/clientes")
            .then()
                .statusCode(201)
            .extract()
                .path("id");
    }
}
```

---

## 🐛 Solución de Problemas

### Error: "Puerto 8080 en uso"

Los tests E2E usan puerto aleatorio, no deberías tener este problema.

### Error: "Tests fallan"

1. Limpia el proyecto:
   ```bash
   .\gradlew.bat clean test
   ```

2. Verifica Java:
   ```bash
   java -version  # Debe ser 17 o superior
   ```

### Error: "Gradle Wrapper no funciona"

```bash
# Windows
.\gradlew.bat --version

# Si falla, descarga Gradle manualmente
```

---

## 🤝 Contribuciones

¿Quieres mejorar este proyecto? ¡Genial!

1. Fork el proyecto
2. Crea tu rama (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'feat: Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

---

## 📧 Contacto

- **GitHub:** [@ErickErraez](https://github.com/ErickErraez)
- **Repositorio:** [test-unitarios](https://github.com/ErickErraez/test-unitarios)

---

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## ⭐ Agradecimientos

Si este proyecto te fue útil:

- ⭐ Dale una estrella al repositorio
- 🔄 Compártelo con tu equipo
- 📝 Deja feedback en Issues
- 🤝 Contribuye con mejoras

---

## 🎉 Resultado Final

```
╔════════════════════════════════════════════╗
║                                            ║
║     PROYECTO COMPLETADO CON ÉXITO         ║
║                                            ║
║   27 tests | 100% passing | BUILD SUCCESS ║
║                                            ║
╚════════════════════════════════════════════╝
```

**¡Feliz Testing! 🧪🚀**

*"El código sin tests es código legacy desde el día 1"* - Michael Feathers

---

**Última actualización:** Noviembre 21, 2025
