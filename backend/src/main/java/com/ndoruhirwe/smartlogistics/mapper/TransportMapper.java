package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.TransportCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.TransportUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.TransportResponse;
import com.ndoruhirwe.smartlogistics.entity.Transport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransportMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originWarehouse", ignore = true)
    @Mapping(target = "destinationWarehouse", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "actualDepartureAt", ignore = true)
    @Mapping(target = "actualArrivalAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Transport toEntity(TransportCreateRequest request);

    @Mapping(source = "originWarehouse.id", target = "originWarehouseId")
    @Mapping(source = "originWarehouse.code", target = "originWarehouseCode")
    @Mapping(source = "originWarehouse.name", target = "originWarehouseName")

    @Mapping(source = "destinationWarehouse.id", target = "destinationWarehouseId")
    @Mapping(source = "destinationWarehouse.code", target = "destinationWarehouseCode")
    @Mapping(source = "destinationWarehouse.name", target = "destinationWarehouseName")

    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.registrationNumber", target = "vehicleRegistrationNumber")

    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "driver.firstName", target = "driverFirstName")
    @Mapping(source = "driver.lastName", target = "driverLastName")
    TransportResponse toResponse(Transport transport);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originWarehouse", ignore = true)
    @Mapping(target = "destinationWarehouse", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "actualDepartureAt", ignore = true)
    @Mapping(target = "actualArrivalAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(TransportUpdateRequest request, @MappingTarget Transport transport);
}
