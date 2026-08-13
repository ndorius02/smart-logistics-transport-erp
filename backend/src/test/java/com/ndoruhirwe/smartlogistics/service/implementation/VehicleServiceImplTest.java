package com.ndoruhirwe.smartlogistics.service.implementation;


import com.ndoruhirwe.smartlogistics.dto.request.VehicleCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.VehicleUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.VehicleResponse;
import com.ndoruhirwe.smartlogistics.entity.Vehicle;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleStatus;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleType;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.VehicleMapper;
import com.ndoruhirwe.smartlogistics.repository.VehicleRepository;
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

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.DUPLICATE_VEHICLE_REGISTRATION;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.VEHICLE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {
    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    private VehicleServiceImpl vehicleService;

    private UUID vehicleId;
    private Vehicle vehicle;
    private VehicleResponse response;

    @BeforeEach
    void setUp() {

        vehicleService = new VehicleServiceImpl(
                vehicleRepository,
                vehicleMapper
        );

        vehicleId = UUID.randomUUID();

        vehicle = Vehicle.builder()
                .id(vehicleId)
                .registrationNumber("1-ABC-123")
                .brand("Volvo")
                .model("FH16")
                .vehicleType(VehicleType.TRUCK)
                .loadCapacity(24000)
                .operationalStatus(VehicleStatus.AVAILABLE)
                .active(true)
                .build();

        response = new VehicleResponse(
                vehicleId,
                "1-ABC-123",
                "Volvo",
                "FH16",
                VehicleType.TRUCK,
                24000,
                VehicleStatus.AVAILABLE,
                true,
                null,
                null
        );
    }

    @Test
    void createVehicle_shouldSaveAndReturnVehicle_whenRegistrationIsUnique() {

        VehicleCreateRequest request =
                new VehicleCreateRequest(
                        " 1-abc-123 ",
                        "Volvo",
                        "FH16",
                        VehicleType.TRUCK,
                        24000
                );

        when(vehicleRepository
                .findByRegistrationNumberIgnoreCase("1-ABC-123"))
                .thenReturn(Optional.empty());

        when(vehicleMapper.toEntity(request))
                .thenReturn(vehicle);

        when(vehicleRepository.save(vehicle))
                .thenReturn(vehicle);

        when(vehicleMapper.toResponse(vehicle))
                .thenReturn(response);

        VehicleResponse result =
                vehicleService.createVehicle(request);

        assertNotNull(result);
        assertEquals(vehicleId, result.id());
        assertEquals("1-ABC-123", result.registrationNumber());
        assertEquals(VehicleStatus.AVAILABLE, result.operationalStatus());
        assertTrue(result.active());

        verify(vehicleRepository)
                .findByRegistrationNumberIgnoreCase("1-ABC-123");

        verify(vehicleRepository).save(vehicle);
        verify(vehicleMapper).toResponse(vehicle);
    }

    @Test
    void createVehicle_shouldThrowDuplicateResourceException_whenRegistrationAlreadyExists() {

        VehicleCreateRequest request =
                new VehicleCreateRequest(
                        "1-ABC-123",
                        "Volvo",
                        "FH16",
                        VehicleType.TRUCK,
                        24000
                );

        when(vehicleRepository
                .findByRegistrationNumberIgnoreCase("1-ABC-123"))
                .thenReturn(Optional.of(vehicle));

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> vehicleService.createVehicle(request)
                );

        assertEquals(
                DUPLICATE_VEHICLE_REGISTRATION,
                exception.getMessage()
        );

        verify(vehicleRepository, never()).save(any());
        verify(vehicleMapper, never()).toEntity(any());
    }

    @Test
    void getVehicleById_shouldReturnVehicle_whenVehicleExists() {

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(vehicleMapper.toResponse(vehicle))
                .thenReturn(response);

        VehicleResponse result =
                vehicleService.getVehicleById(vehicleId);

        assertNotNull(result);
        assertEquals(vehicleId, result.id());
        assertEquals("Volvo", result.brand());

        verify(vehicleRepository).findById(vehicleId);
        verify(vehicleMapper).toResponse(vehicle);
    }

    @Test
    void getVehicleById_shouldThrowResourceNotFoundException_whenVehicleDoesNotExist() {

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> vehicleService.getVehicleById(vehicleId)
                );

        assertEquals(
                VEHICLE_NOT_FOUND,
                exception.getMessage()
        );

        verify(vehicleMapper, never()).toResponse(any());
    }

    @Test
    void updateVehicle_shouldUpdateAndReturnVehicle_whenDataIsValid() {

        VehicleUpdateRequest request =
                new VehicleUpdateRequest(
                        " 1-abc-999 ",
                        "Volvo",
                        "FH Aero",
                        VehicleType.TRUCK,
                        25000,
                        VehicleStatus.MAINTENANCE
                );

        VehicleResponse updatedResponse =
                new VehicleResponse(
                        vehicleId,
                        "1-ABC-999",
                        "Volvo",
                        "FH Aero",
                        VehicleType.TRUCK,
                        25000,
                        VehicleStatus.MAINTENANCE,
                        true,
                        null,
                        null
                );

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(vehicleRepository
                .findByRegistrationNumberIgnoreCase("1-ABC-999"))
                .thenReturn(Optional.empty());

        doAnswer(invocation -> {
            Vehicle target = invocation.getArgument(1);

            target.setBrand("Volvo");
            target.setModel("FH Aero");
            target.setVehicleType(VehicleType.TRUCK);
            target.setLoadCapacity(25000);
            target.setOperationalStatus(VehicleStatus.MAINTENANCE);

            return null;
        }).when(vehicleMapper)
                .updateEntity(request, vehicle);

        when(vehicleRepository.save(vehicle))
                .thenReturn(vehicle);

        when(vehicleMapper.toResponse(vehicle))
                .thenReturn(updatedResponse);

        VehicleResponse result =
                vehicleService.updateVehicle(
                        vehicleId,
                        request
                );

        assertEquals(
                "1-ABC-999",
                vehicle.getRegistrationNumber()
        );

        assertEquals(
                VehicleStatus.MAINTENANCE,
                result.operationalStatus()
        );

        verify(vehicleMapper)
                .updateEntity(request, vehicle);

        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void deactivateVehicle_shouldDeactivateVehicle_whenVehicleIsActive() {

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(vehicleRepository.save(vehicle))
                .thenReturn(vehicle);

        VehicleResponse inactiveResponse =
                new VehicleResponse(
                        vehicleId,
                        vehicle.getRegistrationNumber(),
                        vehicle.getBrand(),
                        vehicle.getModel(),
                        vehicle.getVehicleType(),
                        vehicle.getLoadCapacity(),
                        vehicle.getOperationalStatus(),
                        false,
                        null,
                        null
                );

        when(vehicleMapper.toResponse(vehicle))
                .thenReturn(inactiveResponse);

        VehicleResponse result =
                vehicleService.deactivateVehicle(vehicleId);

        assertFalse(vehicle.isActive());
        assertFalse(result.active());

        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void activateVehicle_shouldActivateVehicle_whenVehicleIsInactive() {

        vehicle.setActive(false);

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(vehicleRepository.save(vehicle))
                .thenReturn(vehicle);

        VehicleResponse activeResponse =
                new VehicleResponse(
                        vehicleId,
                        vehicle.getRegistrationNumber(),
                        vehicle.getBrand(),
                        vehicle.getModel(),
                        vehicle.getVehicleType(),
                        vehicle.getLoadCapacity(),
                        vehicle.getOperationalStatus(),
                        true,
                        null,
                        null
                );

        when(vehicleMapper.toResponse(vehicle))
                .thenReturn(activeResponse);

        VehicleResponse result =
                vehicleService.activateVehicle(vehicleId);

        assertTrue(vehicle.isActive());
        assertTrue(result.active());

        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void getAllVehicles_shouldReturnPaginatedVehicles() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Vehicle> vehiclePage =
                new PageImpl<>(
                        List.of(vehicle),
                        pageable,
                        1
                );

        when(vehicleRepository.findAll(pageable))
                .thenReturn(vehiclePage);

        when(vehicleMapper.toResponse(vehicle))
                .thenReturn(response);

        Page<VehicleResponse> result =
                vehicleService.getAllVehicles(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(
                vehicleId,
                result.getContent().getFirst().id()
        );

        verify(vehicleRepository).findAll(pageable);
    }
}
