package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.SupplierCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.SupplierUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.SupplierResponse;
import com.ndoruhirwe.smartlogistics.entity.Supplier;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.SupplierMapper;
import com.ndoruhirwe.smartlogistics.repository.SupplierRepository;
import com.ndoruhirwe.smartlogistics.service.SupplierService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(
            SupplierRepository supplierRepository,
            SupplierMapper supplierMapper
    ) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public SupplierResponse createSupplier(SupplierCreateRequest request) {

        String normalizedCode = normalizeCode(request.code());

        String normalizedVatNumber = normalizeVatNumber(request.vatNumber());

        validateUniqueCode(normalizedCode, null);

        validateUniqueVatNumber(normalizedVatNumber, null);

        Supplier supplier = supplierMapper.toEntity(request);

        supplier.setCode(normalizedCode);
        supplier.setVatNumber(normalizedVatNumber);
        supplier.setActive(true);

        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toResponse(savedSupplier);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierResponse> getAllSuppliers(Pageable pageable) {
        return supplierRepository
                .findAll(pageable)
                .map(supplierMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(UUID id) {
        Supplier supplier = findSupplierById(id);
        return supplierMapper.toResponse(supplier);
    }

    @Override
    public SupplierResponse updateSupplier(UUID id, SupplierUpdateRequest request) {
        Supplier supplier = findSupplierById(id);

        String normalizedCode = normalizeCode(request.code());

        String normalizedVatNumber = normalizeVatNumber(request.vatNumber());

        validateUniqueCode(normalizedCode, id);

        validateUniqueVatNumber(normalizedVatNumber, id);

        supplierMapper.updateEntity(request, supplier);
        supplier.setCode(normalizedCode);
        supplier.setVatNumber(normalizedVatNumber);

        Supplier updatedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toResponse(updatedSupplier);
    }

    @Override
    public SupplierResponse activateSupplier(UUID id) {
        return changeActiveStatus(id, true);
    }

    @Override
    public SupplierResponse deactivateSupplier(UUID id) {
        return changeActiveStatus(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierResponse>
    searchSuppliersByCompanyName(String companyName, Pageable pageable) {
        String normalizedCompanyName = companyName.trim();

        return supplierRepository
                .findByCompanyNameContainingIgnoreCase(normalizedCompanyName, pageable)
                .map(supplierMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierResponse>
    searchSuppliersByCode(String code, Pageable pageable) {
        String normalizedCode = normalizeCode(code);

        return supplierRepository
                .findByCodeContainingIgnoreCase(normalizedCode, pageable)
                .map(supplierMapper::toResponse);
    }

    private Supplier findSupplierById(UUID id) {
        return supplierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(SUPPLIER_NOT_FOUND));
    }

    private void validateUniqueCode(String code, UUID currentSupplierId) {
        supplierRepository
                .findByCodeIgnoreCase(code)
                .filter(existingSupplier ->
                        currentSupplierId == null
                                || !existingSupplier
                                .getId()
                                .equals(currentSupplierId)
                )
                .ifPresent(existingSupplier -> {
                    throw new DuplicateResourceException(DUPLICATE_SUPPLIER_CODE);});
    }

    private void validateUniqueVatNumber(String vatNumber, UUID currentSupplierId) {
        if (vatNumber == null) {
            return;
        }

        supplierRepository
                .findByVatNumberIgnoreCase(vatNumber)
                .filter(existingSupplier ->
                        currentSupplierId == null
                                || !existingSupplier
                                .getId()
                                .equals(currentSupplierId)
                )
                .ifPresent(existingSupplier -> {
                    throw new DuplicateResourceException(DUPLICATE_SUPPLIER_VAT_NUMBER);
                });
    }

    private String normalizeCode(String code) {
        return code
                .trim()
                .toUpperCase();
    }

    private String normalizeVatNumber(String vatNumber) {
        if (
                vatNumber == null || vatNumber.isBlank()
        ) {
            return null;
        }

        return vatNumber
                .trim()
                .toUpperCase();
    }

    private SupplierResponse changeActiveStatus(UUID id, boolean active) {
        Supplier supplier = findSupplierById(id);

        if (supplier.isActive() == active) {
            return supplierMapper.toResponse(supplier);
        }

        supplier.setActive(active);

        Supplier updatedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toResponse(updatedSupplier);
    }
}
