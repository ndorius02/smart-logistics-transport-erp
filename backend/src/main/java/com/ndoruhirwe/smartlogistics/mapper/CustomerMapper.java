package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.CustomerCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.CustomerUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.CustomerResponse;
import com.ndoruhirwe.smartlogistics.entity.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring")

public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Customer toEntity(CustomerCreateRequest request);

    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "id", ignore = true)
    void updateEntity(
            CustomerUpdateRequest request,
            @MappingTarget Customer customer
    );
}
