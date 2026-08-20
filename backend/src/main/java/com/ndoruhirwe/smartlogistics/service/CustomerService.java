package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.CustomerCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.CustomerUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreateRequest request);

    Page<CustomerResponse> getAllCustomers(Pageable pageable);

    CustomerResponse getCustomerById(UUID id);

    CustomerResponse updateCustomer(UUID id, CustomerUpdateRequest request);

    CustomerResponse activateCustomer(UUID id);

    CustomerResponse deactivateCustomer(UUID id);

    Page<CustomerResponse> searchCustomersByCompanyName(String companyName, Pageable pageable);

    Page<CustomerResponse> searchCustomersByCode(String code, Pageable pageable);

}
