package com.example.demo.service;

import com.example.demo.exception.ClienteAlreadyExistsException;
import com.example.demo.exception.ClienteNotFoundException;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.model.dto.ClienteRequestDTO;
import com.example.demo.model.dto.ClienteResponseDTO;
import com.example.demo.model.entity.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests Unitarios para ClienteService usando Mockito
 * Testean la lógica de negocio de forma aislada
 */
@ExtendWith(MockitoExtension.class)  // Habilita Mockito
class ClienteServiceTest {
    
    @Mock  // Dependencia FALSA
    private ClienteRepository repository;
    
    @Mock  // Dependencia FALSA
    private ClienteMapper mapper;
    
    @InjectMocks  // Inyecta los mocks en el service
    private ClienteServiceImpl service;
    
    // ============================================
    // TEST 1: CREAR CLIENTE
    // ============================================
    
    @Test
    @DisplayName("Debe crear cliente cuando datos son válidos")
    void debeCrearClienteCuandoDatosValidos() {
        // ============================================
        // ARRANGE (Preparar) - Configurar el escenario
        // ============================================
        
        // 1. Crear datos de entrada
        ClienteRequestDTO request = new ClienteRequestDTO(
            "Juan Pérez",
            "juan@test.com",
            "0999999999"
        );
        
        // 2. Crear entidad que "retornará" el mapper
        Cliente clienteEntity = new Cliente();
        clienteEntity.setNombre("Juan Pérez");
        clienteEntity.setEmail("juan@test.com");
        clienteEntity.setTelefono("0999999999");
        
        // 3. Crear entidad guardada (con ID)
        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId(1L);
        clienteGuardado.setNombre("Juan Pérez");
        clienteGuardado.setEmail("juan@test.com");
        clienteGuardado.setTelefono("0999999999");
        clienteGuardado.setActivo(true);
        clienteGuardado.setFechaRegistro(LocalDateTime.now());
        
        // 4. Crear DTO de respuesta esperada
        ClienteResponseDTO expectedResponse = new ClienteResponseDTO(
            1L, 
            "Juan Pérez", 
            "juan@test.com", 
            "0999999999",
            LocalDateTime.now(),
            true
        );
        
        // 5. CONFIGURAR COMPORTAMIENTO DE LOS MOCKS
        // "Cuando llamen a repository.existsByNombre(), retorna false (no existe)"
        when(repository.existsByNombre(request.getNombre())).thenReturn(false);
        
        // "Cuando llamen a mapper.toEntity() con request, retorna clienteEntity"
        when(mapper.toEntity(request)).thenReturn(clienteEntity);
        
        // "Cuando llamen a repository.save() con cualquier Cliente, retorna clienteGuardado"
        when(repository.save(any(Cliente.class))).thenReturn(clienteGuardado);
        
        // "Cuando llamen a mapper.toResponseDTO() con clienteGuardado, retorna expectedResponse"
        when(mapper.toResponseDTO(clienteGuardado)).thenReturn(expectedResponse);
        
        // ============================================
        // ACT (Ejecutar) - Llamar al método a testear
        // ============================================
        ClienteResponseDTO result = service.crear(request);
        
        // ============================================
        // ASSERT (Verificar) - Comprobar resultados
        // ============================================
        
        // Verificar que el resultado no es nulo
        assertNotNull(result);
        
        // Verificar campos del resultado
        assertEquals(1L, result.getId());
        assertEquals("Juan Pérez", result.getNombre());
        assertEquals("juan@test.com", result.getEmail());
        assertEquals("0999999999", result.getTelefono());
        assertTrue(result.getActivo());
        
        // Verificar que se llamaron los métodos correctos
        verify(repository).existsByNombre(request.getNombre());
        verify(mapper).toEntity(request);
        verify(repository).save(any(Cliente.class));
        verify(mapper).toResponseDTO(clienteGuardado);
    }
    
    // ============================================
    // TEST 2: LANZAR EXCEPCIÓN CUANDO CLIENTE NO EXISTE
    // ============================================
    
    @Test
    @DisplayName("Debe lanzar excepción cuando cliente no existe")
    void debeLanzarExcepcionCuandoClienteNoExiste() {
        // ARRANGE
        Long clienteId = 99L;
        
        // Configurar mock: repository NO encuentra el cliente
        when(repository.findById(clienteId)).thenReturn(Optional.empty());
        
        // ACT & ASSERT
        // Verificar que se lanza la excepción esperada
        ClienteNotFoundException exception = assertThrows(
            ClienteNotFoundException.class,
            () -> service.obtenerPorId(clienteId)
        );
        
        // Verificar mensaje de la excepción
        assertEquals("Cliente no encontrado con id: 99", exception.getMessage());
        
        // Verificar que se llamó findById
        verify(repository).findById(clienteId);
        
        // Verificar que NO se llamó save (porque no existe)
        verify(repository, never()).save(any());
    }
    
    // ============================================
    // TEST 3: ELIMINAR CLIENTE EXISTENTE
    // ============================================
    
