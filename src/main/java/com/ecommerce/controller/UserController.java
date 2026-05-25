package com.ecommerce.controller;

import com.ecommerce.ApiResponse;
import com.ecommerce.ApiResponseUtil;
import com.ecommerce.dto.auth.authDTOs.UserDTO;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "User Management", description = "User management APIs (ADMIN only)")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Get all users",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(

            @PageableDefault(
                    size = 20,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        log.info("GET /api/admin/users");

        Page<UserDTO> users =
                userService.getAllUsers(pageable);

        return ApiResponseUtil.success(
                "Users fetched successfully",
                users
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @PathVariable Long id
    ) {
        log.info("GET /api/admin/users/{}", id);

        UserDTO user =
                userService.getUserById(id);

        return ApiResponseUtil.success(
                "User fetched successfully",
                user
        );
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUsername(
            @PathVariable String username
    ) {
        log.info("GET /api/admin/users/username/{}", username);

        UserDTO user =
                userService.getUserByUsername(username);

        return ApiResponseUtil.success(
                "User fetched successfully",
                user
        );
    }

    @GetMapping("/email/{email}")
    @Operation(
            summary = "Get user by email",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(
            @PathVariable String email
    ) {
        log.info("GET /api/admin/users/email/{}", email);

        UserDTO user =
                userService.getUserByEmail(email);

        return ApiResponseUtil.success(
                "User fetched successfully",
                user
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id
    ) {
        log.info("DELETE /api/admin/users/{}", id);

        userService.deleteUser(id);

        return ApiResponseUtil.success(
                "User deleted successfully",
                null
        );
    }

    @PostMapping("/{userId}/roles/{roleName}")
    @Operation(summary = "Assign role to user",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<String>> assignRoleToUser(

            @PathVariable Long userId,

            @PathVariable String roleName
    ) {

        log.info("POST /api/admin/users/{}/roles/{}", userId, roleName);

        userService.assignRoleToUser(
                userId,
                roleName
        );

        return ApiResponseUtil.success(
                "Role assigned successfully",
                null
        );
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    @Operation(summary = "Remove role from user",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<String>> removeRoleFromUser(

            @PathVariable Long userId,

            @PathVariable String roleName
    ) {

        log.info("DELETE /api/admin/users/{}/roles/{}", userId, roleName);

        userService.removeRoleFromUser(
                userId,
                roleName
        );

        return ApiResponseUtil.success(
                "Role removed successfully",
                null
        );
    }

    @GetMapping("/check/email/{email}")
    @Operation(
            summary = "Check if email exists",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<Boolean>> checkEmailExists(
            @PathVariable String email
    ) {
        log.debug("GET /api/admin/users/check/email/{}", email);

        boolean exists =
                userService.emailExists(email);

        return ApiResponseUtil.success(
                "Email existence checked successfully",
                exists
        );
    }

    @GetMapping("/check/username/{username}")
    @Operation(summary = "Check if username exists",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<Boolean>> checkUsernameExists(
            @PathVariable String username
    ) {

        log.debug("GET /api/admin/users/check/username/{}", username);

        boolean exists =
                userService.usernameExists(username);

        return ApiResponseUtil.success(
                "Username existence checked successfully",
                exists
        );
    }
}