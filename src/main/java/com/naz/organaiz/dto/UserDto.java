package com.naz.organaiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDto(@NotBlank(message = "First name is required")
                      String firstName,

                      @NotBlank(message = "Last name is required")
                      String lastName,

                      @NotBlank(message = "Email is required")
                      String email,

                      @NotBlank(message = "Password name is required")
                      @Size(min = 8, message = "Password must be at least 8 characters long")
                      @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&*?])[A-Za-z\\d!@#$%&*?]{8,}",
                      message = "Password must contain at least an uppercase, a lowercase, a digit, and a special character")
                      String password,

                      String phone
) {
}
