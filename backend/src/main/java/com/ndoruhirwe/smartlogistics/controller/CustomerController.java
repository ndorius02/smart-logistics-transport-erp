package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.CustomerCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.CustomerUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.CustomerResponse;
import com.ndoruhirwe.smartlogistics.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        return customerService.createCustomer(request);
    }

    @GetMapping
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        return customerService.getAllCustomers(pageable);
    }

    @GetMapping("/search/company-name")
    public Page<CustomerResponse> searchByCompanyName(@RequestParam String companyName,
                                                      Pageable pageable) {
        return customerService.searchCustomersByCompanyName(companyName, pageable);
    }

    @GetMapping("/search/code")
    public Page<CustomerResponse> searchByCode(@RequestParam String code, Pageable pageable) {
        return customerService.searchCustomersByCode(code, pageable);
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable UUID id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(@PathVariable UUID id,
            @Valid @RequestBody CustomerUpdateRequest request) {
        return customerService.updateCustomer(id, request);
    }

    @PatchMapping("/{id}/activate")
    public CustomerResponse activateCustomer(@PathVariable UUID id) {
        return customerService.activateCustomer(id);
    }

    @PatchMapping("/{id}/deactivate")
    public CustomerResponse deactivateCustomer(@PathVariable UUID id) {
        return customerService.deactivateCustomer(id);
    }
}
