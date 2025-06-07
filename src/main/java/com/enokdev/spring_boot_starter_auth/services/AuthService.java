package com.enokdev.spring_boot_starter_auth.services;

import com.enokdev.spring_boot_starter_auth.api.IAuthService;
import com.enokdev.spring_boot_starter_auth.config.IPUtils;
import com.enokdev.spring_boot_starter_auth.dtos.AuthResponse;
import com.enokdev.spring_boot_starter_auth.dtos.LoginRequest;
import com.enokdev.spring_boot_starter_auth.dtos.RegisterRequest;
import com.enokdev.spring_boot_starter_auth.dtos.AuthUserDTO;
import com.enokdev.spring_boot_starter_auth.dtos.UserProfileUpdateDto;
import com.enokdev.spring_boot_starter_auth.entities.LoginHistory;
import com.enokdev.spring_boot_starter_auth.entities.AuthUser;
import com.enokdev.spring_boot_starter_auth.exeption.AccountLockedException;
import com.enokdev.spring_boot_starter_auth.exeption.EmailAlreadyExistsException;
import com.enokdev.spring_boot_starter_auth.exeption.InvalidTokenException;
import com.enokdev.spring_boot_starter_auth.exeption.TokenExpiredException;
import com.enokdev.spring_boot_starter_auth.exeption.UserNotActiveException;
import com.enokdev.spring_boot_starter_auth.exeption.UserNotFoundException;
import com.enokdev.spring_boot_starter_auth.repositories.LoginHistoryRepository;
import com.enokdev.spring_boot_starter_auth.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


// Implémentation
@Service
@Slf4j
public class AuthService implements IAuthService {

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 24; // en heures
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private  final LoginHistoryRepository loginHistoryRepository;
    private final EmailService emailService;

    private  final JavaMailSender mailSender;


    private final TemplateEngine templateEngine;


