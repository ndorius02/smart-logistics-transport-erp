package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.VehicleCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.VehicleUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.VehicleResponse;
import com.ndoruhirwe.smartlogistics.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "operationalStatus", ignore = true)
    Vehicle toEntity(VehicleCreateRequest request);

    VehicleResponse toResponse(Vehicle vehicle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntity(
            VehicleUpdateRequest request,
            @MappingTarget Vehicle vehicle
    );

}
