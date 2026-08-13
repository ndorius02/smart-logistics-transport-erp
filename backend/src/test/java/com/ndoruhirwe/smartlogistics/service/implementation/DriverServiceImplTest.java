package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.DriverCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.DriverUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.DriverResponse;
import com.ndoruhirwe.smartlogistics.entity.Driver;
import com.ndoruhirwe.smartlogistics.entity.enums.DriverStatus;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.DriverMapper;
import com.ndoruhirwe.smartlogistics.repository.DriverRepository;
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

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.DRIVER_NOT_FOUND;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.DUPLICATE_DRIVER_LICENSE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {
    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    private DriverServiceImpl driverService;

    private UUID driverId;
    private Driver driver;
    private DriverResponse response;

    @BeforeEach
    void setUp() {

        driverService = new DriverServiceImpl(
                driverRepository,
                driverMapper
        );

        driverId = UUID.randomUUID();

        driver = Driver.builder()
                .id(driverId)
                .firstName("Lucas")
                .lastName("Martin")
                .licenseNumber("BE-DRV-2026-001")
                .phoneNumber("+32470000001")
                .status(DriverStatus.AVAILABLE)
                .active(true)
                .build();

        response = new DriverResponse(
                driverId,
                "Lucas",
                "Martin",
                "BE-DRV-2026-001",
                "+32470000001",
                DriverStatus.AVAILABLE,
                true,
                null,
                null
        );
    }

    @Test
    void createDriver_shouldSaveAndReturnDriver_whenLicenseIsUnique() {

        DriverCreateRequest request =
                new DriverCreateRequest(
                        "Lucas",
                        "Martin",
                        " be-drv-2026-001 ",
                        "+32470000001"
                );

        when(driverRepository
                .findByLicenseNumberIgnoreCase("BE-DRV-2026-001"))
                .thenReturn(Optional.empty());

        when(driverMapper.toEntity(request))
                .thenReturn(driver);

        when(driverRepository.save(driver))
                .thenReturn(driver);

        when(driverMapper.toResponse(driver))
                .thenReturn(response);

        DriverResponse result =
                driverService.createDriver(request);

        assertNotNull(result);
        assertEquals(driverId, result.id());
        assertEquals(
                "BE-DRV-2026-001",
                result.licenseNumber()
        );
        assertEquals(
                DriverStatus.AVAILABLE,
                result.status()
        );
        assertTrue(result.active());

        verify(driverRepository)
                .findByLicenseNumberIgnoreCase(
                        "BE-DRV-2026-001"
                );

        verify(driverRepository).save(driver);
        verify(driverMapper).toResponse(driver);
    }

    @Test
    void createDriver_shouldThrowDuplicateResourceException_whenLicenseAlreadyExists() {

        DriverCreateRequest request =
                new DriverCreateRequest(
                        "Lucas",
                        "Martin",
                        "BE-DRV-2026-001",
                        "+32470000001"
                );

        when(driverRepository
                .findByLicenseNumberIgnoreCase(
                        "BE-DRV-2026-001"
                ))
                .thenReturn(Optional.of(driver));

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> driverService.createDriver(request)
                );

        assertEquals(
                DUPLICATE_DRIVER_LICENSE,
                exception.getMessage()
        );

        verify(driverRepository, never()).save(any());
        verify(driverMapper, never()).toEntity(any());
    }

    @Test
    void getDriverById_shouldReturnDriver_whenDriverExists() {

        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(driver));

        when(driverMapper.toResponse(driver))
                .thenReturn(response);

        DriverResponse result =
                driverService.getDriverById(driverId);

        assertNotNull(result);
        assertEquals(driverId, result.id());
        assertEquals("Lucas", result.firstName());
        assertEquals("Martin", result.lastName());

        verify(driverRepository).findById(driverId);
        verify(driverMapper).toResponse(driver);
    }

    @Test
    void getDriverById_shouldThrowResourceNotFoundException_whenDriverDoesNotExist() {

        when(driverRepository.findById(driverId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> driverService.getDriverById(driverId)
                );

        assertEquals(
                DRIVER_NOT_FOUND,
                exception.getMessage()
        );

        verify(driverMapper, never()).toResponse(any());
    }

    @Test
    void updateDriver_shouldUpdateAndReturnDriver_whenDataIsValid() {

        DriverUpdateRequest request =
                new DriverUpdateRequest(
                        "Lucas",
                        "Martin",
                        " be-drv-2026-999 ",
                        "+32470000999",
                        DriverStatus.ON_LEAVE
                );

        DriverResponse updatedResponse =
                new DriverResponse(
                        driverId,
                        "Lucas",
                        "Martin",
                        "BE-DRV-2026-999",
                        "+32470000999",
                        DriverStatus.ON_LEAVE,
                        true,
                        null,
                        null
                );

        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(driver));

        when(driverRepository
                .findByLicenseNumberIgnoreCase(
                        "BE-DRV-2026-999"
                ))
                .thenReturn(Optional.empty());

        doAnswer(invocation -> {
            Driver target = invocation.getArgument(1);

            target.setFirstName("Lucas");
            target.setLastName("Martin");
            target.setPhoneNumber("+32470000999");
            target.setStatus(DriverStatus.ON_LEAVE);

            return null;
        }).when(driverMapper)
                .updateEntity(request, driver);

        when(driverRepository.save(driver))
                .thenReturn(driver);

        when(driverMapper.toResponse(driver))
                .thenReturn(updatedResponse);

        DriverResponse result =
                driverService.updateDriver(
                        driverId,
                        request
                );

        assertEquals(
                "BE-DRV-2026-999",
                driver.getLicenseNumber()
        );

        assertEquals(
                DriverStatus.ON_LEAVE,
                result.status()
        );

        verify(driverMapper)
                .updateEntity(request, driver);

        verify(driverRepository).save(driver);
    }

    @Test
    void deactivateDriver_shouldDeactivateDriver_whenDriverIsActive() {

        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(driver));

        when(driverRepository.save(driver))
                .thenReturn(driver);

        DriverResponse inactiveResponse =
                new DriverResponse(
                        driverId,
                        driver.getFirstName(),
                        driver.getLastName(),
                        driver.getLicenseNumber(),
                        driver.getPhoneNumber(),
                        driver.getStatus(),
                        false,
                        null,
                        null
                );

        when(driverMapper.toResponse(driver))
                .thenReturn(inactiveResponse);

        DriverResponse result =
                driverService.deactivateDriver(driverId);

        assertFalse(driver.isActive());
        assertFalse(result.active());

        verify(driverRepository).save(driver);
    }

    @Test
    void activateDriver_shouldActivateDriver_whenDriverIsInactive() {

        driver.setActive(false);

        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(driver));

        when(driverRepository.save(driver))
                .thenReturn(driver);

        DriverResponse activeResponse =
                new DriverResponse(
                        driverId,
                        driver.getFirstName(),
                        driver.getLastName(),
                        driver.getLicenseNumber(),
                        driver.getPhoneNumber(),
                        driver.getStatus(),
                        true,
                        null,
                        null
                );

        when(driverMapper.toResponse(driver))
                .thenReturn(activeResponse);

        DriverResponse result =
                driverService.activateDriver(driverId);

        assertTrue(driver.isActive());
        assertTrue(result.active());

        verify(driverRepository).save(driver);
    }

    @Test
    void getAllDrivers_shouldReturnPaginatedDrivers() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Driver> driverPage =
                new PageImpl<>(
                        List.of(driver),
                        pageable,
                        1
                );

        when(driverRepository.findAll(pageable))
                .thenReturn(driverPage);

        when(driverMapper.toResponse(driver))
                .thenReturn(response);

        Page<DriverResponse> result =
                driverService.getAllDrivers(pageable);

        assertEquals(
                1,
                result.getTotalElements()
        );

        assertEquals(
                1,
                result.getContent().size()
        );

        assertEquals(
                driverId,
                result.getContent().getFirst().id()
        );

        verify(driverRepository)
                .findAll(pageable);
    }
}
