package com.example.organization.dto;

import com.example.organization.model.Address;
import com.example.organization.model.Coordinates;
import com.example.organization.model.OrganizationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationCreateDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Coordinates cannot be null")
    @Valid
    private Coordinates coordinates;

    @DecimalMin(value = "0.0", inclusive = false, message = "Annual turnover must be greater than 0")
    private Double annualTurnover;

    @Size(min = 1, max = 1347, message = "Full name must be between 1 and 1347 characters")
    private String fullName;

    @NotNull(message = "Employees count cannot be null")
    @Min(value = 1, message = "Employees count must be greater than 0")
    private Integer employeesCount;

    @NotNull(message = "Type cannot be null")
    private OrganizationType type;

    @NotNull(message = "Official address cannot be null")
    @Valid
    private Address officialAddress;
}