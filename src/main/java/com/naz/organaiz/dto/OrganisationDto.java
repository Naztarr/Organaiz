package com.naz.organaiz.dto;

import jakarta.validation.constraints.NotBlank;

public record OrganisationDto(@NotBlank(message = "name is required")
                              String name,
                              String description) {
}
