package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.CarrierCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.CarrierUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.CarrierResponse;
import com.ndoruhirwe.smartlogistics.entity.Carrier;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.CarrierMapper;
import com.ndoruhirwe.smartlogistics.repository.CarrierRepository;
import com.ndoruhirwe.smartlogistics.service.CarrierService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@Transactional

public class CarrierServiceImpl implements CarrierService {

    private final CarrierRepository carrierRepository;
    private final CarrierMapper carrierMapper;

    public CarrierServiceImpl(
            CarrierRepository carrierRepository,
            CarrierMapper carrierMapper
    ) {
        this.carrierRepository = carrierRepository;
        this.carrierMapper = carrierMapper;
    }

    @Override
    public CarrierResponse createCarrier(CarrierCreateRequest request) {

        String normalizedCode = normalizeCode(request.code());

        String normalizedVatNumber = normalizeVatNumber(request.vatNumber());

        String normalizedLicenseNumber = normalizeLicenseNumber(request.licenseNumber());

        validateUniqueCode(normalizedCode, null);

        validateUniqueVatNumber(normalizedVatNumber, null);

        validateUniqueLicenseNumber(normalizedLicenseNumber, null);

        Carrier carrier = carrierMapper.toEntity(request);

        carrier.setCode(normalizedCode);
        carrier.setVatNumber(normalizedVatNumber);
        carrier.setLicenseNumber(normalizedLicenseNumber);
        carrier.setActive(true);

        Carrier savedCarrier = carrierRepository.save(carrier);

        return carrierMapper.toResponse(savedCarrier);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarrierResponse> getAllCarriers(Pageable pageable) {
        return carrierRepository
                .findAll(pageable)
                .map(carrierMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CarrierResponse getCarrierById(UUID id) {
        Carrier carrier = findCarrierById(id);

        return carrierMapper.toResponse(carrier);
    }

    @Override
    public CarrierResponse updateCarrier(UUID id, CarrierUpdateRequest request) {
        Carrier carrier = findCarrierById(id);

        String normalizedCode = normalizeCode(request.code());

        String normalizedVatNumber = normalizeVatNumber(request.vatNumber());

        String normalizedLicenseNumber = normalizeLicenseNumber(request.licenseNumber());

        validateUniqueCode(normalizedCode, id);

        validateUniqueVatNumber(normalizedVatNumber, id);

        validateUniqueLicenseNumber(normalizedLicenseNumber, id);

        carrierMapper.updateEntity(request, carrier);

        carrier.setCode(normalizedCode);
        carrier.setVatNumber(normalizedVatNumber);
        carrier.setLicenseNumber(normalizedLicenseNumber);

        Carrier updatedCarrier = carrierRepository.save(carrier);

        return carrierMapper.toResponse(updatedCarrier);}

    @Override
    public CarrierResponse activateCarrier(UUID id) {
        return changeActiveStatus(id, true);
    }

    @Override
    public CarrierResponse deactivateCarrier(UUID id) {
        return changeActiveStatus(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarrierResponse>
    searchCarriersByCompanyName(String companyName, Pageable pageable) {
        String normalizedCompanyName = companyName.trim();

        return carrierRepository
                .findByCompanyNameContainingIgnoreCase(normalizedCompanyName, pageable)
                .map(carrierMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarrierResponse>
    searchCarriersByCode(String code, Pageable pageable) {
        String normalizedCode = normalizeCode(code);

        return carrierRepository
                .findByCodeContainingIgnoreCase(normalizedCode, pageable)
                .map(carrierMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarrierResponse>
    searchCarriersByLicenseNumber(String licenseNumber, Pageable pageable) {
        String normalizedLicenseNumber = normalizeLicenseNumber(licenseNumber);

        return carrierRepository
                .findByLicenseNumberContainingIgnoreCase(normalizedLicenseNumber, pageable)
                .map(carrierMapper::toResponse);
    }

    private Carrier findCarrierById(UUID id) {
        return carrierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CARRIER_NOT_FOUND)
                );
    }

    private void validateUniqueCode(String code, UUID currentCarrierId) {
        carrierRepository
                .findByCodeIgnoreCase(code)
                .filter(existingCarrier ->
                        currentCarrierId == null
                                || !existingCarrier
                                .getId()
                                .equals(currentCarrierId)
                )
                .ifPresent(existingCarrier -> {
                    throw new DuplicateResourceException(DUPLICATE_CARRIER_CODE);
                });
    }
    private void validateUniqueVatNumber(String vatNumber, UUID currentCarrierId) {
        if (vatNumber == null) {
            return;
        }

        carrierRepository
                .findByVatNumberIgnoreCase(vatNumber)
                .filter(existingCarrier ->
                        currentCarrierId == null
                                || !existingCarrier
                                .getId()
                                .equals(currentCarrierId)
                )
                .ifPresent(existingCarrier -> {
                    throw new DuplicateResourceException(DUPLICATE_CARRIER_VAT_NUMBER);
                });
    }

    private void validateUniqueLicenseNumber(String licenseNumber, UUID currentCarrierId) {
        carrierRepository
                .findByLicenseNumberIgnoreCase(
                        licenseNumber
                )
                .filter(existingCarrier ->
                        currentCarrierId == null
                                || !existingCarrier
                                .getId()
                                .equals(currentCarrierId)
                )
                .ifPresent(existingCarrier -> {
                    throw new DuplicateResourceException(DUPLICATE_CARRIER_LICENSE_NUMBER);
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

    private String normalizeLicenseNumber(String licenseNumber) {
        return licenseNumber
                .trim()
                .toUpperCase();
    }

    private CarrierResponse changeActiveStatus(UUID id, boolean active) {
        Carrier carrier = findCarrierById(id);

        if (carrier.isActive() == active) {
            return carrierMapper.toResponse(carrier);
        }

        carrier.setActive(active);

        Carrier updatedCarrier = carrierRepository.save(carrier);

        return carrierMapper.toResponse(updatedCarrier);
    }
}
