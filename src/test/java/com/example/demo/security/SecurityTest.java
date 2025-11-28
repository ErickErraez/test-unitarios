package com.example.demo.security;

import com.example.demo.model.dto.ClienteRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de SEGURIDAD (Security Testing)
 * 
 * Objetivo: Detectar vulnerabilidades y validar datos de entrada
 * Categorías: Injection, XSS, Validación de datos
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        System.out.println("\n🔒 INICIANDO PRUEBA DE SEGURIDAD");
    }

    /**
     * TEST 1: Verificar protección contra SQL Injection
     * 
     * Ataque: Intentar inyectar código SQL en el nombre
     * Resultado esperado: Sistema RECHAZA (400 Bad Request)
     * Protección: @Pattern bloquea caracteres especiales + JPA usa prepared statements
     */
    @Test
    void testSQLInjectionPrevention() throws Exception {
        System.out.println("💉 Test: SQL Injection Protection");
        
        // 1. Crear payload malicioso con SQL injection
        ClienteRequestDTO maliciousClient = new ClienteRequestDTO();
        maliciousClient.setNombre("'; DROP TABLE clientes; --");
        maliciousClient.setEmail("hacker@test.com");
        
        // 2. Intentar crear cliente - DEBE SER RECHAZADO
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(maliciousClient)))
                .andExpect(status().isBadRequest()); // Rechazado por caracteres especiales
        
        System.out.println("✅ SQL Injection prevención: BLOQUEADO por validación @Pattern");
    }

    /**
     * TEST 2: Verificar protección contra XSS (Cross-Site Scripting)
     * 
     * Ataque: Intentar inyectar JavaScript en el nombre
     * Resultado esperado: Sistema RECHAZA (400 Bad Request)
     * Protección: @Pattern bloquea caracteres HTML/JS (< > { } [ ])
     */
    @Test
    void testXSSPrevention() throws Exception {
        System.out.println("🛡 Test: XSS Protection");
        
        // 1. Crear payload con script malicioso
        ClienteRequestDTO xssClient = new ClienteRequestDTO();
        xssClient.setNombre("<script>alert('XSS')</script>");
        xssClient.setEmail("xss@test.com");
        
        // 2. Intentar crear cliente - DEBE SER RECHAZADO
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(xssClient)))
                .andExpect(status().isBadRequest()); // Rechazado por caracteres especiales
        
        System.out.println("✅ XSS Prevention: BLOQUEADO por validación @Pattern");
    }

    /**
     * TEST 3: Validar formato de email
     * 
     * Validación: Email debe contener @ y dominio válido
     * Anotación: @Email en ClienteRequestDTO
     */
    @Test
    void testEmailValidation() throws Exception {
        System.out.println("📧 Test: Email Validation");
        
        // 1. Crear DTO con email inválido (sin @)
        ClienteRequestDTO invalidEmail = new ClienteRequestDTO();
        invalidEmail.setNombre("Test User");
        invalidEmail.setEmail("invalid-email-format");
        
        // 2. Debe rechazar (400 Bad Request)
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmail)))
                .andExpect(status().isBadRequest()); // DEBE rechazar email inválido
        
        System.out.println("✅ Email validation: Sistema valida formato correctamente");
    }

    /**
     * TEST 4: Rechazar campos vacíos
     * 
     * Validación: Campos obligatorios no pueden estar vacíos
     * Anotación: @NotBlank en ClienteRequestDTO
     */
    @Test
    void testEmptyFieldsValidation() throws Exception {
        System.out.println("🚫 Test: Empty Fields Validation");
        
        // 1. Crear DTO con campos vacíos
        ClienteRequestDTO emptyClient = new ClienteRequestDTO();
        emptyClient.setNombre("");
        emptyClient.setEmail("valid@test.com");
        
        // 2. Debe rechazar
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyClient)))
                .andExpect(status().isBadRequest()); // DEBE rechazar campos vacíos
        
        System.out.println("✅ Empty fields validation: Sistema rechaza campos vacíos correctamente");
    }

    /**
     * TEST 5: Rechazar campos nulos
     * 
     * Validación: Campos obligatorios no pueden ser null
     * Anotación: @NotBlank cubre null + vacío
     */
    @Test
    void testNullFieldsValidation() throws Exception {
        System.out.println("❌ Test: Null Fields Validation");
        
        // 1. Crear DTO con campos null (valores por defecto)
        ClienteRequestDTO nullClient = new ClienteRequestDTO();
        // nombre y email son null por defecto
        
        // 2. Debe rechazar
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullClient)))
                .andExpect(status().isBadRequest()); // DEBE rechazar campos null
        
        System.out.println("✅ Null validation: Sistema rechaza campos null correctamente");
    }

    /**
     * TEST 6: Validar longitud máxima de campos
     * 
     * Concepto: Prevenir buffer overflow y DoS
     * Validación: Nombre debe tener máximo 100 caracteres
     * Anotación: @Size(max=100)
     */
    @Test
    void testFieldLengthValidation() throws Exception {
        System.out.println("📏 Test: Field Length Validation");
        
        // 1. Crear DTO con nombre muy largo (1000 caracteres)
        ClienteRequestDTO longNameClient = new ClienteRequestDTO();
        longNameClient.setNombre("A".repeat(1000)); // Supera el límite de 100
        longNameClient.setEmail("test@test.com");
        
        // 2. Debe rechazar
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(longNameClient)))
                .andExpect(status().isBadRequest()); // DEBE rechazar longitud excesiva
        
        System.out.println("✅ Field length validation: Longitud excesiva rechazada correctamente");
    }

    /**
     * TEST 7: Validar caracteres especiales en nombre
     * 
     * Concepto: Algunos caracteres pueden ser usados para ataques XSS/Injection
     * Validación: Solo permitir letras y espacios
     * Anotación: @Pattern con regex
     */
    @Test
    void testSpecialCharactersValidation() throws Exception {
        System.out.println("🔣 Test: Special Characters Validation");
        
        // 1. Crear DTO con caracteres especiales peligrosos
        ClienteRequestDTO specialCharsClient = new ClienteRequestDTO();
        specialCharsClient.setNombre("Test<script>alert('XSS')</script>");
        specialCharsClient.setEmail("test@test.com");
        
        // 2. Debe rechazar
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialCharsClient)))
                .andExpect(status().isBadRequest()); // DEBE rechazar caracteres especiales
        
        System.out.println("✅ Special chars validation: Caracteres peligrosos rechazados correctamente");
    }
}
