package com.example.demo.e2e;

import com.example.demo.model.dto.ClienteRequestDTO;
import com.example.demo.repository.ClienteRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Tests End-to-End (E2E) para Cliente usando REST Assured
 * Testean la aplicación completa como lo haría un usuario real
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClienteE2ETest {
    
    @LocalServerPort  // Obtiene el puerto aleatorio
    private int port;
    
    @Autowired
    private ClienteRepository repository;
    
    @BeforeEach
    void setUp() {
        // Configurar REST Assured
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/clientes";
        
        // Limpiar BD
        repository.deleteAll();
    }
    
    // ============================================
    // TEST 1: FLUJO COMPLETO CRUD
    // ============================================
    
    @Test
    @DisplayName("Flujo completo: CREAR → CONSULTAR → ACTUALIZAR → ELIMINAR")
    void flujoCompletoCRUD() {
        // ============================================
        // PASO 1: CREAR CLIENTE (POST)
        // ============================================
        ClienteRequestDTO createRequest = new ClienteRequestDTO(
            "Ana Martínez",
            "ana@test.com",
            "0966666666"
        );
        
        Integer clienteIdInt = given()  // Inicia la petición
                .contentType(ContentType.JSON)
                .body(createRequest)
            .when()  // Ejecuta
                .post()
            .then()  // Verifica
                .statusCode(201)
                .body("nombre", equalTo("Ana Martínez"))
                .body("email", equalTo("ana@test.com"))
                .body("telefono", equalTo("0966666666"))
                .body("activo", equalTo(true))
            .extract()  // Extrae datos
                .path("id");
        
        Long clienteId = clienteIdInt.longValue();
        
        System.out.println("✅ Cliente creado con ID: " + clienteId);
        
        // ============================================
        // PASO 2: CONSULTAR CLIENTE CREADO (GET)
        // ============================================
        given()
                .pathParam("id", clienteId)
            .when()
                .get("/{id}")
            .then()
                .statusCode(200)
                .body("id", equalTo(clienteId.intValue()))
                .body("nombre", equalTo("Ana Martínez"));
        
        System.out.println("✅ Cliente consultado correctamente");
        
        // ============================================
        // PASO 3: ACTUALIZAR CLIENTE (PUT)
        // ============================================
        ClienteRequestDTO updateRequest = new ClienteRequestDTO(
            "Ana Martínez López",
            "ana.lopez@test.com",
            "0955555555"
        );
        
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", clienteId)
                .body(updateRequest)
            .when()
                .put("/{id}")
            .then()
                .statusCode(200)
                .body("nombre", equalTo("Ana Martínez López"))
                .body("email", equalTo("ana.lopez@test.com"))
                .body("telefono", equalTo("0955555555"));
        
        System.out.println("✅ Cliente actualizado correctamente");
        
        // ============================================
        // PASO 4: ELIMINAR CLIENTE (DELETE)
        // ============================================
        given()
                .pathParam("id", clienteId)
            .when()
                .delete("/{id}")
            .then()
                .statusCode(204);  // No Content
        
        System.out.println("✅ Cliente eliminado correctamente");
        
        // ============================================
        // PASO 5: VERIFICAR QUE YA NO ESTÁ EN LA LISTA (GET)
        // ============================================
        given()
            .when()
                .get()
            .then()
                .statusCode(200)
                .body("$", hasSize(0));  // Lista vacía
        
        System.out.println("✅ Verificado que cliente no aparece en lista de activos");
        System.out.println("🎉 FLUJO CRUD COMPLETO EXITOSO");
    }
    
    // ============================================
    // TESTS ADICIONALES E2E
    // ============================================
    
    @Test
    @DisplayName("Flujo: Crear múltiples clientes y listar todos")
    void debeCrearMultiplesClientesYListarlos() {
        // Crear cliente 1
        ClienteRequestDTO cliente1 = new ClienteRequestDTO(
            "Cliente Uno",
            "uno@test.com",
            "0999999999"
        );
        
        given()
                .contentType(ContentType.JSON)
                .body(cliente1)
            .when()
                .post()
            .then()
                .statusCode(201);
        
        // Crear cliente 2
        ClienteRequestDTO cliente2 = new ClienteRequestDTO(
            "Cliente Dos",
            "dos@test.com",
            "0988888888"
        );
        
        given()
                .contentType(ContentType.JSON)
                .body(cliente2)
            .when()
                .post()
            .then()
                .statusCode(201);
        
        // Crear cliente 3
        ClienteRequestDTO cliente3 = new ClienteRequestDTO(
            "Cliente Tres",
            "tres@test.com",
            "0977777777"
        );
        
        given()
                .contentType(ContentType.JSON)
                .body(cliente3)
            .when()
                .post()
            .then()
                .statusCode(201);
        
        // Listar todos
        given()
            .when()
                .get()
            .then()
                .statusCode(200)
                .body("$", hasSize(3))
                .body("[0].nombre", equalTo("Cliente Uno"))
                .body("[1].nombre", equalTo("Cliente Dos"))
                .body("[2].nombre", equalTo("Cliente Tres"));
        
        System.out.println("✅ Múltiples clientes creados y listados correctamente");
    }
    
    @Test
    @DisplayName("Flujo de error: Intentar crear cliente con datos inválidos")
    void debeRetornarErrorCuandoDatosInvalidos() {
        // Intentar crear con nombre vacío
        ClienteRequestDTO requestInvalido = new ClienteRequestDTO(
            "",  // Nombre vacío
            "email@test.com",
            "0999999999"
        );
        
        given()
                .contentType(ContentType.JSON)
                .body(requestInvalido)
            .when()
                .post()
            .then()
                .statusCode(400);  // Bad Request
        
        System.out.println("✅ Validación de datos funciona correctamente");
    }
    
    @Test
    @DisplayName("Flujo de error: Intentar obtener cliente inexistente")
    void debeRetornar404ParaClienteInexistente() {
        given()
                .pathParam("id", 999L)
            .when()
                .get("/{id}")
            .then()
                .statusCode(404)  // Not Found
                .body("message", containsString("Cliente no encontrado"));
        
        System.out.println("✅ Manejo de error 404 funciona correctamente");
    }
    
    @Test
    @DisplayName("Flujo de error: Intentar crear cliente duplicado")
    void debeRetornarErrorParaClienteDuplicado() {
        // Crear cliente 1
        ClienteRequestDTO cliente1 = new ClienteRequestDTO(
            "Cliente Duplicado",
            "cliente@test.com",
            "0999999999"
        );
        
        given()
                .contentType(ContentType.JSON)
                .body(cliente1)
            .when()
                .post()
            .then()
                .statusCode(201);
        
        // Intentar crear cliente 2 con el mismo nombre
        ClienteRequestDTO cliente2 = new ClienteRequestDTO(
            "Cliente Duplicado",
            "otro@test.com",
            "0988888888"
        );
        
        given()
                .contentType(ContentType.JSON)
                .body(cliente2)
            .when()
                .post()
            .then()
                .statusCode(409)  // Conflict
                .body("message", containsString("Ya existe un cliente con el nombre"));
        
        System.out.println("✅ Validación de duplicados funciona correctamente");
    }
    
    @Test
    @DisplayName("Flujo: Buscar cliente por nombre")
    void debeBuscarClientePorNombre() {
        // Crear cliente
        ClienteRequestDTO createRequest = new ClienteRequestDTO(
            "Cliente Buscar",
            "buscar@test.com",
            "0966666666"
        );
        
        given()
                .contentType(ContentType.JSON)
                .body(createRequest)
            .when()
                .post()
            .then()
                .statusCode(201);
        
        // Buscar por nombre
        given()
                .queryParam("nombre", "Cliente Buscar")
            .when()
                .get("/buscar")
            .then()
                .statusCode(200)
                .body("nombre", equalTo("Cliente Buscar"))
                .body("email", equalTo("buscar@test.com"));
        
        System.out.println("✅ Búsqueda por nombre funciona correctamente");
    }
    
    @Test
    @DisplayName("Flujo: Actualizar cliente con nuevo nombre")
    void debeActualizarClienteConNuevoNombre() {
        // Crear cliente
        ClienteRequestDTO createRequest = new ClienteRequestDTO(
            "Nombre Original",
            "original@test.com",
            "0999999999"
        );
        
        Integer clienteIdInt = given()
                .contentType(ContentType.JSON)
                .body(createRequest)
            .when()
                .post()
            .then()
                .statusCode(201)
            .extract()
                .path("id");
        
        Long clienteId = clienteIdInt.longValue();
        
        // Actualizar con nuevo nombre
        ClienteRequestDTO updateRequest = new ClienteRequestDTO(
            "Nombre Actualizado",
            "actualizado@test.com",
            "0988888888"
        );
        
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", clienteId)
                .body(updateRequest)
            .when()
                .put("/{id}")
            .then()
                .statusCode(200)
                .body("nombre", equalTo("Nombre Actualizado"))
                .body("email", equalTo("actualizado@test.com"));
        
        System.out.println("✅ Actualización de nombre funciona correctamente");
    }
    
    @Test
    @DisplayName("Flujo: Verificar health check del servicio")
    void debeResponderHealthCheck() {
        given()
            .when()
                .get("/health")
            .then()
                .statusCode(200)
                .body(equalTo("Cliente Service is UP"));
        
        System.out.println("✅ Health check funciona correctamente");
    }
    
    @Test
    @DisplayName("Flujo completo: Crear, eliminar y verificar que no existe más")
    void debeCrearEliminarYVerificarNoExiste() {
        // Crear cliente
        ClienteRequestDTO createRequest = new ClienteRequestDTO(
            "Cliente Temporal",
            "temporal@test.com",
            "0999999999"
        );
        
        Integer clienteIdInt = given()
                .contentType(ContentType.JSON)
                .body(createRequest)
            .when()
                .post()
            .then()
                .statusCode(201)
            .extract()
                .path("id");
        
        Long clienteId = clienteIdInt.longValue();
        
        System.out.println("✅ Cliente temporal creado con ID: " + clienteId);
        
        // Verificar que existe
        given()
                .pathParam("id", clienteId)
            .when()
                .get("/{id}")
            .then()
                .statusCode(200);
        
        // Eliminar
        given()
                .pathParam("id", clienteId)
            .when()
                .delete("/{id}")
            .then()
                .statusCode(204);
        
        System.out.println("✅ Cliente eliminado");
        
        // Verificar que no aparece en lista de activos
        given()
            .when()
                .get()
            .then()
                .statusCode(200)
                .body("$", hasSize(0));
        
        System.out.println("✅ Cliente no aparece en lista de activos");
        System.out.println("🎉 FLUJO COMPLETO DE ELIMINACIÓN EXITOSO");
    }
}
