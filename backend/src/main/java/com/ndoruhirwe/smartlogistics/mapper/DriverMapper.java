package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.DriverCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.DriverUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.DriverResponse;
import com.ndoruhirwe.smartlogistics.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface DriverMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "status", ignore = true)
    Driver toEntity(DriverCreateRequest request);

    DriverResponse toResponse(Driver driver);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntity(DriverUpdateRequest request, @MappingTarget Driver driver);
}
