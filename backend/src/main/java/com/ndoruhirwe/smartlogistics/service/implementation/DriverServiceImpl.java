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
import com.ndoruhirwe.smartlogistics.service.DriverService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.DRIVER_NOT_FOUND;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.DUPLICATE_DRIVER_LICENSE;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public DriverResponse createDriver(DriverCreateRequest request) {
        String normalizedLicenseNumber = normalizeLicenseNumber(request.licenseNumber());

        validateUniqueLicenseNumber(normalizedLicenseNumber, null);

        Driver driver = driverMapper.toEntity(request);

        driver.setLicenseNumber(normalizedLicenseNumber);

        driver.setStatus(DriverStatus.AVAILABLE);

        driver.setActive(true);

        Driver savedDriver = driverRepository.save(driver);

        return driverMapper.toResponse(savedDriver);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverResponse> getAllDrivers(Pageable pageable
    ) {
        return driverRepository.findAll(pageable)
                .map(driverMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponse getDriverById(UUID id) {

        Driver driver = findDriverById(id);

        return driverMapper.toResponse(driver);
    }

    @Override
    public DriverResponse updateDriver(UUID id, DriverUpdateRequest request) {
        Driver driver = findDriverById(id);

        String normalizedLicenseNumber = normalizeLicenseNumber(request.licenseNumber());

        validateUniqueLicenseNumber(normalizedLicenseNumber, id);

        driverMapper.updateEntity(request, driver);

        driver.setLicenseNumber(normalizedLicenseNumber);

        Driver updatedDriver = driverRepository.save(driver);

        return driverMapper.toResponse(updatedDriver);
    }

    @Override
    public DriverResponse activateDriver(UUID id) {
        return changeActiveStatus(id, true);
    }

    @Override
    public DriverResponse deactivateDriver(UUID id) {
        return changeActiveStatus(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverResponse> searchDriversByLicenseNumber(String licenseNumber, Pageable pageable) {
        String normalizedLicenseNumber = normalizeLicenseNumber(licenseNumber);

        return driverRepository
                .findByLicenseNumberContainingIgnoreCase(normalizedLicenseNumber, pageable)
                .map(driverMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverResponse> searchDriversByLastName(String lastName, Pageable pageable) {
        String normalizedLastName = lastName.trim();

        return driverRepository
                .findByLastNameContainingIgnoreCase(normalizedLastName, pageable)
                .map(driverMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverResponse> getDriversByStatus(DriverStatus status, Pageable pageable) {
        return driverRepository
                .findByStatus(status, pageable)
                .map(driverMapper::toResponse);
    }

    private Driver findDriverById(UUID id) {

        return driverRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(DRIVER_NOT_FOUND));
    }

    private void validateUniqueLicenseNumber(String licenseNumber, UUID currentDriverId) {
        driverRepository
                .findByLicenseNumberIgnoreCase(licenseNumber)
                .filter(existingDriver ->
                        currentDriverId == null
                                || !existingDriver.getId()
                                .equals(currentDriverId)
                )
                .ifPresent(existingDriver -> {
                    throw new DuplicateResourceException(DUPLICATE_DRIVER_LICENSE);
                });
    }

    private String normalizeLicenseNumber(String licenseNumber) {
        return licenseNumber
                .trim()
                .toUpperCase();
    }

    private DriverResponse changeActiveStatus(UUID id, boolean active) {
        Driver driver = findDriverById(id);

        if (driver.isActive() == active) {
            return driverMapper.toResponse(driver);
        }

        driver.setActive(active);

        Driver updatedDriver =
                driverRepository.save(driver);

        return driverMapper.toResponse(updatedDriver);
    }

}
