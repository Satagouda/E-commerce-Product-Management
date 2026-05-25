package com.ecommerce.dto.auth.authDTOs;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User details DTO")
public class UserDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "johndoe")
    private String username;

    @Schema(example = "john@example.com")
    private String email;

    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "https://cdn.example.com/profiles/john.jpg")
    private String profileImageUrl;

    @Schema(example = "+1234567890")
    private String phoneNumber;

    @Schema(example = "true")
    private Boolean isEmailVerified;

    @Schema(example = "false")
    private Boolean isPhoneVerified;
}

