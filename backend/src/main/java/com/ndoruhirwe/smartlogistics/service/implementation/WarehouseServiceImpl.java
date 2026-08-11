package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.WarehouseCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.WarehouseUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.WarehouseResponse;
import com.ndoruhirwe.smartlogistics.entity.Warehouse;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.WarehouseMapper;
import com.ndoruhirwe.smartlogistics.repository.WarehouseRepository;
import com.ndoruhirwe.smartlogistics.service.WarehouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.DUPLICATE_WAREHOUSE_CODE;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.WAREHOUSE_NOT_FOUND;
import java.util.UUID;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    public WarehouseServiceImpl(
            WarehouseRepository warehouseRepository,
            WarehouseMapper warehouseMapper
    ) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public WarehouseResponse createWarehouse(
            WarehouseCreateRequest request
    ) {
        String normalizedCode = normalizeCode(request.code());

        validateUniqueCode(normalizedCode, null);

        Warehouse warehouse = warehouseMapper.toEntity(request);

        warehouse.setCode(normalizedCode);
        warehouse.setActive(true);

        Warehouse savedWarehouse =
                warehouseRepository.save(warehouse);

        return warehouseMapper.toResponse(savedWarehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarehouseResponse> getAllWarehouses(Pageable pageable) {

        return warehouseRepository.findAll(pageable)
                .map(warehouseMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getWarehouseById(UUID id) {
        Warehouse warehouse = findWarehouseById(id);

        return warehouseMapper.toResponse(warehouse);
    }

    @Override
    public WarehouseResponse updateWarehouse(
            UUID id,
            WarehouseUpdateRequest request
    ) {
        Warehouse warehouse = findWarehouseById(id);

        String normalizedCode = normalizeCode(request.code());

        validateUniqueCode(normalizedCode, id);

        warehouseMapper.updateEntity(request, warehouse);

        warehouse.setCode(normalizedCode);

        Warehouse updatedWarehouse =
                warehouseRepository.save(warehouse);

        return warehouseMapper.toResponse(updatedWarehouse);
    }

    @Override
    public WarehouseResponse deactivateWarehouse(UUID id) {
        Warehouse warehouse = findWarehouseById(id);

        warehouse.setActive(false);

        warehouseRepository.save(warehouse);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarehouseResponse> searchWarehousesByName(String name, Pageable pageable) {
        String normalizedName = name.trim();
        return warehouseRepository
                .findByNameIgnoreCase(
                        normalizedName,
                        pageable
                )
                .map(warehouseMapper::toResponse);
    }

    @Override
    public WarehouseResponse activateWarehouse(UUID id) {

        Warehouse warehouse = findWarehouseById(id);

        warehouse.setActive(true);

        Warehouse updatedWarehouse =
                warehouseRepository.save(warehouse);

        return warehouseMapper.toResponse(updatedWarehouse);
    }

    private Warehouse findWarehouseById(UUID id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(WAREHOUSE_NOT_FOUND)
                );
    }

    private void validateUniqueCode(
            String code,
            UUID currentWarehouseId
    ) {
        warehouseRepository.findByCodeIgnoreCase(code)
                .filter(existingWarehouse ->
                        currentWarehouseId == null
                                || !existingWarehouse.getId()
                                .equals(currentWarehouseId)
                )
                .ifPresent(existingWarehouse -> {
                    throw new DuplicateResourceException(DUPLICATE_WAREHOUSE_CODE);
                });
    }

    private String normalizeCode(String code) {
        return code
                .trim()
                .toUpperCase();
    }
}
