package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.CarrierCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.CarrierUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.CarrierResponse;
import com.ndoruhirwe.smartlogistics.entity.Carrier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface CarrierMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Carrier toEntity(CarrierCreateRequest request);

    CarrierResponse toResponse(Carrier carrier);

    @Mapping(target = "id", ignore = true)
    void updateEntity(CarrierUpdateRequest request, @MappingTarget Carrier carrier);
}
