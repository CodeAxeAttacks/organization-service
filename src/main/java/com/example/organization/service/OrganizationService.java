package com.example.organization.service;

import com.example.organization.dto.OrganizationCreateDTO;
import com.example.organization.dto.OrganizationResponseDTO;
import com.example.organization.dto.PageResponse;
import com.example.organization.exception.ResourceNotFoundException;
import com.example.organization.model.Organization;
import com.example.organization.model.OrganizationType;
import com.example.organization.repository.OrganizationRepository;
import com.example.organization.repository.OrganizationSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Transactional(readOnly = true)
    public PageResponse<OrganizationResponseDTO> getAllOrganizations(
            Integer page, Integer size, String sortStr,
            Long id, String name, Double coordinatesX, Integer coordinatesY,
            LocalDateTime creationDateFrom, LocalDateTime creationDateTo,
            Double annualTurnoverMin, Double annualTurnoverMax,
            String fullName, Integer employeesCountMin, Integer employeesCountMax,
            OrganizationType type, String street) {

        // Построение Specification для фильтрации
        Specification<Organization> spec = Specification.where(null);

        if (id != null) spec = spec.and(OrganizationSpecification.hasId(id));
        if (name != null) spec = spec.and(OrganizationSpecification.nameLike(name));
        if (coordinatesX != null) spec = spec.and(OrganizationSpecification.hasCoordinateX(coordinatesX));
        if (coordinatesY != null) spec = spec.and(OrganizationSpecification.hasCoordinateY(coordinatesY));
        if (creationDateFrom != null) spec = spec.and(OrganizationSpecification.createdAfter(creationDateFrom));
        if (creationDateTo != null) spec = spec.and(OrganizationSpecification.createdBefore(creationDateTo));
        if (annualTurnoverMin != null) spec = spec.and(OrganizationSpecification.annualTurnoverMin(annualTurnoverMin));
        if (annualTurnoverMax != null) spec = spec.and(OrganizationSpecification.annualTurnoverMax(annualTurnoverMax));
        if (fullName != null) spec = spec.and(OrganizationSpecification.fullNameLike(fullName));
        if (employeesCountMin != null) spec = spec.and(OrganizationSpecification.employeesCountMin(employeesCountMin));
        if (employeesCountMax != null) spec = spec.and(OrganizationSpecification.employeesCountMax(employeesCountMax));
        if (type != null) spec = spec.and(OrganizationSpecification.hasType(type));
        if (street != null) spec = spec.and(OrganizationSpecification.streetLike(street));

        // Парсинг сортировки
        Sort sort = parseSort(sortStr);
        Pageable pageable = PageRequest.of(page != null ? page : 0,
                size != null ? size : 20,
                sort);

        Page<Organization> organizationPage = organizationRepository.findAll(spec, pageable);

        List<OrganizationResponseDTO> content = organizationPage.getContent().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return PageResponse.<OrganizationResponseDTO>builder()
                .content(content)
                .page(organizationPage.getNumber())
                .size(organizationPage.getSize())
                .totalElements(organizationPage.getTotalElements())
                .totalPages(organizationPage.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public OrganizationResponseDTO getOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id " + id + " not found"));
        return toResponseDTO(organization);
    }

    @Transactional
    public OrganizationResponseDTO createOrganization(OrganizationCreateDTO dto) {
        Organization organization = Organization.builder()
                .name(dto.getName())
                .coordinates(dto.getCoordinates())
                .annualTurnover(dto.getAnnualTurnover())
                .fullName(dto.getFullName())
                .employeesCount(dto.getEmployeesCount())
                .type(dto.getType())
                .officialAddress(dto.getOfficialAddress())
                .build();

        Organization saved = organizationRepository.save(organization);
        log.info("Created organization with id: {}", saved.getId());
        return toResponseDTO(saved);
    }

    @Transactional
    public OrganizationResponseDTO updateOrganization(Long id, OrganizationCreateDTO dto) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id " + id + " not found"));

        organization.setName(dto.getName());
        organization.setCoordinates(dto.getCoordinates());
        organization.setAnnualTurnover(dto.getAnnualTurnover());
        organization.setFullName(dto.getFullName());
        organization.setEmployeesCount(dto.getEmployeesCount());
        organization.setType(dto.getType());
        organization.setOfficialAddress(dto.getOfficialAddress());

        Organization updated = organizationRepository.save(organization);
        log.info("Updated organization with id: {}", id);
        return toResponseDTO(updated);
    }

    @Transactional
    public void deleteOrganization(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Organization with id " + id + " not found");
        }
        organizationRepository.deleteById(id);
        log.info("Deleted organization with id: {}", id);
    }

    @Transactional
    public int deleteByFullName(String fullName) {
        List<Organization> deleted = organizationRepository.deleteByFullName(fullName);
        int count = deleted.size();
        log.info("Deleted {} organizations with fullName: {}", count, fullName);
        return count;
    }

    @Transactional(readOnly = true)
    public Double getAverageAnnualTurnover() {
        return organizationRepository.calculateAverageAnnualTurnover();
    }

    @Transactional(readOnly = true)
    public long getCountWithTurnover() {
        return organizationRepository.countOrganizationsWithTurnover();
    }

    @Transactional(readOnly = true)
    public long countByTypeGreaterThan(OrganizationType type) {
        return organizationRepository.countByTypeGreaterThan(type);
    }

    private OrganizationResponseDTO toResponseDTO(Organization organization) {
        return OrganizationResponseDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .coordinates(organization.getCoordinates())
                .creationDate(organization.getCreationDate())
                .annualTurnover(organization.getAnnualTurnover())
                .fullName(organization.getFullName())
                .employeesCount(organization.getEmployeesCount())
                .type(organization.getType())
                .officialAddress(organization.getOfficialAddress())
                .build();
    }

    private Sort parseSort(String sortStr) {
        if (sortStr == null || sortStr.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = new ArrayList<>();
        String[] sortPairs = sortStr.split(";");

        for (String pair : sortPairs) {
            String[] parts = pair.split(",");
            if (parts.length == 2) {
                String field = parts[0].trim();
                String direction = parts[1].trim().toLowerCase();

                // Обработка вложенных полей
                field = field.replace(".", "_");

                Sort.Direction dir = direction.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(dir, field));
            }
        }

        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }
}