    @Test
    @DisplayName("Debe eliminar cliente existente")
    void debeEliminarClienteExistente() {
        // ARRANGE
        Long clienteId = 1L;
        
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNombre("Cliente a Eliminar");
        cliente.setActivo(true);
        
        // Mock: repository encuentra el cliente
        when(repository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(repository.save(any(Cliente.class))).thenReturn(cliente);
        
        // ACT
        service.eliminar(clienteId);
        
        // ASSERT
        // Verificar que se buscó el cliente
        verify(repository).findById(clienteId);
        
        // Verificar que se guardó (desactivó)
        verify(repository).save(argThat(c -> 
            c.getId().equals(clienteId) && !c.getActivo()
        ));
    }
    
    // ============================================
    // TESTS ADICIONALES
    // ============================================
    
    @Test
    @DisplayName("Debe lanzar excepción cuando cliente ya existe")
    void debeLanzarExcepcionCuandoClienteYaExiste() {
        // ARRANGE
        ClienteRequestDTO request = new ClienteRequestDTO(
            "Juan Pérez",
            "juan@test.com",
            "0999999999"
        );
        
        // Configurar mock: el nombre ya existe
        when(repository.existsByNombre(request.getNombre())).thenReturn(true);
        
        // ACT & ASSERT
        ClienteAlreadyExistsException exception = assertThrows(
            ClienteAlreadyExistsException.class,
            () -> service.crear(request)
        );
        
        // Verificar mensaje
        assertTrue(exception.getMessage().contains("Ya existe un cliente con el nombre"));
        
        // Verificar que NO se intentó guardar
        verify(repository, never()).save(any());
    }
    
    @Test
    @DisplayName("Debe listar todos los clientes activos")
    void debeListarClientesActivos() {
        // ARRANGE
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNombre("Cliente 1");
        cliente1.setActivo(true);
        
        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNombre("Cliente 2");
        cliente2.setActivo(true);
        
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        
        ClienteResponseDTO dto1 = new ClienteResponseDTO(
            1L, "Cliente 1", "email1@test.com", "0999999999", LocalDateTime.now(), true
        );
        ClienteResponseDTO dto2 = new ClienteResponseDTO(
            2L, "Cliente 2", "email2@test.com", "0988888888", LocalDateTime.now(), true
        );
        List<ClienteResponseDTO> dtos = Arrays.asList(dto1, dto2);
        
        // Configurar mocks
        when(repository.findByActivo(true)).thenReturn(clientes);
        when(mapper.toResponseDTOList(clientes)).thenReturn(dtos);
        
        // ACT
        List<ClienteResponseDTO> result = service.listar();
        
        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByActivo(true);
        verify(mapper).toResponseDTOList(clientes);
    }
    
    @Test
    @DisplayName("Debe actualizar cliente existente")
    void debeActualizarClienteExistente() {
        // ARRANGE
        Long clienteId = 1L;
        
        ClienteRequestDTO request = new ClienteRequestDTO(
            "Juan Pérez Actualizado",
            "juan.nuevo@test.com",
            "0977777777"
        );
        
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(clienteId);
        clienteExistente.setNombre("Juan Pérez");
        clienteExistente.setEmail("juan@test.com");
        clienteExistente.setTelefono("0999999999");
        
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(clienteId);
        clienteActualizado.setNombre(request.getNombre());
        clienteActualizado.setEmail(request.getEmail());
        clienteActualizado.setTelefono(request.getTelefono());
        
        ClienteResponseDTO expectedResponse = new ClienteResponseDTO(
            clienteId,
            request.getNombre(),
            request.getEmail(),
            request.getTelefono(),
            LocalDateTime.now(),
            true
        );
        
        // Configurar mocks
        when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));
        when(repository.existsByNombre(request.getNombre())).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenReturn(clienteActualizado);
        when(mapper.toResponseDTO(clienteActualizado)).thenReturn(expectedResponse);
        
        // ACT
        ClienteResponseDTO result = service.actualizar(clienteId, request);
        
        // ASSERT
        assertNotNull(result);
        assertEquals(request.getNombre(), result.getNombre());
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(request.getTelefono(), result.getTelefono());
        
        verify(repository).findById(clienteId);
        verify(repository).save(any(Cliente.class));
        verify(mapper).toResponseDTO(clienteActualizado);
    }
    
    @Test
    @DisplayName("Debe obtener cliente por nombre")
    void debeObtenerClientePorNombre() {
        // ARRANGE
        String nombre = "Juan Pérez";
        
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre(nombre);
        cliente.setEmail("juan@test.com");
        
        ClienteResponseDTO expectedResponse = new ClienteResponseDTO(
            1L,
            nombre,
            "juan@test.com",
            "0999999999",
            LocalDateTime.now(),
            true
        );
        
        // Configurar mocks
        when(repository.findByNombre(nombre)).thenReturn(Optional.of(cliente));
        when(mapper.toResponseDTO(cliente)).thenReturn(expectedResponse);
        
        // ACT
        ClienteResponseDTO result = service.obtenerPorNombre(nombre);
        
        // ASSERT
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());
        verify(repository).findByNombre(nombre);
        verify(mapper).toResponseDTO(cliente);
    }
    
    @Test
    @DisplayName("Debe eliminar permanentemente un cliente")
    void debeEliminarPermanentementeCliente() {
        // ARRANGE
        Long clienteId = 1L;
        
        // Configurar mock: el cliente existe
        when(repository.existsById(clienteId)).thenReturn(true);
        doNothing().when(repository).deleteById(clienteId);
        
        // ACT
        service.eliminarPermanente(clienteId);
        
        // ASSERT
        verify(repository).existsById(clienteId);
        verify(repository).deleteById(clienteId);
    }
}
