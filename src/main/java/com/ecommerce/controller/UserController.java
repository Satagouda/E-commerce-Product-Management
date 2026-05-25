package com.ecommerce.controller;



import com.ecommerce.dto.auth.authDTOs.UserDTO;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "Get all users", description = "Retrieve all users (ADMIN only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        log.info("GET /admin/users - Fetching all users");
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user by ID (ADMIN only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {

        log.info("GET /admin/users/{} - Fetching user", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieve user by username (ADMIN only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserByUsername(
            @Parameter(description = "Username", example = "johndoe")
            @PathVariable String username) {

        log.info("GET /admin/users/username/{} - Fetching user", username);
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieve user by email (ADMIN only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserByEmail(
            @Parameter(description = "Email address", example = "john@example.com")
            @PathVariable String email) {

        log.info("GET /admin/users/email/{} - Fetching user", email);
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete (soft delete) a user (ADMIN only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {

        log.info("DELETE /admin/users/{} - Deleting user", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles/{roleName}")
    @Operation(summary = "Assign role to user", description = "Assign a role to a user (ADMIN only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role assigned successfully"),
            @ApiResponse(responseCode = "404", description = "User or role not found")
    })
    public ResponseEntity<String> assignRoleToUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Role name", example = "ADMIN")
            @PathVariable String roleName) {

        log.info("POST /admin/users/{}/roles/{} - Assigning role", userId, roleName);
        userService.assignRoleToUser(userId, roleName);
        return ResponseEntity.ok("Role assigned successfully");
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    @Operation(summary = "Remove role from user", description = "Remove a role from a user (ADMIN only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role removed successfully"),
            @ApiResponse(responseCode = "404", description = "User or role not found")
    })
    public ResponseEntity<String> removeRoleFromUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Role name", example = "ADMIN")
            @PathVariable String roleName) {

        log.info("DELETE /admin/users/{}/roles/{} - Removing role", userId, roleName);
        userService.removeRoleFromUser(userId, roleName);
        return ResponseEntity.ok("Role removed successfully");
    }

    @GetMapping("/check/email/{email}")
    @Operation(summary = "Check if email exists", description = "Check if email is already registered",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "200", description = "Check result")
    public ResponseEntity<Boolean> checkEmailExists(
            @Parameter(description = "Email address", example = "john@example.com")
            @PathVariable String email) {

        log.debug("GET /admin/users/check/email/{} - Checking if email exists", email);
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check/username/{username}")
    @Operation(summary = "Check if username exists", description = "Check if username is already taken",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "200", description = "Check result")
    public ResponseEntity<Boolean> checkUsernameExists(
            @Parameter(description = "Username", example = "johndoe")
            @PathVariable String username) {

        log.debug("GET /admin/users/check/username/{} - Checking if username exists", username);
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }
}

