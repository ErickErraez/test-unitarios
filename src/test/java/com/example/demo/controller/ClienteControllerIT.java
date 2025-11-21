package com.example.demo.controller;

import com.example.demo.model.dto.ClienteRequestDTO;
import com.example.demo.model.entity.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de Integración para ClienteController usando MockMvc
 * Testean Controller + Service + Repository trabajando juntos
 */
@SpringBootTest  // Levanta contexto completo de Spring
@AutoConfigureMockMvc  // Configura MockMvc automáticamente
class ClienteControllerIT {
    
    @Autowired
    private MockMvc mockMvc;  // Para simular peticiones HTTP
    
    @Autowired
    private ObjectMapper objectMapper;  // Para convertir objetos a JSON
    
    @Autowired
    private ClienteRepository repository;  // Para preparar/limpiar BD
    
    @BeforeEach
    void setUp() {
        // Limpiar base de datos antes de cada test
        repository.deleteAll();
    }
    
    // ============================================
    // TEST 1: POST CREAR CLIENTE
    // ============================================
    
    @Test
    @DisplayName("POST /api/v1/clientes - Debe crear cliente")
    void debeCrearCliente() throws Exception {
        // ============================================
        // ARRANGE
        // ============================================
        ClienteRequestDTO request = new ClienteRequestDTO(
            "María García",
            "maria@test.com",
            "0988888888"
        );
        
        // ============================================
        // ACT & ASSERT
        // ============================================
        mockMvc.perform(
                post("/api/v1/clientes")  // Método y URL
                    .contentType(MediaType.APPLICATION_JSON)  // Enviar JSON
                    .content(objectMapper.writeValueAsString(request))  // Body
            )
            // Imprimir request/response en consola (útil para debug)
            .andDo(print())
            
            // Verificar status HTTP 201 CREATED
            .andExpect(status().isCreated())
            
            // Verificar que retorna JSON
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            
            // Verificar campos del JSON de respuesta
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nombre").value("María García"))
            .andExpect(jsonPath("$.email").value("maria@test.com"))
            .andExpect(jsonPath("$.telefono").value("0988888888"))
            .andExpect(jsonPath("$.activo").value(true))
            .andExpect(jsonPath("$.fechaRegistro").exists());
    }
    
    // ============================================
    // TEST 2: GET OBTENER CLIENTE POR ID
    // ============================================
    
    @Test
    @DisplayName("GET /api/v1/clientes/{id} - Debe obtener cliente")
    void debeObtenerClientePorId() throws Exception {
        // ============================================
        // ARRANGE - Guardar cliente en BD primero
        // ============================================
        Cliente cliente = new Cliente();
        cliente.setNombre("Pedro López");
        cliente.setEmail("pedro@test.com");
        cliente.setTelefono("0977777777");
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDateTime.now());
        
        Cliente guardado = repository.save(cliente);
        
        // ============================================
        // ACT & ASSERT
        // ============================================
        mockMvc.perform(
                get("/api/v1/clientes/{id}", guardado.getId())  // Path variable
            )
            .andDo(print())
            
            // Verificar status 200 OK
            .andExpect(status().isOk())
            