    private  final IPUtils ipUtils;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager, LoginHistoryRepository loginHistoryRepository, EmailService emailService, JavaMailSender mailSender, TemplateEngine templateEngine, IPUtils ipUtils
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.loginHistoryRepository = loginHistoryRepository;
        this.emailService = emailService;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.ipUtils = ipUtils;
    }




    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            // Vérifier si l'utilisateur existe
            AuthUser user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Vérifier si le compte est activé
            if (!user.isEnabled()) {
                log.warn("Login attempt for inactive account: {}", request.getUsername());
               return AuthResponse.builder()
                       .message("Your account is inactive")
                       .build();
            }

            // Vérifier si le compte est verrouillé
            if (!user.isAccountNonLocked()) {
                // Vérifier si la période de verrouillage est terminée
                unlockWhenTimeExpired(user);

                if (!user.isAccountNonLocked()) {
                    log.warn("Login attempt for locked account: {}", request.getUsername());
                    return AuthResponse.builder()
                            .message("Account is locked")
                            .build();
                }
            }

            try {
                // Tentative d'authentification
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

                // Authentification réussie
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Enregistrer la tentative de connexion réussie
                recordLoginAttempt(user.getEmail(), true);

                // Générer le token JWT
                String token = jwtService.generateToken(user);

              //  log.info("Successful login for user: {}", request.getUsername());

                return AuthResponse.builder()
                        .token(token)
                        .type("Bearer")
                        .id(user.getId())
                        .username(user.getUsername())
                        .roles(user.getRoles())
                        .build();

            } catch (AuthenticationException e) {

                recordLoginAttempt(user.getEmail(), false);
                return AuthResponse.builder()
                        .message("Invalid credentials")
                        .build();
            }

        } catch (UserNotFoundException | UserNotActiveException |
                AccountLockedException | BadCredentialsException e) {
            throw e;
        } catch (Exception e) {

            return AuthResponse.builder()
                    .message("Error during login process " + e.getMessage())
                    .build();
        }
    }

    @Override
    public AuthUserDTO getCurrentUser() {
        try {
            // Récupérer l'authentification courante
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Vérifier si l'authentification existe
            if (authentication == null) {
                log.error("No authentication found in SecurityContext");
                throw new UserNotFoundException("No authentication found");

            }


            // Tenter de trouver l'utilisateur par username ET email
            AuthUser user = userRepository.findByUsernameOrEmail(authentication.getName(), authentication.getName())
                    .orElseThrow(() -> {
                        log.error("User not found for authenticated username/email: {}", authentication.getName());
                        return new UserNotFoundException("User not found for authenticated session");
                    });

            log.info("Successfully retrieved current user: {}", user.getUsername());

            return AuthUserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .roles(user.getRoles())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .enabled(user.isEnabled())
                    .accountNonLocked(user.isAccountNonLocked())
                    .lastLogin(user.getLastLogin())
                    .build();

        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting current user", e);
            throw new RuntimeException("Error retrieving current user information", e);
        }
    }



    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
    @Override
    public AuthResponse register(RegisterRequest registrationDto) {
        log.debug("Starting registration process for email: {}", registrationDto.getEmail());

        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            log.warn("Registration attempt with existing email: {}", registrationDto.getEmail());
            throw new EmailAlreadyExistsException("Email already registered");
        }

        try {
            // Créer un nouvel utilisateur
            AuthUser user = new AuthUser();

            user.setEmail(registrationDto.getEmail());
            user.setUsername(registrationDto.getUsername());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setFirstName(registrationDto.getFirstName());
            user.setLastName(registrationDto.getLastName());
            user.setEnabled(false); // ou false si vous voulez une confirmation par email
            user.setCreatedAt(LocalDateTime.now());

            // Initialiser les rôles
            Set<String> roles = new HashSet<>();
            roles.add("ROLE_USER");
            user.setRoles(roles);

            // Générer le token de confirmation
            String confirmationToken = generateToken();
            user.setEmailConfirmationToken(confirmationToken);

            // Sauvegarder l'utilisateur
            AuthUser savedUser = userRepository.save(user);
            log.info("User registered successfully: {}", savedUser.getEmail());

            // Envoyer l'email de confirmation
            emailService.sendConfirmationEmail(savedUser.getEmail(), confirmationToken);

            // Générer le token JWT
            String jwtToken = jwtService.generateToken(savedUser);

            // Retourner la réponse
            return AuthResponse.builder()
                    .token(jwtToken)
                    .type("Bearer")
                    .id(savedUser.getId())
                    .username(savedUser.getUsername())
                    .roles(savedUser.getRoles())
                    .email(savedUser.getEmail())
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .build();

        } catch (Exception e) {
            log.error("Error during registration process", e);
            throw new RuntimeException("Failed to register user", e);
        }
    }

    public void confirmEmail(String token) {
        AuthUser user = userRepository.findByEmailConfirmationToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid confirmation token"));

        user.setEnabled(true);
        user.setEmailConfirmationToken(null);
        userRepository.save(user);
    }

    public void initiatePasswordReset(String email) {
        AuthUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String resetToken = generateToken();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));

        userRepository.save(user);
        emailService.sendPasswordResetEmail(email, resetToken);
    }

    public void resetPassword(String token, String newPassword) {
        AuthUser user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }
    @Override

    public AuthUser updateProfile(Long userId, UserProfileUpdateDto updateDto) {
        AuthUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (updateDto.getFirstName() != null) {
            user.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            user.setLastName(updateDto.getLastName());
        }

        userRepository.save(user);
        return user;
    }
    @Override

    public void recordLoginAttempt(String email, boolean success) {
        AuthUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUser(user);
        loginHistory.setTimestamp(LocalDateTime.now());
        loginHistory.setSuccess(success);
        loginHistory.setIpAddress(ipUtils.getCurrentIpAddress());

        if (!success) {
            if (user.getFailedAttempts() + 1 >= MAX_FAILED_ATTEMPTS) {
                lockUser(user);
            } else {
                user.setFailedAttempts(user.getFailedAttempts() + 1);
            }
        } else {
            user.setFailedAttempts(0);
        }

        userRepository.save(user);
    }

    @Override
    public List<LoginHistory> getLoginHistory(Long userId) {
        AuthUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        try {
            return loginHistoryRepository.findByUserOrderByTimestampDesc(user);
        } catch (Exception e) {
            log.error("Failed to retrieve login history for user ID: {}", userId, e);
            throw new RuntimeException("Failed to retrieve login history", e);
        }
    }


    private void lockUser(AuthUser user) {
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendAccountLockedEmail(user.getEmail());
    }
    @Override
    public void unlockWhenTimeExpired(AuthUser user) {
        if (user.getLockTime() != null && user.getLockTime().plusHours(LOCK_TIME_DURATION)
                .isBefore(LocalDateTime.now())) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempts(0);
            userRepository.save(user);
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }



}