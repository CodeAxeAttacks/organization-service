package com.example.organization.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coordinates {

    @NotNull(message = "Coordinate X cannot be null")
    private Double x;

    @NotNull(message = "Coordinate Y cannot be null")
    @Max(value = 402, message = "Coordinate Y must not exceed 402")
    private Integer y;
}