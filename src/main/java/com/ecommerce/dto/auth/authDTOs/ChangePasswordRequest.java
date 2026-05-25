package com.ecommerce.dto.auth.authDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Change password request DTO")
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    @Schema(example = "OldPassword@123")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters")
    @Schema(example = "NewPassword@456")
    private String newPassword;

    @NotBlank(message = "Password confirmation is required")
    @Schema(example = "NewPassword@456")
    private String confirmPassword;
}
