package com.ecommerce.controller;

import com.ecommerce.ApiResponse;
import com.ecommerce.ApiResponseUtil;
import com.ecommerce.dto.auth.authDTOs.AuthResponse;
import com.ecommerce.dto.auth.authDTOs.ChangePasswordRequest;
import com.ecommerce.dto.auth.authDTOs.LoginRequest;
import com.ecommerce.dto.auth.authDTOs.RefreshTokenRequest;
import com.ecommerce.dto.auth.authDTOs.RegisterRequest;
import com.ecommerce.dto.auth.authDTOs.UserDTO;
import com.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and authorization APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        log.info("POST /api/auth/register - Registering user: {}", registerRequest.getUsername());

        AuthResponse authResponse =
                authService.register(registerRequest);

        return ApiResponseUtil.created(
                "User registered successfully",
                authResponse
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        log.info("POST /api/auth/login - User login attempt: {}", loginRequest.getEmailOrUsername());

        AuthResponse authResponse =
                authService.login(loginRequest);

        return ApiResponseUtil.success(
                "Login successful",
                authResponse
        );
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        log.info("POST /api/auth/refresh-token");

        AuthResponse authResponse =
                authService.refreshToken(refreshTokenRequest);

        return ApiResponseUtil.success(
                "Token refreshed successfully",
                authResponse
        );
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change user password",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest
    ) {

        String username = getAuthenticatedUsername();

        log.info("POST /api/auth/change-password - User changing password: {}", username);

        authService.changePassword(
                username,
                changePasswordRequest
        );

        return ApiResponseUtil.success(
                "Password changed successfully",
                null
        );
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {

        String username =
                getAuthenticatedUsername();

        log.info("GET /api/auth/me - Fetching user profile: {}", username);

        UserDTO userDTO =
                authService.getUserProfile(username);

        return ApiResponseUtil.success(
                "User profile fetched successfully",
                userDTO
        );
    }

    @PutMapping("/me")
    @Operation(summary = "Update user profile",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @Valid @RequestBody UserDTO userDTO
    ) {

        String username =
                getAuthenticatedUsername();

        log.info("PUT /api/auth/me - Updating profile: {}", username);

        UserDTO updatedUser =
                authService.updateUserProfile(
                        username,
                        userDTO
                );

        return ApiResponseUtil.success(
                "Profile updated successfully",
                updatedUser
        );
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Verify user email",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<String>> verifyEmail() {

        String username =
                getAuthenticatedUsername();

        log.info("POST /api/auth/verify-email - Verifying email for user: {}", username);

        authService.verifyEmail(username);

        return ApiResponseUtil.success(
                "Email verified successfully",
                null
        );
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<String>> logout() {

        String username =
                getAuthenticatedUsername();

        log.info("POST /api/auth/logout - User logging out: {}", username);

        return ApiResponseUtil.success(
                "Logged out successfully. Please delete the token on client side.",
                null
        );
    }

    /**
     * Helper method to get authenticated username
     */
    private String getAuthenticatedUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                        !authentication.isAuthenticated()
        ) {
            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        return authentication.getName();
    }
}