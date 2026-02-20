package com.example.organization.dto;

import com.example.organization.model.Address;
import com.example.organization.model.Coordinates;
import com.example.organization.model.OrganizationType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponseDTO {

    private Long id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Double annualTurnover;
    private String fullName;
    private Integer employeesCount;
    private OrganizationType type;
    private Address officialAddress;
}