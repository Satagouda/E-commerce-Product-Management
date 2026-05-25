package com.ecommerce.dto.auth.authDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Registration request DTO")
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(example = "johndoe")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(example = "john@example.com")
    private String email;

    @NotBlank(message = "First name is required")
    @Schema(example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(example = "Doe")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(example = "SecurePassword@123")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    @Schema(example = "SecurePassword@123")
    private String confirmPassword;
}
