package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.VehicleCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.VehicleUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.VehicleResponse;
import com.ndoruhirwe.smartlogistics.entity.Vehicle;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleStatus;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.VehicleMapper;
import com.ndoruhirwe.smartlogistics.repository.VehicleRepository;
import com.ndoruhirwe.smartlogistics.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.DUPLICATE_VEHICLE_REGISTRATION;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.VEHICLE_NOT_FOUND;

@Service
@Transactional

public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            VehicleMapper vehicleMapper
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public VehicleResponse createVehicle(
            VehicleCreateRequest request
    ) {
        String normalizedRegistrationNumber = normalizeRegistrationNumber(
                request.registrationNumber());

        validateUniqueRegistrationNumber(normalizedRegistrationNumber, null);

        Vehicle vehicle = vehicleMapper.toEntity(request);

        vehicle.setRegistrationNumber(normalizedRegistrationNumber);

        vehicle.setActive(true);
        vehicle.setOperationalStatus(VehicleStatus.AVAILABLE);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponse(savedVehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponse> getAllVehicles(
            Pageable pageable
    ) {
        return vehicleRepository.findAll(pageable)
                .map(vehicleMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse getVehicleById(UUID id) {

        Vehicle vehicle = findVehicleById(id);

        return vehicleMapper.toResponse(vehicle);
    }

    @Override
    public VehicleResponse updateVehicle(UUID id, VehicleUpdateRequest request) {
        Vehicle vehicle = findVehicleById(id);

        String normalizedRegistrationNumber = normalizeRegistrationNumber(
                        request.registrationNumber()
                );

        validateUniqueRegistrationNumber(normalizedRegistrationNumber, id);

        vehicleMapper.updateEntity(request, vehicle);

        vehicle.setRegistrationNumber(normalizedRegistrationNumber);

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponse(updatedVehicle);
    }

    @Override
    public VehicleResponse activateVehicle(UUID id) {
        return changeActiveStatus(id, true);
    }

    @Override
    public VehicleResponse deactivateVehicle(UUID id) {
        return changeActiveStatus(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponse>
    searchVehiclesByRegistrationNumber(String registrationNumber, Pageable pageable) {
        String normalizedRegistrationNumber = normalizeRegistrationNumber(registrationNumber);

        return vehicleRepository
                .findByRegistrationNumberContainingIgnoreCase(
                        normalizedRegistrationNumber, pageable)
                .map(vehicleMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponse> searchVehiclesByBrand(String brand, Pageable pageable) {
        String normalizedBrand = brand.trim();

        return vehicleRepository
                .findByBrandContainingIgnoreCase(
                        normalizedBrand, pageable)
                .map(vehicleMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponse> getVehiclesByStatus(VehicleStatus status, Pageable pageable) {
        return vehicleRepository
                .findByOperationalStatus(status, pageable)
                .map(vehicleMapper::toResponse);
    }

    private Vehicle findVehicleById(UUID id) {

        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                VEHICLE_NOT_FOUND
                        )
                );
    }

    private void validateUniqueRegistrationNumber(String registrationNumber,
                                                  UUID currentVehicleId
    ) {
        vehicleRepository
                .findByRegistrationNumberIgnoreCase(
                        registrationNumber
                )
                .filter(existingVehicle ->
                        currentVehicleId == null
                                || !existingVehicle.getId()
                                .equals(currentVehicleId)
                )
                .ifPresent(existingVehicle -> {
                    throw new DuplicateResourceException(
                            DUPLICATE_VEHICLE_REGISTRATION
                    );
                });
    }

    private String normalizeRegistrationNumber(
            String registrationNumber
    ) {
        return registrationNumber
                .trim()
                .toUpperCase();
    }

    private VehicleResponse changeActiveStatus(
            UUID id,
            boolean active
    ) {
        Vehicle vehicle = findVehicleById(id);

        if (vehicle.isActive() == active) {
            return vehicleMapper.toResponse(vehicle);
        }

        vehicle.setActive(active);

        Vehicle updatedVehicle =
                vehicleRepository.save(vehicle);

        return vehicleMapper.toResponse(updatedVehicle);
    }
}
