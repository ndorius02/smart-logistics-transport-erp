package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.TransportCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.TransportUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.TransportResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.TransportStatus;
import com.ndoruhirwe.smartlogistics.service.TransportService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transports")
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransportResponse createTransport(@Valid @RequestBody TransportCreateRequest request) {
        return transportService.createTransport(request);
    }

    @GetMapping
    public Page<TransportResponse> getAllTransports(Pageable pageable) {
        return transportService.getAllTransports(pageable);
    }

    @GetMapping("/search/code")
    public Page<TransportResponse> searchTransportsByCode(@RequestParam String code, Pageable pageable) {
        return transportService.searchTransportsByCode(code, pageable);
    }

    @GetMapping("/status/{status}")
    public Page<TransportResponse> getTransportsByStatus(@PathVariable TransportStatus status, Pageable pageable) {
        return transportService.getTransportsByStatus(status, pageable);
    }

    @GetMapping("/{id}")
    public TransportResponse getTransportById(@PathVariable UUID id) {
        return transportService.getTransportById(id);
    }

    @PutMapping("/{id}")
    public TransportResponse updateTransport(@PathVariable UUID id,
                                             @Valid @RequestBody TransportUpdateRequest request) {
        return transportService.updateTransport(id, request);
    }

    @PatchMapping("/{id}/start")
    public TransportResponse startTransport(@PathVariable UUID id) {
        return transportService.startTransport(id);
    }

    @PatchMapping("/{id}/complete")
    public TransportResponse completeTransport(@PathVariable UUID id) {
        return transportService.completeTransport(id);
    }

    @PatchMapping("/{id}/cancel")
    public TransportResponse cancelTransport(@PathVariable UUID id) {
        return transportService.cancelTransport(id);
    }
}
