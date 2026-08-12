package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.TransportCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.TransportUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.TransportResponse;
import com.ndoruhirwe.smartlogistics.entity.Driver;
import com.ndoruhirwe.smartlogistics.entity.Transport;
import com.ndoruhirwe.smartlogistics.entity.Vehicle;
import com.ndoruhirwe.smartlogistics.entity.Warehouse;
import com.ndoruhirwe.smartlogistics.entity.enums.DriverStatus;
import com.ndoruhirwe.smartlogistics.entity.enums.TransportStatus;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleStatus;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.exception.BusinessRuleException;
import com.ndoruhirwe.smartlogistics.mapper.TransportMapper;
import com.ndoruhirwe.smartlogistics.repository.DriverRepository;
import com.ndoruhirwe.smartlogistics.repository.TransportRepository;
import com.ndoruhirwe.smartlogistics.repository.VehicleRepository;
import com.ndoruhirwe.smartlogistics.repository.WarehouseRepository;
import com.ndoruhirwe.smartlogistics.service.TransportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@Transactional

public class TransportServiceImpl implements TransportService {

    private final TransportRepository transportRepository;
    private final WarehouseRepository warehouseRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TransportMapper transportMapper;

    public TransportServiceImpl(
            TransportRepository transportRepository,
            WarehouseRepository warehouseRepository,
            VehicleRepository vehicleRepository,
            DriverRepository driverRepository,
            TransportMapper transportMapper
    ) {
        this.transportRepository = transportRepository;
        this.warehouseRepository = warehouseRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.transportMapper = transportMapper;
    }

