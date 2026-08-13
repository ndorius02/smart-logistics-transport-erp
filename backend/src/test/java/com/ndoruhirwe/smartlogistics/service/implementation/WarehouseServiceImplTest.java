package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.WarehouseCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.WarehouseUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.WarehouseResponse;
import com.ndoruhirwe.smartlogistics.entity.Warehouse;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.WarehouseMapper;
import com.ndoruhirwe.smartlogistics.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.DUPLICATE_WAREHOUSE_CODE;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.WAREHOUSE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseMapper warehouseMapper;

    private WarehouseServiceImpl warehouseService;

    private UUID warehouseId;
    private Warehouse warehouse;
    private WarehouseResponse response;

    @BeforeEach
    void setUp() {

        warehouseService = new WarehouseServiceImpl(
                warehouseRepository,
                warehouseMapper
        );

        warehouseId = UUID.randomUUID();

        warehouse = Warehouse.builder()
                .id(warehouseId)
                .code("WH-BRU-001")
                .name("Brussels Central Warehouse")
                .address("120 Avenue du Port")
                .city("Brussels")
                .country("Belgium")
                .capacity(5000)
                .active(true)
                .build();

        response = new WarehouseResponse(
                warehouseId,
                "WH-BRU-001",
                "Brussels Central Warehouse",
                "120 Avenue du Port",
                "Brussels",
                "Belgium",
                5000,
                true,
                null,
                null
        );
    }

    @Test
    void createWarehouse_shouldSaveAndReturnWarehouse_whenCodeIsUnique() {

        WarehouseCreateRequest request =
                new WarehouseCreateRequest(
                        " wh-bru-001 ",
                        "Brussels Central Warehouse",
                        "120 Avenue du Port",
                        "Brussels",
                        "Belgium",
                        5000
                );

        when(warehouseRepository
                .findByCodeIgnoreCase("WH-BRU-001"))
                .thenReturn(Optional.empty());

        when(warehouseMapper.toEntity(request))
                .thenReturn(warehouse);

        when(warehouseRepository.save(warehouse))
                .thenReturn(warehouse);

        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(response);

        WarehouseResponse result =
                warehouseService.createWarehouse(request);

        assertNotNull(result);
        assertEquals(warehouseId, result.id());
        assertEquals("WH-BRU-001", result.code());
        assertTrue(result.active());

        verify(warehouseRepository)
                .findByCodeIgnoreCase("WH-BRU-001");

        verify(warehouseRepository).save(warehouse);
        verify(warehouseMapper).toResponse(warehouse);
    }

    @Test
    void createWarehouse_shouldThrowDuplicateResourceException_whenCodeAlreadyExists() {

        WarehouseCreateRequest request =
                new WarehouseCreateRequest(
                        "WH-BRU-001",
                        "Brussels Central Warehouse",
                        "120 Avenue du Port",
                        "Brussels",
                        "Belgium",
                        5000
                );

        when(warehouseRepository
                .findByCodeIgnoreCase("WH-BRU-001"))
                .thenReturn(Optional.of(warehouse));

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> warehouseService.createWarehouse(request)
                );

        assertEquals(
                DUPLICATE_WAREHOUSE_CODE,
                exception.getMessage()
        );

        verify(warehouseRepository, never()).save(any());
        verify(warehouseMapper, never()).toEntity(any());
    }

    @Test
    void getWarehouseById_shouldReturnWarehouse_whenWarehouseExists() {

        when(warehouseRepository.findById(warehouseId))
                .thenReturn(Optional.of(warehouse));

        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(response);

        WarehouseResponse result =
                warehouseService.getWarehouseById(warehouseId);

        assertNotNull(result);
        assertEquals(warehouseId, result.id());
        assertEquals("WH-BRU-001", result.code());

        verify(warehouseRepository).findById(warehouseId);
        verify(warehouseMapper).toResponse(warehouse);
    }

    @Test
    void getWarehouseById_shouldThrowResourceNotFoundException_whenWarehouseDoesNotExist() {

        when(warehouseRepository.findById(warehouseId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> warehouseService.getWarehouseById(warehouseId)
                );

        assertEquals(
                WAREHOUSE_NOT_FOUND,
                exception.getMessage()
        );

        verify(warehouseMapper, never()).toResponse(any());
    }

    @Test
    void updateWarehouse_shouldUpdateAndReturnWarehouse_whenDataIsValid() {

        WarehouseUpdateRequest request =
                new WarehouseUpdateRequest(
                        " wh-ant-001 ",
                        "Antwerp Distribution Center",
                        "78 Havenlaan",
                        "Antwerp",
                        "Belgium",
                        7000,
                        true
                );

        WarehouseResponse updatedResponse =
                new WarehouseResponse(
                        warehouseId,
                        "WH-ANT-001",
                        "Antwerp Distribution Center",
                        "78 Havenlaan",
                        "Antwerp",
                        "Belgium",
                        7000,
                        true,
                        null,
                        null
                );

        when(warehouseRepository.findById(warehouseId))
                .thenReturn(Optional.of(warehouse));

        when(warehouseRepository
                .findByCodeIgnoreCase("WH-ANT-001"))
                .thenReturn(Optional.empty());

        doAnswer(invocation -> {
            Warehouse target = invocation.getArgument(1);

            target.setName("Antwerp Distribution Center");
            target.setAddress("78 Havenlaan");
            target.setCity("Antwerp");
            target.setCountry("Belgium");
            target.setCapacity(7000);
            target.setActive(true);

            return null;
        }).when(warehouseMapper)
                .updateEntity(request, warehouse);

        when(warehouseRepository.save(warehouse))
                .thenReturn(warehouse);

        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(updatedResponse);

        WarehouseResponse result =
                warehouseService.updateWarehouse(
                        warehouseId,
                        request
                );

        assertEquals(
                "WH-ANT-001",
                warehouse.getCode()
        );

        assertEquals(
                "Antwerp Distribution Center",
                result.name()
        );

        verify(warehouseMapper)
                .updateEntity(request, warehouse);

        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void deactivateWarehouse_shouldDeactivateWarehouse_whenWarehouseIsActive() {

        when(warehouseRepository.findById(warehouseId))
                .thenReturn(Optional.of(warehouse));

        when(warehouseRepository.save(warehouse))
                .thenReturn(warehouse);

        WarehouseResponse inactiveResponse =
                new WarehouseResponse(
                        warehouseId,
                        warehouse.getCode(),
                        warehouse.getName(),
                        warehouse.getAddress(),
                        warehouse.getCity(),
                        warehouse.getCountry(),
                        warehouse.getCapacity(),
                        false,
                        null,
                        null
                );

        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(inactiveResponse);

        WarehouseResponse result =
                warehouseService.deactivateWarehouse(
                        warehouseId
                );

        assertFalse(warehouse.isActive());
        assertFalse(result.active());

        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void activateWarehouse_shouldActivateWarehouse_whenWarehouseIsInactive() {

        warehouse.setActive(false);

        when(warehouseRepository.findById(warehouseId))
                .thenReturn(Optional.of(warehouse));

        when(warehouseRepository.save(warehouse))
                .thenReturn(warehouse);

        WarehouseResponse activeResponse =
                new WarehouseResponse(
                        warehouseId,
                        warehouse.getCode(),
                        warehouse.getName(),
                        warehouse.getAddress(),
                        warehouse.getCity(),
                        warehouse.getCountry(),
                        warehouse.getCapacity(),
                        true,
                        null,
                        null
                );

        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(activeResponse);

        WarehouseResponse result =
                warehouseService.activateWarehouse(
                        warehouseId
                );

        assertTrue(warehouse.isActive());
        assertTrue(result.active());

        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void getAllWarehouses_shouldReturnPaginatedWarehouses() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Warehouse> warehousePage =
                new PageImpl<>(
                        List.of(warehouse),
                        pageable,
                        1
                );

        when(warehouseRepository.findAll(pageable))
                .thenReturn(warehousePage);

        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(response);

        Page<WarehouseResponse> result =
                warehouseService.getAllWarehouses(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(
                warehouseId,
                result.getContent().getFirst().id()
        );

        verify(warehouseRepository).findAll(pageable);
    }
}
