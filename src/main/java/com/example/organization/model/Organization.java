package com.example.organization.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "ID must be greater than 0")
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;

    @Embedded
    @NotNull(message = "Coordinates cannot be null")
    private Coordinates coordinates;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Annual turnover must be greater than 0")
    @Column(nullable = true)
    private Double annualTurnover;

    @Size(min = 1, max = 1347, message = "Full name must be between 1 and 1347 characters")
    @Column(length = 1347)
    private String fullName;

    @Min(value = 1, message = "Employees count must be greater than 0")
    @Column(nullable = false)
    private Integer employeesCount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type cannot be null")
    @Column(nullable = false)
    private OrganizationType type;

    @Embedded
    @NotNull(message = "Official address cannot be null")
    private Address officialAddress;
}