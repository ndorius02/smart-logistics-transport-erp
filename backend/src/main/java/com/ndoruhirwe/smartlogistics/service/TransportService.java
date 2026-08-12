package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.TransportCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.TransportUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.TransportResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.TransportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransportService {
    TransportResponse createTransport(TransportCreateRequest request);

    Page<TransportResponse> getAllTransports(Pageable pageable);

    TransportResponse getTransportById(UUID id);

    TransportResponse updateTransport(UUID id, TransportUpdateRequest request);

    TransportResponse startTransport(UUID id);

    TransportResponse completeTransport(UUID id);

    TransportResponse cancelTransport(UUID id);

    Page<TransportResponse> searchTransportsByCode(String code, Pageable pageable);

    Page<TransportResponse> getTransportsByStatus(TransportStatus status, Pageable pageable);
}
