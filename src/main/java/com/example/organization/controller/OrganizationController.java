package com.example.organization.controller;

import com.example.organization.dto.OrganizationCreateDTO;
import com.example.organization.dto.OrganizationResponseDTO;
import com.example.organization.dto.PageResponse;
import com.example.organization.model.OrganizationType;
import com.example.organization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<PageResponse<OrganizationResponseDTO>> getAllOrganizations(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(name = "coordinates.x", required = false) Double coordinatesX,
            @RequestParam(name = "coordinates.y", required = false) Integer coordinatesY,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creationDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creationDateTo,
            @RequestParam(required = false) Double annualTurnoverMin,
            @RequestParam(required = false) Double annualTurnoverMax,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) Integer employeesCountMin,
            @RequestParam(required = false) Integer employeesCountMax,
            @RequestParam(required = false) OrganizationType type,
            @RequestParam(name = "officialAddress.street", required = false) String street) {

        PageResponse<OrganizationResponseDTO> response = organizationService.getAllOrganizations(
                page, size, sort, id, name, coordinatesX, coordinatesY,
                creationDateFrom, creationDateTo, annualTurnoverMin, annualTurnoverMax,
                fullName, employeesCountMin, employeesCountMax, type, street);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> getOrganizationById(
            @PathVariable("id") Long id) {  // ← добавить явное имя
        if (id < 1) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        OrganizationResponseDTO response = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OrganizationResponseDTO> createOrganization(
            @Valid @RequestBody OrganizationCreateDTO dto) {
        OrganizationResponseDTO response = organizationService.createOrganization(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> updateOrganization(
            @PathVariable("id") Long id,  // ← добавить явное имя
            @Valid @RequestBody OrganizationCreateDTO dto) {
        if (id < 1) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        OrganizationResponseDTO response = organizationService.updateOrganization(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(
            @PathVariable("id") Long id) {  // ← добавить явное имя
        if (id < 1) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-full-name/{fullName}")
    public ResponseEntity<Map<String, Object>> deleteByFullName(
            @PathVariable("fullName") String fullName) {  // ← добавить явное имя
        int deletedCount = organizationService.deleteByFullName(fullName);

        Map<String, Object> response = new HashMap<>();
        response.put("deletedCount", deletedCount);
        response.put("fullName", fullName);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/average-turnover")
    public ResponseEntity<Map<String, Object>> getAverageTurnover() {
        Double average = organizationService.getAverageAnnualTurnover();
        long count = organizationService.getCountWithTurnover();

        Map<String, Object> response = new HashMap<>();
        response.put("averageAnnualTurnover", average);
        response.put("count", count);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count-by-type-greater/{type}")
    public ResponseEntity<Map<String, Object>> countByTypeGreater(
            @PathVariable("type") OrganizationType type) {  // ← добавить явное имя
        long count = organizationService.countByTypeGreaterThan(type);

        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        response.put("comparedType", type);

        return ResponseEntity.ok(response);
    }
}