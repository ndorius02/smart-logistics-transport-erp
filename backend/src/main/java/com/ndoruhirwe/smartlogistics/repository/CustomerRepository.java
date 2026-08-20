package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByCodeIgnoreCase(String code);

    Optional<Customer> findByVatNumberIgnoreCase(String vatNumber);

    Page<Customer> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);

    Page<Customer> findByCodeContainingIgnoreCase(String code, Pageable pageable);
}
