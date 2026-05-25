package com.ecommerce.service;



import com.ecommerce.dto.auth.authDTOs.*;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.exception.customException.BadRequestException;
import com.ecommerce.exception.customException.ConflictException;
import com.ecommerce.exception.customException.UnauthorizedException;
import com.ecommerce.repository.Bundle.RoleRepository;
import com.ecommerce.repository.Bundle.UserRepository;
import com.ecommerce.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Register a new user
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("Registering new user: {}", registerRequest.getUsername());

        // Validate passwords match
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Check if user already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ConflictException("Username already taken: " + registerRequest.getUsername());
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("Email already registered: " + registerRequest.getEmail());
        }

        // Validate password strength (minimum 8 characters)
        if (registerRequest.getPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }

        // Create new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .isLocked(false)
                .build();

//        // Assign default USER role
//        Role userRole = roleRepository.findByName("ROLE_USER")
//                .orElseThrow(() -> new RuntimeException("USER role not found. Initialize roles in database."));

//        Set<Role> roles = new HashSet<>();
//        roles.add(userRole);
//        user.setRoles(roles);
        Set<Role> roles = new HashSet<>();

// Assign ADMIN role for admin email/username
        if (
                registerRequest.getEmail().equalsIgnoreCase("admin@example.com")
                        || registerRequest.getUsername().equalsIgnoreCase("adminuser")
        ) {

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            roles.add(adminRole);

        } else {

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));

            roles.add(userRole);
        }

        user.setRoles(roles);

        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getId());

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        UserDTO userDTO = convertToUserDTO(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L) // 24 hours in seconds
                .user(userDTO)
                .build();
    }

    /**
     * Login user with email or username
     */
    public AuthResponse login(@Valid  LoginRequest loginRequest) {
        log.info("User login attempt: {}", loginRequest.getEmailOrUsername());

        try {
            // Try to authenticate with the provided credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmailOrUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Get the authenticated user
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UnauthorizedException("User not found"));

            // Check if account is locked
            if (user.getIsLocked()) {
                throw new UnauthorizedException("Account is locked. Please contact support.");
            }

            // Reset failed login attempts on successful login
            user.setFailedLoginAttempts(0);
            userRepository.save(user);

            log.info("User logged in successfully: {}", user.getId());

            // Generate tokens
            String accessToken = jwtTokenProvider.generateAccessToken(username);
            String refreshToken = jwtTokenProvider.generateRefreshToken(username);

            UserDTO userDTO = convertToUserDTO(user);

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(86400L) // 24 hours in seconds
                    .user(userDTO)
                    .build();

        } catch (org.springframework.security.core.AuthenticationException e) {
            log.warn("Login failed for user: {}", loginRequest.getEmailOrUsername());

            // Find user and increment failed attempts
            userRepository.findByEmailOrUsername(
                    loginRequest.getEmailOrUsername(),
                    loginRequest.getEmailOrUsername()
            ).ifPresent(user -> {
                user.setFailedLoginAttempts((user.getFailedLoginAttempts() != null ? user.getFailedLoginAttempts() : 0) + 1);

                // Lock account after 5 failed attempts
                if (user.getFailedLoginAttempts() >= 5) {
                    user.setIsLocked(true);
                    log.warn("Account locked due to failed login attempts: {}", user.getId());
                }

                userRepository.save(user);
            });

            throw new UnauthorizedException("Invalid email/username or password");
        }
    }

    /**
     * Refresh JWT token
     */
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("Refreshing JWT token");

        String refreshToken = refreshTokenRequest.getRefreshToken();

        // Validate refresh token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        // Get username from refresh token
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

        // Get user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Generate new access token
        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        UserDTO userDTO = convertToUserDTO(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(userDTO)
                .build();
    }

    /**
     * Change user password
     */
    public void changePassword(String username, @Valid ChangePasswordRequest changePasswordRequest) {
        log.info("User changing password: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // Validate passwords match
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new BadRequestException("New passwords do not match");
        }

        // Validate password strength
        if (changePasswordRequest.getNewPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }

        // Ensure new password is different from old password
        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", username);
    }

    /**
     * Get user profile
     */
    @Transactional(readOnly = true)
    public UserDTO getUserProfile(String username) {
        log.debug("Fetching user profile: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        return convertToUserDTO(user);
    }

    /**
     * Update user profile
     */
    public UserDTO updateUserProfile(String username, @Valid  UserDTO userDTO) {
        log.info("Updating user profile: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        user = userRepository.save(user);
        log.info("User profile updated: {}", user.getId());

        return convertToUserDTO(user);
    }

    /**
     * Unlock user account (ADMIN only)
     */
    public void unlockUserAccount(Long userId) {
        log.info("Unlocking user account: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsLocked(false);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        log.info("User account unlocked: {}", userId);
    }

    /**
     * Verify email (for email verification)
     */
    public void verifyEmail(String username) {
        log.info("Verifying email for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        user.setIsEmailVerified(true);
        userRepository.save(user);

        log.info("Email verified for user: {}", username);
    }

    // Helper method
    private UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profileImageUrl(user.getProfileImageUrl())
                .phoneNumber(user.getPhoneNumber())
                .isEmailVerified(user.getIsEmailVerified())
                .isPhoneVerified(user.getIsPhoneVerified())
                .build();
    }
}