    @Override
    public TransportResponse createTransport(
            TransportCreateRequest request
    ) {
        String normalizedCode = normalizeCode(request.code());

        validateUniqueCode(normalizedCode, null);

        validateDifferentWarehouses(request.originWarehouseId(),
                request.destinationWarehouseId()
        );

        validatePlannedDates(request.plannedDepartureAt(), request.plannedArrivalAt());

        Warehouse originWarehouse = findWarehouseById(request.originWarehouseId());

        Warehouse destinationWarehouse = findWarehouseById(request.destinationWarehouseId());

        validateWarehouseActive(originWarehouse);
        validateWarehouseActive(destinationWarehouse);

        Vehicle vehicle = findVehicleById(request.vehicleId());

        Driver driver = findDriverById(request.driverId());

        validateVehicleAvailable(vehicle);
        validateDriverAvailable(driver);

        Transport transport = transportMapper.toEntity(request);

        transport.setCode(normalizedCode);
        transport.setOriginWarehouse(originWarehouse);
        transport.setDestinationWarehouse(destinationWarehouse);
        transport.setVehicle(vehicle);
        transport.setDriver(driver);
        transport.setStatus(TransportStatus.PLANNED);

        Transport savedTransport = transportRepository.save(transport);

        return transportMapper.toResponse(savedTransport);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransportResponse> getAllTransports(Pageable pageable) {
        return transportRepository.findAll(pageable)
                .map(transportMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TransportResponse getTransportById(UUID id) {

        Transport transport = findTransportById(id);

        return transportMapper.toResponse(transport);
    }

    @Override
    public TransportResponse updateTransport(UUID id, TransportUpdateRequest request) {
        Transport transport = findTransportById(id);

        validateTransportIsPlanned(transport);

        String normalizedCode = normalizeCode(request.code());

        validateUniqueCode(normalizedCode, id);

        validateDifferentWarehouses(request.originWarehouseId(), request.destinationWarehouseId());

        validatePlannedDates(request.plannedDepartureAt(), request.plannedArrivalAt());

        Warehouse originWarehouse = findWarehouseById(request.originWarehouseId());

        Warehouse destinationWarehouse = findWarehouseById(request.destinationWarehouseId());

        validateWarehouseActive(originWarehouse);
        validateWarehouseActive(destinationWarehouse);

        Vehicle vehicle = findVehicleById(request.vehicleId());

        Driver driver = findDriverById(request.driverId());

        validateVehicleAvailable(vehicle);
        validateDriverAvailable(driver);

        transportMapper.updateEntity(request, transport);

        transport.setCode(normalizedCode);
        transport.setOriginWarehouse(originWarehouse);
        transport.setDestinationWarehouse(destinationWarehouse);
        transport.setVehicle(vehicle);
        transport.setDriver(driver);

        Transport updatedTransport = transportRepository.save(transport);

        return transportMapper.toResponse(updatedTransport);
    }

    @Override
    public TransportResponse startTransport(UUID id) {

        Transport transport = findTransportById(id);

        if (transport.getStatus() != TransportStatus.PLANNED) {
            throw new BusinessRuleException(
                    INVALID_TRANSPORT_STATUS_TRANSITION
            );
        }

        Vehicle vehicle = transport.getVehicle();
        Driver driver = transport.getDriver();

        validateVehicleAvailable(vehicle);
        validateDriverAvailable(driver);

        transport.setStatus(TransportStatus.IN_PROGRESS);

        transport.setActualDepartureAt(LocalDateTime.now());

        vehicle.setOperationalStatus(VehicleStatus.ASSIGNED);

        driver.setStatus(DriverStatus.ASSIGNED);

        vehicleRepository.save(vehicle);
        driverRepository.save(driver);

        Transport updatedTransport = transportRepository.save(transport);

        return transportMapper.toResponse(updatedTransport);
    }

    @Override
    public TransportResponse completeTransport(UUID id) {

        Transport transport = findTransportById(id);

        if (transport.getStatus()
                != TransportStatus.IN_PROGRESS) {

            throw new BusinessRuleException(
                    INVALID_TRANSPORT_STATUS_TRANSITION
            );
        }

        transport.setStatus(TransportStatus.COMPLETED);

        transport.setActualArrivalAt(LocalDateTime.now());

        Vehicle vehicle = transport.getVehicle();
        Driver driver = transport.getDriver();

        vehicle.setOperationalStatus(VehicleStatus.AVAILABLE);

        driver.setStatus(DriverStatus.AVAILABLE);

        vehicleRepository.save(vehicle);
        driverRepository.save(driver);

        Transport updatedTransport = transportRepository.save(transport);

        return transportMapper.toResponse(updatedTransport);
    }

    @Override
    public TransportResponse cancelTransport(UUID id) {

        Transport transport = findTransportById(id);

        if (transport.getStatus()
                != TransportStatus.PLANNED) {

            throw new BusinessRuleException(
                    INVALID_TRANSPORT_STATUS_TRANSITION
            );
        }

        transport.setStatus(TransportStatus.CANCELLED);

        Transport updatedTransport = transportRepository.save(transport);

        return transportMapper.toResponse(updatedTransport);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransportResponse> searchTransportsByCode(String code, Pageable pageable) {
        String normalizedCode = normalizeCode(code);

        return transportRepository
                .findByCodeContainingIgnoreCase(normalizedCode, pageable)
                .map(transportMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransportResponse> getTransportsByStatus(TransportStatus status, Pageable pageable) {
        return transportRepository
                .findByStatus(status, pageable)
                .map(transportMapper::toResponse);
    }

    private Transport findTransportById(UUID id) {

        return transportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(TRANSPORT_NOT_FOUND)
                );
    }

    private Warehouse findWarehouseById(UUID id) {

        return warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(WAREHOUSE_NOT_FOUND)
                );
    }

    private Vehicle findVehicleById(UUID id) {

        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(VEHICLE_NOT_FOUND)
                );
    }

    private Driver findDriverById(UUID id) {

        return driverRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(DRIVER_NOT_FOUND)
                );
    }

    private void validateUniqueCode(String code, UUID currentTransportId) {
        transportRepository.findByCodeIgnoreCase(code)
                .filter(existingTransport ->
                        currentTransportId == null
                                || !existingTransport.getId()
                                .equals(currentTransportId)
                )
                .ifPresent(existingTransport -> {
                    throw new DuplicateResourceException(DUPLICATE_TRANSPORT_CODE);
                });
    }

    private void validateDifferentWarehouses(UUID originWarehouseId, UUID destinationWarehouseId) {
        if (originWarehouseId.equals(
                destinationWarehouseId
        )) {
            throw new BusinessRuleException(ORIGIN_DESTINATION_MUST_DIFFER);
        }
    }

    private void validateWarehouseActive(Warehouse warehouse) {
        if (!warehouse.isActive()) {
            throw new BusinessRuleException(
                    WAREHOUSE_INACTIVE
            );
        }
    }

    private void validateVehicleAvailable(Vehicle vehicle) {
        if (!vehicle.isActive()
                || vehicle.getOperationalStatus()
                != VehicleStatus.AVAILABLE) {

            throw new BusinessRuleException(VEHICLE_NOT_AVAILABLE);
        }
    }

    private void validateDriverAvailable(Driver driver) {
        if (!driver.isActive()
                || driver.getStatus()
                != DriverStatus.AVAILABLE) {

            throw new BusinessRuleException(DRIVER_NOT_AVAILABLE);
        }
    }

    private void validatePlannedDates(LocalDateTime departure, LocalDateTime arrival) {
        if (!departure.isBefore(arrival)) {
            throw new BusinessRuleException(INVALID_TRANSPORT_DATES);
        }
    }

    private void validateTransportIsPlanned(Transport transport) {
        if (transport.getStatus()
                != TransportStatus.PLANNED) {

            throw new BusinessRuleException(INVALID_TRANSPORT_STATUS_TRANSITION);
        }
    }

    private String normalizeCode(String code) {
        return code
                .trim()
                .toUpperCase();
    }
}
