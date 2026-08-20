package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.CustomerCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.CustomerUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.CustomerResponse;
import com.ndoruhirwe.smartlogistics.entity.Customer;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.CustomerMapper;
import com.ndoruhirwe.smartlogistics.repository.CustomerRepository;
import com.ndoruhirwe.smartlogistics.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            CustomerMapper customerMapper
    ) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponse createCustomer( CustomerCreateRequest request) {

        String normalizedCode = normalizeCode(request.code());

        String normalizedVatNumber = normalizeVatNumber(request.vatNumber());

        validateUniqueCode(normalizedCode, null);

        validateUniqueVatNumber(normalizedVatNumber, null);

        Customer customer = customerMapper.toEntity(request);

        customer.setCode(normalizedCode);
        customer.setVatNumber(normalizedVatNumber);

        customer.setActive(true);

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        return customerRepository
                .findAll(pageable)
                .map(customerMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(UUID id) {

        Customer customer = findCustomerById(id);

        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerUpdateRequest request) {

        Customer customer = findCustomerById(id);

        String normalizedCode = normalizeCode(request.code());

        String normalizedVatNumber = normalizeVatNumber(request.vatNumber());

        validateUniqueCode(normalizedCode, id);

        validateUniqueVatNumber(normalizedVatNumber, id);

        customerMapper.updateEntity(request, customer);

        customer.setCode(normalizedCode);

        customer.setVatNumber(normalizedVatNumber);

        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    public CustomerResponse activateCustomer(UUID id) {

        return changeActiveStatus(id, true);
    }

    @Override
    public CustomerResponse deactivateCustomer(UUID id) {

        return changeActiveStatus(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse>
    searchCustomersByCompanyName(String companyName, Pageable pageable) {

        String normalizedCompanyName = companyName.trim();

        return customerRepository
                .findByCompanyNameContainingIgnoreCase(normalizedCompanyName, pageable)
                .map(customerMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse>
    searchCustomersByCode(String code, Pageable pageable) {

        String normalizedCode = normalizeCode(code);

        return customerRepository
                .findByCodeContainingIgnoreCase(normalizedCode, pageable)
                .map(customerMapper::toResponse);
    }

    private Customer findCustomerById(UUID id) {

        return customerRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CUSTOMER_NOT_FOUND)
                );
    }

    private void validateUniqueCode(String code, UUID currentCustomerId) {

        customerRepository
                .findByCodeIgnoreCase(code)
                .filter(existingCustomer ->
                        currentCustomerId == null
                                || !existingCustomer
                                .getId()
                                .equals(
                                        currentCustomerId
                                )
                )
                .ifPresent(existingCustomer -> {
                    throw new DuplicateResourceException(
                            DUPLICATE_CUSTOMER_CODE
                    );
                });
    }


    private void validateUniqueVatNumber(String vatNumber, UUID currentCustomerId) {

        if (vatNumber == null) {
            return;
        }

        customerRepository
                .findByVatNumberIgnoreCase(vatNumber)
                .filter(existingCustomer ->
                        currentCustomerId == null
                                || !existingCustomer
                                .getId()
                                .equals(
                                        currentCustomerId
                                )
                )
                .ifPresent(existingCustomer -> {
                    throw new DuplicateResourceException(
                            DUPLICATE_CUSTOMER_VAT_NUMBER
                    );
                });
    }

    private String normalizeCode(String code) {

        return code
                .trim()
                .toUpperCase();
    }

    private String normalizeVatNumber(
            String vatNumber
    ) {

        if (
                vatNumber == null || vatNumber.isBlank()
        ) {
            return null;
        }

        return vatNumber
                .trim()
                .toUpperCase();
    }

    private CustomerResponse changeActiveStatus(UUID id, boolean active) {

        Customer customer = findCustomerById(id);

        if (customer.isActive() == active) {
            return customerMapper.toResponse(customer);
        }

        customer.setActive(active);

        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(updatedCustomer);
    }
}