            // Verificar JSON
            .andExpect(jsonPath("$.id").value(guardado.getId()))
            .andExpect(jsonPath("$.nombre").value("Pedro López"))
            .andExpect(jsonPath("$.email").value("pedro@test.com"))
            .andExpect(jsonPath("$.telefono").value("0977777777"))
            .andExpect(jsonPath("$.activo").value(true));
    }
    
    // ============================================
    // TESTS ADICIONALES
    // ============================================
    
    @Test
    @DisplayName("GET /api/v1/clientes - Debe listar todos los clientes activos")
    void debeListarClientesActivos() throws Exception {
        // ARRANGE - Guardar varios clientes
        Cliente cliente1 = new Cliente();
        cliente1.setNombre("Cliente 1");
        cliente1.setEmail("cliente1@test.com");
        cliente1.setTelefono("0999999999");
        cliente1.setActivo(true);
        cliente1.setFechaRegistro(LocalDateTime.now());
        
        Cliente cliente2 = new Cliente();
        cliente2.setNombre("Cliente 2");
        cliente2.setEmail("cliente2@test.com");
        cliente2.setTelefono("0988888888");
        cliente2.setActivo(true);
        cliente2.setFechaRegistro(LocalDateTime.now());
        
        repository.saveAll(List.of(cliente1, cliente2));
        
        // ACT & ASSERT
        mockMvc.perform(get("/api/v1/clientes"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].nombre").value("Cliente 1"))
            .andExpect(jsonPath("$[1].nombre").value("Cliente 2"));
    }
    
    @Test
    @DisplayName("PUT /api/v1/clientes/{id} - Debe actualizar cliente")
    void debeActualizarCliente() throws Exception {
        // ARRANGE - Guardar cliente primero
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Original");
        cliente.setEmail("original@test.com");
        cliente.setTelefono("0999999999");
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDateTime.now());
        
        Cliente guardado = repository.save(cliente);
        
        // Datos para actualizar
        ClienteRequestDTO request = new ClienteRequestDTO(
            "Cliente Actualizado",
            "actualizado@test.com",
            "0977777777"
        );
        
        // ACT & ASSERT
        mockMvc.perform(
                put("/api/v1/clientes/{id}", guardado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(guardado.getId()))
            .andExpect(jsonPath("$.nombre").value("Cliente Actualizado"))
            .andExpect(jsonPath("$.email").value("actualizado@test.com"))
            .andExpect(jsonPath("$.telefono").value("0977777777"));
    }
    
    @Test
    @DisplayName("DELETE /api/v1/clientes/{id} - Debe eliminar cliente")
    void debeEliminarCliente() throws Exception {
        // ARRANGE - Guardar cliente primero
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente a Eliminar");
        cliente.setEmail("eliminar@test.com");
        cliente.setTelefono("0999999999");
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDateTime.now());
        
        Cliente guardado = repository.save(cliente);
        
        // ACT & ASSERT
        mockMvc.perform(delete("/api/v1/clientes/{id}", guardado.getId()))
            .andDo(print())
            .andExpect(status().isNoContent());
        
        // Verificar que el cliente fue desactivado (no eliminado físicamente)
        Cliente clienteEliminado = repository.findById(guardado.getId()).orElse(null);
        assertNotNull(clienteEliminado);
        assertFalse(clienteEliminado.getActivo());
    }
    
    @Test
    @DisplayName("GET /api/v1/clientes/{id} - Debe retornar 404 si cliente no existe")
    void debeRetornar404CuandoClienteNoExiste() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/api/v1/clientes/{id}", 999L))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Cliente no encontrado con id: 999"));
    }
    
    @Test
    @DisplayName("POST /api/v1/clientes - Debe retornar 400 cuando datos son inválidos")
    void debeRetornar400CuandoDatosInvalidos() throws Exception {
        // ARRANGE - Request con datos inválidos (nombre vacío)
        ClienteRequestDTO request = new ClienteRequestDTO(
            "",  // Nombre vacío
            "email-invalido",  // Email inválido
            "0999999999"
        );
        
        // ACT & ASSERT
        mockMvc.perform(
                post("/api/v1/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("GET /api/v1/clientes/buscar?nombre=X - Debe buscar cliente por nombre")
    void debeBuscarClientePorNombre() throws Exception {
        // ARRANGE - Guardar cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Ana Martínez");
        cliente.setEmail("ana@test.com");
        cliente.setTelefono("0966666666");
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDateTime.now());
        
        repository.save(cliente);
        
        // ACT & ASSERT
        mockMvc.perform(get("/api/v1/clientes/buscar")
                .param("nombre", "Ana Martínez"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Ana Martínez"))
            .andExpect(jsonPath("$.email").value("ana@test.com"));
    }
    
    @Test
    @DisplayName("GET /api/v1/clientes/health - Debe retornar estado del servicio")
    void debeRetornarEstadoDelServicio() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/api/v1/clientes/health"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("Cliente Service is UP"));
    }
    
    @Test
    @DisplayName("POST /api/v1/clientes - Debe retornar 409 cuando cliente ya existe")
    void debeRetornar409CuandoClienteYaExiste() throws Exception {
        // ARRANGE - Guardar cliente primero
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Duplicado");
        cliente.setEmail("juan@test.com");
        cliente.setTelefono("0999999999");
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDateTime.now());
        
        repository.save(cliente);
        
        // Intentar crear otro con el mismo nombre
        ClienteRequestDTO request = new ClienteRequestDTO(
            "Juan Duplicado",
            "otro@test.com",
            "0988888888"
        );
        
        // ACT & ASSERT
        mockMvc.perform(
                post("/api/v1/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value(containsString("Ya existe un cliente con el nombre")));
    }
    
    private void assertNotNull(Cliente cliente) {
        if (cliente == null) {
            throw new AssertionError("Cliente no debe ser nulo");
        }
    }
    
    private void assertFalse(Boolean activo) {
        if (activo == null || activo) {
            throw new AssertionError("El cliente debe estar inactivo");
        }
    }
}
